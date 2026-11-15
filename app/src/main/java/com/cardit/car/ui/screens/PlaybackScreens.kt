package com.cardit.car.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AirlineSeatReclineExtra
import androidx.compose.material.icons.rounded.AspectRatio
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.LocalMovies
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MovieCreation
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Phonelink
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.SurroundSound
import androidx.compose.material.icons.rounded.Theaters
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CarRental
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cardit.car.CarditViewModel
import com.cardit.car.R
import com.cardit.car.Screen
import com.cardit.car.data.SampleData
import com.cardit.car.formatTime
import com.cardit.car.ui.components.CinemaLightBadge
import com.cardit.car.ui.components.CoverImage
import com.cardit.car.ui.components.DarkButton
import com.cardit.car.ui.components.GlassCard
import com.cardit.car.ui.components.IconCircle
import com.cardit.car.ui.components.Overline
import com.cardit.car.ui.components.Pill
import com.cardit.car.ui.components.Scrubber
import com.cardit.car.ui.components.Waveform
import com.cardit.car.ui.components.cinemaBackground
import com.cardit.car.ui.components.pressable
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

// ─────────────────────────── 상영 대기 ───────────────────────────

@Composable
fun PreShowScreen(vm: CarditViewModel) {
    Box(
        Modifier.fillMaxSize().background(CarditColors.Canvas)
            .drawBehind {
                // 극장 커튼 결
                val stripe = size.width / 28f
                for (i in 0 until 28) {
                    drawRect(
                        Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.25f), Color.White.copy(alpha = 0.015f), Color.Black.copy(alpha = 0.25f)), startX = i * stripe, endX = (i + 1) * stripe),
                        topLeft = Offset(i * stripe, 0f), size = androidx.compose.ui.geometry.Size(stripe, size.height),
                    )
                }
                drawCircle(
                    Brush.radialGradient(listOf(CarditColors.Gold.copy(alpha = 0.28f), Color.Transparent), center = Offset(size.width / 2, size.height * 0.42f), radius = size.width * 0.42f),
                    radius = size.width * 0.42f, center = Offset(size.width / 2, size.height * 0.42f),
                )
            },
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().background(CarditColors.Canvas.copy(alpha = 0.8f)).padding(horizontal = 48.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                IconCircle(Icons.Rounded.Theaters, size = 64.dp, container = CarditColors.SurfaceHigh, tint = CarditColors.Gold) { vm.go(Screen.TIMELINE) }
                Spacer(Modifier.width(18.dp))
                Column(Modifier.weight(1f)) {
                    Text("VEHICLE THEATER STAGE", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                    Text("프라이빗 시네마 모드", style = CarditType.HeadlineLg, color = CarditColors.TextHigh)
                }
                Pill("Acoustic Immersion ON", icon = Icons.Rounded.SurroundSound, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, iconTint = CarditColors.Gold, height = 56.dp)
                Spacer(Modifier.width(20.dp))
                Pill("배터리 ${vm.latestSnapshot?.batteryPercent ?: 84}%", icon = Icons.Rounded.BatteryChargingFull, container = CarditColors.SurfaceMid, content = CarditColors.TextHigh, iconTint = CarditColors.Gold, height = 56.dp)
            }
            Column(Modifier.weight(1f).fillMaxWidth().padding(top = 36.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Pill("OFFICIAL IN-CABIN SELECTION", icon = Icons.Rounded.WorkspacePremium, container = CarditColors.SurfaceMid, content = CarditColors.GoldSoft, iconTint = CarditColors.Gold, height = 52.dp, style = CarditType.LabelLg)
                    Pill("4인 출연 · ${vm.genre.label} 로드무비", container = CarditColors.GoldDeep.copy(alpha = 0.6f), content = CarditColors.GoldSoft, height = 52.dp, style = CarditType.LabelLg)
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    vm.tripTitle,
                    style = CarditType.DisplayXl.copy(fontSize = CarditType.DisplayXl.fontSize * 1.4f, lineHeight = CarditType.DisplayXl.lineHeight * 1.4f, shadow = Shadow(CarditColors.Gold.copy(alpha = 0.5f), blurRadius = 40f)),
                    color = CarditColors.TextHigh,
                )
                Box(Modifier.width(240.dp).height(4.dp).background(Brush.horizontalGradient(listOf(Color.Transparent, CarditColors.Gold, Color.Transparent))))
                Spacer(Modifier.height(20.dp))
                Text("A  CARDIT  ORIGINAL  ·  2026  ·  EXCLUSIVE  SCREENING", style = CarditType.LabelLg.copy(letterSpacing = CarditType.LabelLg.letterSpacing * 5), color = CarditColors.TextWarm)
                Spacer(Modifier.height(36.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Pill("주차 확인됨 (P 기어)", icon = Icons.Rounded.CheckCircle, container = CarditColors.SurfaceMid, content = CarditColors.TextHigh, iconTint = CarditColors.Gold, height = 60.dp, style = CarditType.HeadlineSm)
                    Pill("상영 시 무드 조명 시네마 모드", icon = Icons.Rounded.Lightbulb, container = CarditColors.SurfaceMid, content = CarditColors.TextHigh, iconTint = CarditColors.Gold, height = 60.dp, style = CarditType.HeadlineSm)
                    Pill("시네마 3D 오디오 준비됨", icon = Icons.Rounded.SurroundSound, container = CarditColors.SurfaceMid, content = CarditColors.TextHigh, iconTint = CarditColors.TextHigh, height = 60.dp, style = CarditType.HeadlineSm)
                }
                Spacer(Modifier.weight(1f))
                Column(
                    Modifier.size(150.dp).pressable { vm.startScreening() }
                        .shadow(60.dp, CircleShape, ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold)
                        .background(CarditColors.Gold, CircleShape),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                ) {
                    Icon(Icons.Rounded.PlayArrow, null, tint = CarditColors.OnGold, modifier = Modifier.size(64.dp))
                    Text("상영 시작", style = CarditType.LabelLg, color = CarditColors.OnGold)
                }
                Spacer(Modifier.height(28.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Pill("볼륨 ${(vm.volume * 100).toInt()}%", icon = Icons.Rounded.VolumeUp, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 52.dp)
                    Pill("좌석 릴렉스 모드", icon = Icons.Rounded.AirlineSeatReclineExtra, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 52.dp)
                    Pill("와이드 21:9 비율", icon = Icons.Rounded.AspectRatio, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 52.dp)
                }
                Spacer(Modifier.weight(1.4f))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 40.dp)) {
                    Icon(Icons.Rounded.VerifiedUser, null, tint = CarditColors.TextWarm, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("주행을 시작하면 안전을 위해 영상이 자동으로 일시정지되고 사운드 전용 모드로 전환됩니다.", style = CarditType.BodyLg, color = CarditColors.TextWarm)
                }
            }
        }
    }
}

