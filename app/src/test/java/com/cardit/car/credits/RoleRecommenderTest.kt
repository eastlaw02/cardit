package com.cardit.car.credits

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoleRecommenderTest {

    @Test
    fun `각자 가장 두드러진 활동에 맞는 역할을 받는다`() {
        val recs = RoleRecommender.recommend(
            listOf(
                ParticipantActivity("a", navLogs = 9, highlights = 2, laughs = 3, foodPhotos = 4, videoMinutes = 6),
                ParticipantActivity("b", navLogs = 1, highlights = 7, laughs = 11, foodPhotos = 6, videoMinutes = 10),
                ParticipantActivity("c", highlights = 3, laughs = 5, foodPhotos = 31, videoMinutes = 8),
                ParticipantActivity("d", navLogs = 2, highlights = 4, laughs = 4, foodPhotos = 9, videoMinutes = 45),
            ),
        )
        assertEquals(listOf("길잡이", "분위기 메이커", "맛집 탐험가", "기록 담당"), recs.map { it.role })
        assertEquals("길 안내 기록 9회 감지", recs[0].reason)
    }

    @Test
    fun `역할은 겹치지 않는다`() {
        // 둘 다 길 안내가 가장 많아도 한 명만 길잡이
        val recs = RoleRecommender.recommend(
            listOf(
                ParticipantActivity("a", navLogs = 10, foodPhotos = 1),
                ParticipantActivity("b", navLogs = 8, foodPhotos = 2),
            ),
        )
        assertEquals("길잡이", recs[0].role)
        assertEquals("맛집 탐험가", recs[1].role)
    }

    @Test
    fun `활동 기록이 없으면 카메오`() {
        val recs = RoleRecommender.recommend(listOf(ParticipantActivity("a", navLogs = 3), ParticipantActivity("b")))
        assertEquals("길잡이", recs[0].role)
        assertEquals(RoleRecommender.FALLBACK_ROLE, recs[1].role)
    }

    @Test
    fun `역할보다 사람이 많으면 남는 사람은 카메오`() {
        val people = (1..6).map { ParticipantActivity("p$it", navLogs = it, highlights = it, foodPhotos = it, videoMinutes = it) }
        val recs = RoleRecommender.recommend(people)
        assertEquals(RoleRecommender.roles.toSet(), recs.map { it.role }.filter { it != RoleRecommender.FALLBACK_ROLE }.toSet())
        assertEquals(2, recs.count { it.role == RoleRecommender.FALLBACK_ROLE })
    }

    @Test
    fun `빈 목록이면 빈 결과`() {
        assertTrue(RoleRecommender.recommend(emptyList()).isEmpty())
    }
}
