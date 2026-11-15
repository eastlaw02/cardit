package com.cardit.car

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.snapshotFlow
import com.cardit.car.credits.CastMember
import com.cardit.car.credits.CreditBuilder
import com.cardit.car.credits.CreditSheet
import com.cardit.car.credits.RoleRecommender
import com.cardit.car.credits.TripNumbers
import com.cardit.car.playback.ScreeningSession
import com.cardit.car.vehicle.CabinLightController
import com.cardit.car.vehicle.CinemaLight
import com.cardit.car.vehicle.PleosCabinLightController
import com.cardit.car.vehicle.UnsupportedCabinLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import java.io.File
import com.cardit.car.data.Chapter
import com.cardit.car.data.Genre
import com.cardit.car.data.Permission
import com.cardit.car.data.SampleData
import com.cardit.car.gate.GateDecision
import com.cardit.car.gate.PlaybackGate
import com.cardit.car.signal.CarUxDrivingSignalSource
import com.cardit.car.signal.DrivingSignalSource
import com.cardit.car.signal.FakeDrivingSignalSource
import com.cardit.car.signal.UxSignal
import kotlinx.coroutines.flow.StateFlow
import com.cardit.car.vehicle.FakeVehicleSnapshotReader
import com.cardit.car.vehicle.PleosVehicleSnapshotReader
import com.cardit.car.vehicle.RainAccumulator
import com.cardit.car.vehicle.VehicleSnapshot
import com.cardit.car.vehicle.VehicleSnapshotReader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class Screen { HOME, NEW_TRIP, DRIVING, BLOCKED, WRAP_UP, STYLE, CAST, TIMELINE, PRESHOW, CREDITS, COOKIE }

enum class SignalMode { REAL, DEMO }

/** 재생 화면 — 주행 중에는 반드시 차단되어야 하는 화면 */
private val PLAYBACK_SCREENS = setOf(Screen.PRESHOW, Screen.CREDITS, Screen.COOKIE)

class CarditViewModel(app: Application) : AndroidViewModel(app) {

    // ───────── 화면 상태 ─────────
    var screen by mutableStateOf(Screen.HOME)
        private set
    private var resumeTarget: Screen? = null
    private var drivingReturn: Screen? = null

    var toast by mutableStateOf<String?>(null)
        private set
    var devPanelOpen by mutableStateOf(false)
    var roleEditorOpen by mutableStateOf(false)

    // ───────── 여행 ─────────
    var tripTitle by mutableStateOf("부산 우정여행")
    var destination by mutableStateOf("부산 해운대")
    var tripActive by mutableStateOf(true)
        private set
    val permissions = mutableStateListOf(*SampleData.participants.map { it.permission }.toTypedArray())
    var cookiesSaved by mutableIntStateOf(0)
        private set
    var stats by mutableStateOf(SampleData.demoStats)
        private set
    private var startSnapshot: VehicleSnapshot? = null
    var latestSnapshot by mutableStateOf<VehicleSnapshot?>(null)
        private set
    /** 여행 중 앱이 포그라운드인 동안 와이퍼 상태를 주기적으로 샘플링해 강우 시간을 누적 */
    private var rain = RainAccumulator()
    private var rainJob: Job? = null

    // ───────── 제작 ─────────
    var genre by mutableStateOf(Genre.COMEDY)
    var bgmPreview by mutableStateOf(false)
    /** 활동 기록 기반 역할 추천 — 화면에서 'AI 추천' / '직접 지정' 판정 기준 */
    val recommendations = RoleRecommender.recommend(SampleData.activities)
    val roles = mutableStateListOf(*recommendations.map { it.role }.toTypedArray())
    var selectedCast by mutableIntStateOf(0)
    val chapters = mutableStateListOf(*SampleData.chapters.toTypedArray())
    var selectedChapter by mutableStateOf("ch3")
    val addedHighlights = mutableStateListOf<String>()
    var demoCut by mutableStateOf(true)

    // ───────── 재생 ─────────
    var positionMs by mutableLongStateOf(0L)
        private set
    var playing by mutableStateOf(false)
        private set
    var volume by mutableStateOf(0.75f)
    var cookiePositionMs by mutableLongStateOf(27_000L)
        private set
    var cookiePlaying by mutableStateOf(false)
        private set
    /** 시연판 1분 / 정식판 = 챕터 길이 합 (같은 크레딧 데이터에서 재생 길이만 다르게) */
    val durationMs: Long get() = if (demoCut) 60_000L else chapters.sumOf { it.durationSec } * 1000L
    val cookieDurationMs = 42_000L
    private var ticker: Job? = null