// ─────────────────────────── 엔딩 크레딧 재생 ───────────────────────────

@Composable
fun CreditsScreen(vm: CarditViewModel) {
    val progress = vm.positionMs / vm.durationMs.toFloat()
    val chapters = vm.chapters
    val chapterIndex = (progress * chapters.size).toInt().coerceIn(0, chapters.lastIndex)
    val frame = if (chapters[chapterIndex].id == "ch3") R.drawable.img_credits_cabin else chapters[chapterIndex].image
    val sheet = vm.creditSheet

    Column(Modifier.fillMaxSize().cinemaBackground(glowAlpha = 0.05f).padding(horizontal = 48.dp, vertical = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.DirectionsCar, null, tint = CarditColors.TextWarm, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(14.dp))
            Text("DRIVE CINEMA MEMORIES  •  AAOS STATIONARY REPLAY", style = CarditType.LabelLg, color = CarditColors.TextHigh, modifier = Modifier.weight(1f).pressable { vm.devPanelOpen = true })
            CinemaLightBadge(vm.cinemaLight)
            Spacer(Modifier.width(16.dp))
            Pill("쿠키 영상 있음 ★", icon = Icons.Rounded.Star, container = CarditColors.GoldDeep.copy(alpha = 0.35f), content = CarditColors.GoldSoft, iconTint = CarditColors.Gold, border = CarditColors.Gold.copy(alpha = 0.3f), height = 52.dp, style = CarditType.LabelLg) {
                vm.go(Screen.COOKIE)
            }
        }
        Spacer(Modifier.height(36.dp))
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(60.dp)) {
            // 영상 프레임 (Ken Burns)
            Box(Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(36.dp)).background(Color.Black)) {
                Crossfade(frame, animationSpec = tween(900), label = "frame") { res ->
                    Image(
                        painterResource(res), null, contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().graphicsLayer {
                            val local = (progress * chapters.size) % 1f
                            val s = 1.04f + 0.06f * local
                            scaleX = s; scaleY = s
                        },
                    )
                }
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0.7f to Color.Transparent, 1f to CarditColors.Canvas.copy(alpha = 0.9f))))
                Text("2.39 : 1  •  24 FPS  •  CINEMA SOUND IN-CABIN", style = CarditType.LabelSm, color = CarditColors.TextMid, modifier = Modifier.padding(26.dp))
                Row(Modifier.align(Alignment.BottomStart).padding(28.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.MovieCreation, null, tint = CarditColors.Gold, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("ROLL 0${chapterIndex + 1} • ROAD TRIP ARCHIVE", style = CarditType.LabelLg, color = CarditColors.GoldSoft)
                        Text(chapters[chapterIndex].sub, style = CarditType.BodyLg, color = CarditColors.TextHigh)
                    }
                }
            }
            // 크레딧
            Column(Modifier.weight(0.8f).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Overline("EPILOGUE  &  CREDITS")
                Spacer(Modifier.height(12.dp))
                Text("여정의 끝, 그리고 시작", style = CarditType.DisplayLg, color = CarditColors.TextHigh)
                Spacer(Modifier.height(36.dp))
                Column(Modifier.fillMaxWidth().background(CarditColors.SurfaceLow, RoundedCornerShape(24.dp)).padding(26.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    InfoLine(Icons.Rounded.Place, "촬영지", sheet.location)
                    // 차량이 지원하지 않는 통계는 크레딧 데이터에서 이미 빠져 있다
                    if (sheet.stats.isNotEmpty()) {
                        InfoLine(Icons.Rounded.Speed, "주행 기록", sheet.stats.joinToString("  ·  ") { "${it.label} ${it.value}" })
                    }
                }
                Spacer(Modifier.height(36.dp))
                sheet.cast.forEachIndexed { i, entry ->
                    val role = entry.label
                    val name = entry.value
                    CreditLine(role, name, CarditType.HeadlineMd, if (i % 2 == 0) CarditColors.GoldSoft else CarditColors.TextHigh, visible = progress >= i * 0.06f)
                }
                CreditLine("차량 사운드 트랙", vm.genre.bgm, CarditType.BodyXl, CarditColors.TextHigh, labelColor = CarditColors.TextWarm, visible = progress >= 0.3f)
                CreditLine("스페셜 땡스", "오션뷰 전기차 충전소 #4", CarditType.BodyXl, CarditColors.TextHigh, labelColor = CarditColors.TextWarm, visible = progress >= 0.36f)
                Spacer(Modifier.height(36.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.WorkspacePremium, null, tint = CarditColors.TextWarm, modifier = Modifier.size(36.dp))
                    Spacer(Modifier.width(20.dp))
                    Text("OFFICIAL IN-CABIN HIGH-RES ARCHIVE", style = CarditType.LabelMd.copy(letterSpacing = CarditType.LabelMd.letterSpacing * 3), color = CarditColors.TextWarm)
                    Spacer(Modifier.width(20.dp))
                    Icon(Icons.Rounded.WorkspacePremium, null, tint = CarditColors.TextWarm, modifier = Modifier.size(36.dp))
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Scrubber(progress, Modifier.fillMaxWidth(), track = 10.dp, thumb = 26.dp) { vm.seekTo(it) }
        Row(Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text(formatTime(vm.positionMs), style = CarditType.LabelLg, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
            Text(formatTime(vm.durationMs), style = CarditType.LabelLg, color = CarditColors.TextWarm)
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            DarkButton("10초 뒤로", leading = Icons.Rounded.Replay10, height = 80.dp, color = CarditColors.SurfaceMid) { vm.seekBy(-10_000) }
            Spacer(Modifier.width(24.dp))
            Pill("Chapter ${chapterIndex + 1} : ${chapters[chapterIndex].title}", icon = Icons.Rounded.BookmarkBorder, container = CarditColors.SurfaceLow, content = CarditColors.TextWarm, height = 56.dp)
            Spacer(Modifier.weight(1f))
            Box(
                Modifier.size(100.dp).pressable { vm.togglePlay() }
                    .shadow(40.dp, CircleShape, ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold)
                    .background(CarditColors.Gold, CircleShape),
                contentAlignment = Alignment.Center,
            ) { Icon(if (vm.playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null, tint = CarditColors.OnGold, modifier = Modifier.size(52.dp)) }
            Spacer(Modifier.weight(1f))
            Row(
                Modifier.background(CarditColors.SurfaceMid, RoundedCornerShape(24.dp)).height(80.dp).padding(horizontal = 28.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Rounded.VolumeUp, null, tint = CarditColors.TextHigh, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(16.dp))
                Scrubber(vm.volume, Modifier.width(120.dp), track = 10.dp, thumb = 0.dp) { vm.volume = it }
                Spacer(Modifier.width(16.dp))
                Text("${(vm.volume * 100).toInt()}%", style = CarditType.LabelMd, color = CarditColors.TextHigh)
            }
            Spacer(Modifier.width(24.dp))
            DarkButton(
                "쿠키 바로보기", leading = Icons.Rounded.FastForward, height = 80.dp,
                color = CarditColors.GoldDeep.copy(alpha = 0.45f), contentColor = CarditColors.GoldSoft, iconTint = CarditColors.Gold,
            ) {
                vm.go(Screen.COOKIE)
                vm.toggleCookiePlay()
            }
        }
    }
}

@Composable
private fun InfoLine(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = CarditColors.Gold, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, style = CarditType.LabelLg, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
        Text(value, style = CarditType.BodyXl.copy(fontWeight = null), color = CarditColors.TextHigh)
    }
}

@Composable
private fun CreditLine(label: String, value: String, style: TextStyle, valueColor: Color, labelColor: Color = CarditColors.TextHigh, visible: Boolean) {
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp).alpha(if (visible) 1f else 0.08f), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = style.copy(fontWeight = null), color = labelColor)
        Box(
            Modifier.weight(1f).height(2.dp).padding(horizontal = 24.dp).drawBehind {
                drawLine(CarditColors.TextLow, Offset(0f, 0f), Offset(size.width, 0f), 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 6f)))
            },
        )
        Text(value, style = style, color = valueColor, textAlign = TextAlign.End)
    }
}

