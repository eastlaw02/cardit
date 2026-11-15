package com.cardit.car.credits

/** 크레딧 한 줄. [showPhoto] 는 참여자 권한이 '이름만'이면 false */
data class CreditEntry(val label: String, val value: String, val showPhoto: Boolean = false)

/**
 * 차량 앱이 재생하는 크레딧 데이터 (개발계획서 5).
 * 시연판과 정식판은 같은 데이터에서 [durationMs] 만 다르다.
 */
data class CreditSheet(
    val title: String,
    val location: String,
    val genre: String,
    val bgm: String,
    val cast: List<CreditEntry>,
    /** 차량이 지원하지 않는 통계는 빠져 있다 */
    val stats: List<CreditEntry>,
    /** 오프닝 → 여행 장면 → 크레딧 → THE END → 쿠키 → TO BE CONTINUED (사용자가 바꾼 순서) */
    val sequence: List<String>,
    val durationMs: Long,
) {
    fun toJson(): String = buildString {
        append('{')
        field("title", title); append(',')
        field("location", location); append(',')
        field("genre", genre); append(',')
        field("bgm", bgm); append(',')
        append("\"cast\":").append(cast.joinToString(",", "[", "]") { it.json() }).append(',')
        append("\"stats\":").append(stats.joinToString(",", "[", "]") { it.json() }).append(',')
        append("\"sequence\":").append(sequence.joinToString(",", "[", "]") { quote(it) }).append(',')
        append("\"durationMs\":").append(durationMs)
        append('}')
    }

    private fun CreditEntry.json() = "{\"label\":${quote(label)},\"value\":${quote(value)},\"showPhoto\":$showPhoto}"

    private fun StringBuilder.field(name: String, value: String) {
        append(quote(name)).append(':').append(quote(value))
    }
}

/** 크레딧에 들어갈 참여자 정보 */
data class CastMember(val name: String, val role: String, val nameOnly: Boolean)

/** 여행 통계 (null = 차량 미지원 → 크레딧에서 제외) */
data class TripNumbers(val distanceKm: Int?, val rainMinutes: Int?, val batteryUsedPct: Int?)

object CreditBuilder {

    fun build(
        title: String,
        location: String,
        genre: String,
        bgm: String,
        cast: List<CastMember>,
        numbers: TripNumbers,
        sequence: List<String>,
        durationMs: Long,
    ): CreditSheet = CreditSheet(
        title = title,
        location = location,
        genre = genre,
        bgm = bgm,
        cast = cast.map { CreditEntry(it.role, it.name, showPhoto = !it.nameOnly) },
        stats = listOfNotNull(
            numbers.distanceKm?.let { CreditEntry("총 주행거리", "$it km") },
            numbers.rainMinutes?.takeIf { it > 0 }?.let { CreditEntry("빗속 주행", "${it}분") },
            numbers.batteryUsedPct?.let { CreditEntry("사용한 배터리", "$it%") },
        ),
        sequence = sequence,
        durationMs = durationMs,
    )
}

internal fun quote(s: String): String = buildString {
    append('"')
    for (c in s) {
        when (c) {
            '"' -> append("\\\"")
            '\\' -> append("\\\\")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> if (c < ' ') append("\\u%04x".format(c.code)) else append(c)
        }
    }
    append('"')
}
