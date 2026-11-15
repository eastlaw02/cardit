package com.cardit.car.credits

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CreditBuilderTest {

    private fun build(numbers: TripNumbers, cast: List<CastMember> = listOf(CastMember("민지", "길잡이", nameOnly = false))) =
        CreditBuilder.build(
            title = "부산 우정여행", location = "부산 해운대", genre = "코미디", bgm = "Sunset Funk Beat",
            cast = cast, numbers = numbers,
            sequence = listOf("오프닝", "여행 장면", "크레딧", "THE END", "쿠키 영상", "TO BE CONTINUED"),
            durationMs = 60_000,
        )

    @Test
    fun `지원되는 통계만 크레딧에 들어간다`() {
        val sheet = build(TripNumbers(distanceKm = 312, rainMinutes = null, batteryUsedPct = 64))
        assertEquals(listOf("총 주행거리", "사용한 배터리"), sheet.stats.map { it.label })
        assertEquals("312 km", sheet.stats[0].value)
    }

    @Test
    fun `비가 오지 않았으면 빗속 주행은 빠진다`() {
        val sheet = build(TripNumbers(distanceKm = null, rainMinutes = 0, batteryUsedPct = null))
        assertTrue(sheet.stats.isEmpty())
    }

    @Test
    fun `이름만 권한이면 사진을 쓰지 않는다`() {
        val sheet = build(
            TripNumbers(null, null, null),
            cast = listOf(CastMember("민지", "길잡이", nameOnly = false), CastMember("서연", "맛집 탐험가", nameOnly = true)),
        )
        assertTrue(sheet.cast[0].showPhoto)
        assertFalse(sheet.cast[1].showPhoto)
        assertEquals("맛집 탐험가", sheet.cast[1].label)
        assertEquals("서연", sheet.cast[1].value)
    }

    @Test
    fun `JSON 은 따옴표와 줄바꿈을 이스케이프한다`() {
        val sheet = build(TripNumbers(10, 5, 3)).copy(title = "\"우리\"의\n여행")
        val json = sheet.toJson()
        assertTrue(json.startsWith("{\"title\":\"\\\"우리\\\"의\\n여행\""))
        assertTrue(json.contains("\"stats\":[{\"label\":\"총 주행거리\",\"value\":\"10 km\",\"showPhoto\":false}"))
        assertTrue(json.contains("\"sequence\":[\"오프닝\",\"여행 장면\""))
        assertTrue(json.endsWith("\"durationMs\":60000}"))
    }
}