// ─────────────────────────── 쿠키 영상 + 마무리 ───────────────────────────

@Composable
fun CookieScreen(vm: CarditViewModel) {
    val cookieProgress = vm.cookiePositionMs / vm.cookieDurationMs.toFloat()
    Column(
        Modifier.fillMaxSize().cinemaBackground(glowAlpha = 0.12f).padding(horizontal = 48.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("———   POST-CREDITS  EPILOGUE   ———", style = CarditType.LabelLg.copy(letterSpacing = CarditType.LabelLg.letterSpacing * 4), color = CarditColors.TextWarm)
        Spacer(Modifier.height(8.dp))
        Text("THE END", style = CarditType.DisplayXl.copy(letterSpacing = CarditType.LabelLg.letterSpacing * 6), color = CarditColors.GoldSoft)
        Text("BUSAN ROAD CHRONICLES  •  2026 AUTUMN", style = CarditType.LabelLg.copy(letterSpacing = CarditType.LabelLg.letterSpacing * 2), color = CarditColors.TextWarm)
        Spacer(Modifier.height(36.dp))

        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            GlassCard(Modifier.weight(2f).fillMaxHeight(), radius = 40.dp, fill = CarditColors.SurfaceMid, padding = PaddingValues(40.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Pill("BONUS TRACK • 쿠키 영상", icon = Icons.Rounded.LocalMovies, container = CarditColors.CoralSoft.copy(alpha = 0.18f), content = CarditColors.TextHigh, height = 52.dp, style = CarditType.LabelLg)
                    Spacer(Modifier.weight(1f))
                    Pill("In-Cabin Recording", icon = Icons.Rounded.Mic, container = CarditColors.Canvas.copy(alpha = 0.4f), content = CarditColors.TextWarm, iconTint = CarditColors.Gold, height = 44.dp)
                }
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1.2f)) {
                        Text("잠깐! 쿠키 영상이 있어요 🍪", style = CarditType.DisplayMd, color = CarditColors.TextHigh)
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.CarRental, null, tint = CarditColors.Gold, modifier = Modifier.size(26.dp))
                            Spacer(Modifier.width(10.dp))
                            Text("차 안에서 — 준호의 열창 모먼트", style = CarditType.HeadlineLg, color = CarditColors.Gold, modifier = Modifier.weight(1f))
                            Pill(formatTime(vm.cookieDurationMs), container = CarditColors.SurfaceHigh, content = CarditColors.TextWarm, height = 40.dp, shape = RoundedCornerShape(8.dp))
                        }
                        Spacer(Modifier.height(24.dp))
                        Column(Modifier.fillMaxWidth().background(CarditColors.Canvas.copy(alpha = 0.6f), RoundedCornerShape(22.dp)).padding(26.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Mic, null, tint = CarditColors.GoldSoft, modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(10.dp))
                                Text("VOICE TRACK #04 • 기장 COASTLINE", style = CarditType.LabelMd, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
                                Text("${formatTime(vm.cookiePositionMs)} / ${formatTime(vm.cookieDurationMs)}", style = CarditType.LabelLg, color = CarditColors.GoldSoft)
                            }
                            Spacer(Modifier.height(18.dp))
                            Waveform(
                                SampleData.voiceWave, barWidth = 10.dp, gap = 8.dp, maxHeight = 70.dp,
                                activeCount = (SampleData.voiceWave.size * cookieProgress).toInt(),
                                colors = listOf(CarditColors.Gold, CarditColors.CoralSoft, CarditColors.GoldSoft),
                            )
                            Spacer(Modifier.height(18.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconCircle(if (vm.cookiePlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, size = 76.dp, container = CarditColors.Gold, tint = CarditColors.OnGold) { vm.toggleCookiePlay() }
                                Spacer(Modifier.weight(1f))
                                IconCircle(Icons.Rounded.Replay10, size = 56.dp, tint = CarditColors.TextHigh) { vm.seekCookie(-10_000) }
                                Spacer(Modifier.width(14.dp))
                                IconCircle(Icons.Rounded.Forward10, size = 56.dp, tint = CarditColors.TextHigh) { vm.seekCookie(10_000) }
                                Spacer(Modifier.width(18.dp))
                                Icon(Icons.Rounded.GraphicEq, null, tint = CarditColors.TextWarm, modifier = Modifier.size(30.dp))
                            }
                        }
                    }
                    Spacer(Modifier.width(32.dp))
                    CoverImage(R.drawable.img_cookie_cabin, Modifier.weight(1f).height(300.dp), radius = 26.dp, scrim = 0.5f) {
                        IconCircle(Icons.Rounded.Videocam, size = 52.dp, container = CarditColors.Canvas.copy(alpha = 0.7f), tint = CarditColors.TextHigh, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp))
                        Pill("SYNCED DASH-CAM", dot = CarditColors.Rec, container = CarditColors.Canvas.copy(alpha = 0.7f), content = CarditColors.TextHigh, height = 40.dp, modifier = Modifier.align(Alignment.BottomStart).padding(16.dp))
                    }
                }
            }
            NextTripCard(vm, Modifier.weight(1f).fillMaxHeight())
        }
        Spacer(Modifier.height(36.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            BigAction("다시 보기", "본편 무비 런타임 ${formatTime(vm.durationMs)}", Icons.Rounded.Replay, Icons.Rounded.PlayCircleOutline, gold = false, Modifier.weight(1f)) { vm.replay() }
            BigAction("휴대폰으로 보내기", "고화질 클라우드 전송", Icons.Rounded.Phonelink, Icons.Rounded.QrCode2, gold = true, Modifier.weight(1f)) {
                vm.showToast("MP4 내보내기·휴대폰 전송은 3단계에서 지원됩니다")
            }
            BigAction("SNS 공유", "Reels • Shorts 9:16", Icons.Rounded.Share, Icons.Rounded.IosShare, gold = false, Modifier.weight(1f), circleColor = CarditColors.Coral.copy(alpha = 0.35f)) {
                vm.showToast("SNS 공유는 3단계에서 지원됩니다")
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(12.dp).background(CarditColors.Gold, CircleShape))
            Spacer(Modifier.width(10.dp))
            Text("PARKED  •  AAOS CINEMA MODE ACTIVE", style = CarditType.LabelMd, color = CarditColors.TextWarm, modifier = Modifier.weight(1f))
            Text("홈으로", style = CarditType.LabelLg, color = CarditColors.Gold, modifier = Modifier.pressable { vm.go(Screen.HOME) }.padding(12.dp))
        }
    }
}

