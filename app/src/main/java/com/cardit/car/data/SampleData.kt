package com.cardit.car.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.RamenDining
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.SentimentVerySatisfied
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.material.icons.rounded.MovieCreation
import androidx.compose.ui.graphics.Color
import com.cardit.car.R
import com.cardit.car.credits.ParticipantActivity
import com.cardit.car.ui.theme.CarditColors

/**
 * 1단계 프로토타입용 시연 데이터 (Stitch 시안 기준).
 * 2단계에서 Firebase(Firestore/Storage) 데이터로 교체한다.
 */
object SampleData {

    val participants = listOf(
        Participant(
            "minji", "민지", "MINJI", "운전자 (호스트)", listOf(Color(0xFFF5C518), Color(0xFFFF8A5B)),
            R.drawable.img_cast_minji, "길잡이", Icons.Rounded.Explore, Icons.Rounded.Verified,
            "NAV 88%", "길 안내 기록 9회 감지", Icons.Rounded.Route, Permission.MEMBER,
        ),
        Participant(
            "junho", "준호", "JUNHO", "조수석 연결됨", listOf(Color(0xFF5B7CFF), Color(0xFF9B8CFF)),
            R.drawable.img_cast_junho, "분위기 메이커", Icons.Rounded.SentimentVerySatisfied, Icons.Rounded.Celebration,
            "VOICE 94%", "하이라이트 7회 · 큰 웃음 11회", Icons.Rounded.VolumeUp, Permission.MEMBER,
        ),
        Participant(
            "seoyeon", "서연", "SEOYEON", "2열 좌측", listOf(Color(0xFFFF5C8A), Color(0xFFFF8A5B)),
            R.drawable.img_cast_seoyeon, "맛집 탐험가", Icons.Rounded.RamenDining, Icons.Rounded.Restaurant,
            "PHOTO 91%", "식당 및 카페 사진 31장 캡처", Icons.Rounded.PhotoCamera, Permission.GUEST,
        ),
        Participant(
            "taeyoon", "태윤", "TAEYOON", "2열 우측", listOf(Color(0xFF14B8A6), Color(0xFF4ADE80)),
            R.drawable.img_cast_taeyoon, "기록 담당", Icons.Rounded.Description, Icons.Rounded.Videocam,
            "CAM 97%", "비디오 촬영 45분 클립 수록", Icons.Rounded.MovieCreation, Permission.MEMBER,
        ),
    )

    /** 참여자별 활동 기록 — 역할 추천 입력 (2단계에서 Firestore 집계값으로 교체) */
    val activities = listOf(
        ParticipantActivity("minji", navLogs = 9, highlights = 2, laughs = 3, foodPhotos = 4, videoMinutes = 6),
        ParticipantActivity("junho", navLogs = 1, highlights = 7, laughs = 11, foodPhotos = 6, videoMinutes = 10),
        ParticipantActivity("seoyeon", highlights = 3, laughs = 5, foodPhotos = 31, videoMinutes = 8),
        ParticipantActivity("taeyoon", navLogs = 2, highlights = 4, laughs = 4, foodPhotos = 9, videoMinutes = 45),
    )

    val altRoles = listOf("감독", "조연출", "음악 감독", "스턴트", "카메오")

    val chapters = listOf(
        Chapter("ch1", "오프닝", "출발 영상", 30, R.drawable.img_ch_opening),
        Chapter("ch2", "여행 장면", "해안도로 드라이브", 70, R.drawable.img_ch_scenes),
        Chapter("ch3", "크레딧", "친구들 롤링 크레딧", 75, R.drawable.img_ch_credit),
        Chapter("ch4", "THE END", "석양 장면", 20, R.drawable.img_ch_end),
        Chapter("ch5", "쿠키 영상", "비하인드 NG 컷", 30, R.drawable.img_ch_cookie, cookie = true),
        Chapter("ch6", "TO BE CONTINUED", "다음 여행 예고", 15, R.drawable.img_ch_continued),
    )

    val highlights = listOf(
        Highlight("h1", "차 안 웃음 폭발", "00:42", "웃음 데시벨 92dB", CarditColors.GoldSoft, Icons.Rounded.GraphicEq, R.drawable.img_hl_laugh),
        Highlight("h2", "광안대교 진입", "01:15", "속도감 하이라이트", CarditColors.LavenderSoft, Icons.Rounded.Speed, R.drawable.img_hl_gwangan),
        Highlight("h3", "해질녘 바닷가", "02:30", "골든아워 시네마", CarditColors.CoralSoft, Icons.Rounded.WbTwilight, R.drawable.img_hl_sunset),
    )

    val pastTrips = listOf(
        PastTrip("제주 바람길 드라이브", "함덕 서우봉부터 성산일출봉 해안선까지", "2024. 04. 12", "로맨틱 힐링", CarditColors.Lavender, "03:48", R.drawable.img_past_jeju),
        PastTrip("가평 별빛 차박 캠핑", "모닥불 소리와 은하수 아래의 하룻밤", "2024. 02. 18", "감성 다큐", CarditColors.Coral, "04:15", R.drawable.img_past_gapyeong),
    )

    val episodes = listOf(
        Episode("EP.01", "2023 WINTER", "강릉 바다로드", "헌화로 파도 드라이브와 겨울 커피", "03:45", 1, R.drawable.img_ep_gangneung),
        Episode("EP.02", "2023 AUTUMN", "여수 밤바다 투어", "낭만포차와 돌산대교 야경 크루즈", "04:12", 2, R.drawable.img_ep_yeosu),
        Episode("EP.03", "2023 SPRING", "경주 시간여행", "황리단길 벚꽃 터널과 첨성대 야경", "03:20", 1, R.drawable.img_ep_gyeongju),
        Episode("EP.04", "2022 SUMMER", "속초 해안도로", "영금정 일출부터 대포항 활어 야시장", "05:01", 3, R.drawable.img_ep_sokcho),
    )

    /** 차량 신호를 읽지 못했을 때 사용하는 시연값 */
    val demoStats = TripStats(
        distanceKm = 312, routeLabel = "울산 (출발)  해운대 경유  울산 (복귀)",
        rainMinutes = 42, wiperCount = 760,
        batteryUsedPct = 64, batteryLeftPct = 36, efficiency = "5.8 km/kWh",
        mediaCount = 286, builtinCam = 194, phoneCount = 92,
        highlights = 18, laughs = 23, peakDb = 86,
    )

    val laughWave = listOf(0.25f, 0.6f, 0.45f, 0.75f, 0.55f, 0.2f, 0.65f, 0.4f, 0.2f, 0.12f)
    val voiceWave = listOf(0.35f, 0.7f, 0.45f, 0.85f, 0.6f, 1f, 0.7f, 0.5f, 0.3f, 0.55f, 0.4f, 0.65f, 0.3f, 0.2f, 0.15f, 0.12f)
}