    // ───────── 신호 / 게이트 ─────────
    // 차량 서비스가 없는 환경(일반 휴대폰 등)에서는 '항상 판정 불가' 신호/미지원 리더로 대체 → fail-safe
    private val realSource: DrivingSignalSource =
        runCatching { CarUxDrivingSignalSource(app) }.getOrElse { UnavailableSignalSource }
    val demoSource = FakeDrivingSignalSource()
    private val realReader: VehicleSnapshotReader =
        runCatching { PleosVehicleSnapshotReader(app) }.getOrElse { UnavailableSnapshotReader }
    private val demoReader: VehicleSnapshotReader = FakeVehicleSnapshotReader()
    /** 기본은 실차 신호(AAOS CarUxRestrictions). 개발 패널에서 시연 신호로 전환 가능 */
    private val modeFlow = MutableStateFlow(SignalMode.REAL)
    var signalMode by mutableStateOf(SignalMode.REAL)
        private set
    var gate by mutableStateOf(GateDecision.BLOCK_UNKNOWN)
        private set

    // ───────── 조명 연출 / 미디어 세션 ─────────
    private val realLight: CabinLightController =
        runCatching { PleosCabinLightController(app) }.getOrElse { UnsupportedCabinLight }
    var cinemaLight by mutableStateOf(CinemaLight.OFF)
        private set
    private val mediaSession: ScreeningSession? =
        runCatching { ScreeningSession(app, onPlay = ::resumeFromSystem, onPause = ::pauseFromSystem) }.getOrNull()

    /** 지금 영상이 실제로 재생 중인지 (크레딧 또는 쿠키) */
    private val screeningNow: Boolean
        get() = screen in PLAYBACK_SCREENS && (playing || cookiePlaying)

    init {
        realSource.start()
        viewModelScope.launch { collectGate() }
        viewModelScope.launch { startSnapshot = reader().read() }
        viewModelScope.launch { syncCinemaLight() }
        viewModelScope.launch { syncMediaSession() }
    }

    /** 재생 시작 → 시네마 조명 / 일시정지·차단·종료 → 원래 밝기로 즉시 복원 */
    private suspend fun syncCinemaLight() {
        snapshotFlow { screeningNow }
            .distinctUntilChanged()
            .collect { on ->
                if (on) {
                    val controller = if (signalMode == SignalMode.REAL) realLight else UnsupportedCabinLight
                    cinemaLight = if (controller.enterCinema()) CinemaLight.VEHICLE else CinemaLight.SCREEN
                    // 조명을 켜는 사이 재생이 멈췄다면 바로 되돌린다
                    if (!screeningNow) {
                        realLight.restore()
                        cinemaLight = CinemaLight.OFF
                    }
                } else {
                    realLight.restore()
                    cinemaLight = CinemaLight.OFF
                }
            }
    }

    private suspend fun syncMediaSession() {
        val session = mediaSession ?: return
        snapshotFlow { Triple(screen, playing, cookiePlaying) }
            .distinctUntilChanged()
            .collect { (s, p, c) ->
                val cookie = s == Screen.COOKIE
                session.update(
                    title = if (cookie) "$tripTitle · 쿠키 영상" else tripTitle,
                    subtitle = "${genre.label} 엔딩 크레딧 · CARDIT",
                    active = s in PLAYBACK_SCREENS || s == Screen.BLOCKED,
                    playing = if (cookie) c else p,
                    positionMs = if (cookie) cookiePositionMs else positionMs,
                    durationMs = if (cookie) cookieDurationMs else durationMs,
                )
            }
    }

    private fun resumeFromSystem() {
        when (screen) {
            Screen.CREDITS -> play()
            Screen.COOKIE -> if (!cookiePlaying) toggleCookiePlay()
            else -> Unit
        }
    }

    private fun pauseFromSystem() {
        pause()
        cookiePlaying = false
    }

    /** 앱이 화면에서 사라지면 재생을 멈춘다 (포그라운드에서만 상영 · 조명 복원) */
    fun onBackground() = pauseFromSystem()

    /** 크레딧 데이터 — 화면과 JSON 내보내기가 같은 데이터를 쓴다 */
    val creditSheet: CreditSheet
        get() = CreditBuilder.build(
            title = tripTitle,
            location = destination,
            genre = genre.label,
            bgm = genre.bgm,
            cast = SampleData.participants.mapIndexed { i, p ->
                CastMember(p.name, roles[i], nameOnly = permissions[i] == Permission.NAME_ONLY)
            },
            numbers = TripNumbers(stats.distanceKm, stats.rainMinutes, stats.batteryUsedPct),
            sequence = chapters.map { it.title },
            durationMs = durationMs,
        )

