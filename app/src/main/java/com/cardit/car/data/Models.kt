package com.cardit.car.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.cardit.car.R
import com.cardit.car.ui.theme.CarditColors

enum class Genre(
    val label: String,
    val subLabel: String,
    val mood: String,
    val tagline: String,
    val description: String,
    val badge: String,
    val accent: Color,
    @DrawableRes val image: Int,
    val bgm: String,
    val bpm: Int,
) {
    COMEDY(
        "코미디", "COMEDY · 로드무비", "BRIGHT & VIBRANT", "통통 튀는 전환 · 경쾌한 BGM",
        "함께 웃고 떠들던 순간들을 유쾌한 리듬과 따스한 감성 필터로 엮어냅니다.",
        "PLEOS SELECTION 2026", CarditColors.Coral, R.drawable.img_genre_comedy, "Sunset Funk Beat", 128,
    ),
    ACTION(
        "액션", "ACTION · 하이웨이", "FAST & INTENSE", "빠른 컷 · 강렬한 비트",
        "주행 데이터에 맞춰 긴장감 넘치는 컷 전환을 선사합니다.",
        "DYNAMIC DRIVE PACING", CarditColors.Crimson, R.drawable.img_genre_action, "Night Highway Pulse", 150,
    ),
    EMOTIONAL(
        "감성", "EMOTIONAL · 아르누보", "POETIC & CALM", "느린 페이드 · 잔잔한 피아노",
        "노을 진 창밖 풍경과 정적인 휴식의 여운을 부드러운 디졸브로 완성합니다.",
        "POETIC NOSTALGIA", CarditColors.Lavender, R.drawable.img_genre_emotional, "Golden Hour Piano", 72,
    ),
}

/** 참여자 권한 3단계 (개발계획서 4) */
enum class Permission(val label: String) { NAME_ONLY("이름만"), GUEST("게스트"), MEMBER("멤버") }

data class Participant(
    val id: String,
    val name: String,
    val nameEn: String,
    val seat: String,
    val avatar: List<Color>,
    @DrawableRes val photo: Int,
    val role: String,
    val roleIcon: ImageVector,
    val badgeIcon: ImageVector,
    val metric: String,
    val reason: String,
    val reasonIcon: ImageVector,
    val permission: Permission,
)

data class Chapter(
    val id: String,
    val title: String,
    val sub: String,
    val durationSec: Int,
    @DrawableRes val image: Int,
    val cookie: Boolean = false,
)

data class Highlight(
    val id: String,
    val title: String,
    val time: String,
    val meta: String,
    val metaColor: Color,
    val icon: ImageVector,
    @DrawableRes val image: Int,
)

data class PastTrip(
    val title: String,
    val desc: String,
    val date: String,
    val tag: String,
    val tagColor: Color,
    val runtime: String,
    @DrawableRes val image: Int,
)

data class Episode(
    val ep: String,
    val season: String,
    val title: String,
    val desc: String,
    val runtime: String,
    val cookies: Int,
    @DrawableRes val image: Int,
)

/** 여행 데이터 집계 결과. 차량이 지원하지 않는 항목은 null → 크레딧/요약에서 제외 */
data class TripStats(
    val distanceKm: Int?,
    val routeLabel: String,
    val rainMinutes: Int?,
    val wiperCount: Int?,
    val batteryUsedPct: Int?,
    val batteryLeftPct: Int?,
    val efficiency: String?,
    val mediaCount: Int,
    val builtinCam: Int,
    val phoneCount: Int,
    val highlights: Int,
    val laughs: Int,
    val peakDb: Int,
)