@Composable
private fun NextTripCard(vm: CarditViewModel, modifier: Modifier) {
    val shape = RoundedCornerShape(40.dp)
    Column(
        modifier.background(Brush.linearGradient(listOf(CarditColors.SurfaceMid, CarditColors.SurfaceMid, Color(0xFF2A2350))), shape)
            .border(1.dp, CarditColors.GlassBorder, shape).padding(40.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("SEASON FINALE", style = CarditType.LabelMd, color = CarditColors.LavenderSoft, modifier = Modifier.weight(1f))
            repeat(3) { Icon(Icons.Rounded.Star, null, tint = CarditColors.GoldSoft, modifier = Modifier.size(26.dp)) }
        }
        Spacer(Modifier.height(20.dp))
        Text("NEXT DESTINATION TEASER", style = CarditType.LabelLg.copy(letterSpacing = CarditType.LabelLg.letterSpacing * 3), color = CarditColors.TextWarm)
        Text("TO BE\nCONTINUED...", style = CarditType.DisplayLg, color = CarditColors.TextHigh)
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth().background(CarditColors.SurfaceHigh.copy(alpha = 0.6f), RoundedCornerShape(22.dp)).padding(24.dp)) {
            Icon(Icons.Rounded.DirectionsCar, null, tint = CarditColors.LavenderSoft, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(14.dp))
            Column {
                Text("다음 여행의 오프닝으로 이어집니다", style = CarditType.BodyXl, color = CarditColors.TextHigh)
                Spacer(Modifier.height(6.dp))
                Text("차량 내 다음 목적지 내비게이션 예약 시 트립 필름 오프닝 시퀀스가 자동 연동됩니다.", style = CarditType.LabelMd, color = CarditColors.TextWarm)
            }
        }
        Spacer(Modifier.weight(1f))
        Row(
            Modifier.fillMaxWidth().background(CarditColors.Canvas.copy(alpha = 0.5f), RoundedCornerShape(22.dp)).padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text("예상 다음 여정", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                Text("강릉 바다 드라이브 🌊", style = CarditType.HeadlineLg, color = CarditColors.Gold)
            }
            Row(
                Modifier.pressable { vm.planNext("강릉 바다 드라이브", "강릉 안목해변") }
                    .background(CarditColors.LavenderSoft, RoundedCornerShape(14.dp)).padding(horizontal = 22.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("목적지\n설정", style = CarditType.LabelLg, color = Color(0xFF2E158C), textAlign = TextAlign.Center)
                Spacer(Modifier.width(10.dp))
                Icon(Icons.Rounded.ArrowForward, null, tint = Color(0xFF2E158C), modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Composable
private fun BigAction(
    title: String, sub: String, icon: ImageVector, trailing: ImageVector, gold: Boolean, modifier: Modifier,
    circleColor: Color = if (gold) CarditColors.GoldDeep else CarditColors.SurfaceHighest, onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(50)
    Row(
        modifier.pressable(onClick = onClick)
            .then(if (gold) Modifier.shadow(24.dp, shape, ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold) else Modifier)
            .background(if (gold) CarditColors.Gold else CarditColors.SurfaceHigh, shape)
            .height(108.dp).padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconCircle(icon, size = 64.dp, container = circleColor, tint = if (gold) CarditColors.Gold else CarditColors.TextHigh)
        Spacer(Modifier.width(24.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = CarditType.HeadlineMd, color = if (gold) CarditColors.OnGold else CarditColors.TextHigh)
            Text(sub, style = CarditType.LabelMd, color = if (gold) CarditColors.GoldDeep else CarditColors.TextWarm)
        }
        Icon(trailing, null, tint = if (gold) CarditColors.OnGold else CarditColors.TextWarm, modifier = Modifier.size(34.dp))
    }
}