    private fun exportCredits() {
        val json = creditSheet.toJson()
        val dir = getApplication<Application>().filesDir
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { File(dir, "credits.json").writeText(json) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun collectGate() {
        modeFlow
            .flatMapLatest { m -> if (m == SignalMode.REAL) realSource.signal else demoSource.signal }
            .map(PlaybackGate::decide)
            .collect { onGate(it) }
    }

    private fun reader() = if (signalMode == SignalMode.REAL) realReader else demoReader

    fun switchSignalMode(mode: SignalMode) {
        if (mode == signalMode) return
        if (signalMode == SignalMode.REAL) realSource.stop() else demoSource.stop()
        signalMode = mode
        if (mode == SignalMode.REAL) realSource.start() else demoSource.start()
        modeFlow.value = mode
    }

    /**
     * 게이트 → 화면 전환 규칙
     * - 재생 화면에서 차단되면 일시정지 후 차단 화면으로 (fail-safe 포함)
     * - 차단 화면에서 다시 허용되면 같은 지점부터 이어서 재생
     * - 그 밖의 화면에서 주행이 시작되면 주행 중 화면(Safe Glance)으로
     */
    private fun onGate(g: GateDecision) {
        gate = g
        when {
            g != GateDecision.ALLOW && screen in PLAYBACK_SCREENS -> {
                resumeTarget = screen
                pause()
                cookiePlaying = false
                screen = Screen.BLOCKED
            }
            g == GateDecision.ALLOW && screen == Screen.BLOCKED -> {
                val target = resumeTarget ?: Screen.CREDITS
                resumeTarget = null
                screen = target
                if (target == Screen.CREDITS) play()
            }
            g == GateDecision.BLOCK_DRIVING && screen != Screen.DRIVING && screen != Screen.BLOCKED -> {
                drivingReturn = screen
                screen = Screen.DRIVING
            }
            g == GateDecision.ALLOW && screen == Screen.DRIVING -> {
                screen = drivingReturn ?: Screen.HOME
                drivingReturn = null
            }
        }
    }

    // ───────── 내비게이션 ─────────
    fun go(target: Screen) {
        if (target in PLAYBACK_SCREENS && gate != GateDecision.ALLOW) {
            resumeTarget = target
            screen = Screen.BLOCKED
            return
        }
        if (screen == Screen.CREDITS && target != Screen.CREDITS) pause()
        if (screen == Screen.COOKIE) cookiePlaying = false
        if (gate == GateDecision.BLOCK_DRIVING && target != Screen.DRIVING) {
            resumeTarget = null
            drivingReturn = target
            screen = Screen.DRIVING
            return
        }
        screen = target
    }

    fun back() {
        go(
            when (screen) {
                Screen.NEW_TRIP, Screen.WRAP_UP, Screen.BLOCKED, Screen.DRIVING -> Screen.HOME
                Screen.STYLE -> Screen.WRAP_UP
                Screen.CAST -> Screen.STYLE
                Screen.TIMELINE -> Screen.CAST
                Screen.PRESHOW -> Screen.TIMELINE
                Screen.CREDITS -> Screen.PRESHOW
                Screen.COOKIE -> Screen.CREDITS
                Screen.HOME -> Screen.HOME
            },
        )
    }

    fun showToast(message: String) {
        toast = message
        viewModelScope.launch {
            delay(2_400)
            if (toast == message) toast = null
        }
    }

    // ───────── 여행 ─────────
    fun startTrip() {
        tripActive = true
        cookiesSaved = 0
        viewModelScope.launch { startSnapshot = reader().read() }
        startRainSampling()
        showToast("'$tripTitle' 촬영을 시작합니다")
        go(Screen.HOME)
    }

    private fun startRainSampling() {
        rainJob?.cancel()
        rain = RainAccumulator()
        rainJob = viewModelScope.launch {
            while (isActive) {
                val snap = reader().read()
                rain.add(snap.takenAtMillis, snap.wiperOn)
                delay(RAIN_SAMPLE_MS)
            }
        }
    }

    fun planNext(title: String, dest: String) {
        tripTitle = title
        destination = dest
        go(Screen.NEW_TRIP)
    }

    fun setPermission(index: Int, p: Permission) {
        permissions[index] = p
    }

    fun saveCookie() {
        cookiesSaved++
        showToast("지금 이 순간을 쿠키로 저장했어요")
    }

    /** 여행 종료: 시작/종료 스냅샷 차이로 통계를 갱신한다. 미지원 항목은 시연값 유지 */
    fun wrapUp() {
        rainJob?.cancel()
        viewModelScope.launch {
            val end = reader().read()
            latestSnapshot = end
            rain.add(end.takenAtMillis, end.wiperOn)
            val start = startSnapshot
            val distance = if (start?.odometer != null && end.odometer != null) (end.odometer - start.odometer).roundToInt() else null
            val used = if (start?.batteryPercent != null && end.batteryPercent != null) start.batteryPercent - end.batteryPercent else null
            stats = stats.copy(
                distanceKm = distance?.takeIf { it > 0 } ?: SampleData.demoStats.distanceKm,
                batteryUsedPct = used?.takeIf { it >= 0 } ?: SampleData.demoStats.batteryUsedPct,
                batteryLeftPct = end.batteryPercent ?: SampleData.demoStats.batteryLeftPct,
                rainMinutes = rain.minutes() ?: SampleData.demoStats.rainMinutes,
                highlights = SampleData.demoStats.highlights + cookiesSaved,
            )
        }
        go(Screen.WRAP_UP)
    }

    fun readSnapshot() {
        viewModelScope.launch { latestSnapshot = reader().read() }
    }

    // ───────── 제작 ─────────
    fun assignRole(role: String) {
        roles[selectedCast] = role
    }

    fun toggleHighlight(id: String) {
        if (!addedHighlights.remove(id)) addedHighlights.add(id)
    }

    fun moveChapter(from: Int, to: Int) {
        if (from == to || to !in chapters.indices) return
        chapters.add(to, chapters.removeAt(from))
    }

    fun chapterById(id: String): Chapter = chapters.firstOrNull { it.id == id } ?: chapters.first()

    // ───────── 재생 ─────────
    fun startScreening() {
        exportCredits()
        positionMs = 0
        go(Screen.CREDITS)
        if (screen == Screen.CREDITS) play()
    }

    fun play() {
        if (gate != GateDecision.ALLOW) return
        playing = true
        ticker?.cancel()
        ticker = viewModelScope.launch {
            while (isActive && playing) {
                delay(TICK_MS)
                positionMs = (positionMs + TICK_MS).coerceAtMost(durationMs)
                if (positionMs >= durationMs) {
                    playing = false
                    go(Screen.COOKIE)
                    toggleCookiePlay()
                }
            }
        }
    }

    fun pause() {
        playing = false
        ticker?.cancel()
    }

    fun togglePlay() = if (playing) pause() else play()

    fun seekBy(deltaMs: Long) {
        positionMs = (positionMs + deltaMs).coerceIn(0, durationMs)
    }

    fun seekTo(fraction: Float) {
        positionMs = (durationMs * fraction).toLong()
    }

    fun replay() {
        cookiePlaying = false
        startScreening()
    }

    fun toggleCookiePlay() {
        if (cookiePlaying) {
            cookiePlaying = false
            return
        }
        if (gate != GateDecision.ALLOW) return
        if (cookiePositionMs >= cookieDurationMs) cookiePositionMs = 0
        cookiePlaying = true
        viewModelScope.launch {
            while (isActive && cookiePlaying && cookiePositionMs < cookieDurationMs) {
                delay(TICK_MS)
                cookiePositionMs += TICK_MS
            }
            cookiePlaying = false
        }
    }

    fun seekCookie(deltaMs: Long) {
        cookiePositionMs = (cookiePositionMs + deltaMs).coerceIn(0, cookieDurationMs)
    }

    override fun onCleared() {
        realSource.stop()
        demoSource.stop()
        realReader.close()
        demoReader.close()
        realLight.close()
        mediaSession?.close()
    }

    private companion object {
        const val TICK_MS = 200L
        /** 와이퍼 샘플링 주기 — 기본 간격 제한(30초)의 1/3 */
        const val RAIN_SAMPLE_MS = 10_000L
    }
}

private object UnavailableSignalSource : DrivingSignalSource {
    override val signal: StateFlow<UxSignal?> = MutableStateFlow(null)
    override fun start() = Unit
    override fun stop() = Unit
}

private object UnavailableSnapshotReader : VehicleSnapshotReader {
    override suspend fun read() = VehicleSnapshot(odometer = null, batteryPercent = null, wiperOn = null)
    override fun close() = Unit
}

fun formatTime(ms: Long): String {
    val s = (ms / 1000).toInt()
    return "%02d:%02d".format(s / 60, s % 60)
}
