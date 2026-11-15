package com.cardit.car.credits

/**
 * 여행 중 참여자별 활동 기록 (모바일 웹앱에서 집계해 내려받는 값).
 * 1단계에서는 시연 데이터, 2단계에서 Firestore 집계값으로 교체.
 */
data class ParticipantActivity(
    val participantId: String,
    /** 길 안내 · 경로 관련 기록 수 */
    val navLogs: Int = 0,
    /** 직접 지정한 하이라이트 수 */
    val highlights: Int = 0,
    /** 차 안 큰 웃음 감지 수 (2단계 음성 분석) */
    val laughs: Int = 0,
    /** 식당 · 카페 사진 수 */
    val foodPhotos: Int = 0,
    /** 촬영한 영상 길이(분) */
    val videoMinutes: Int = 0,
)

data class RoleRecommendation(val role: String, val reason: String)

/**
 * 규칙 기반 역할 추천 (개발계획서 4).
 *
 * 1. 역할마다 점수 규칙을 두고, 참여자 점수를 그 역할의 최고점으로 나눠 0~1로 맞춘다.
 * 2. 정규화 점수가 높은 (참여자, 역할) 쌍부터 겹치지 않게 배정한다.
 * 3. 점수가 0인 참여자는 [FALLBACK_ROLE]로 둔다. 사용자는 화면에서 직접 바꿀 수 있다.
 */
object RoleRecommender {

    const val FALLBACK_ROLE = "카메오"

    private class Rule(val role: String, val score: (ParticipantActivity) -> Int, val reason: (ParticipantActivity) -> String)

    private val rules = listOf(
        Rule("길잡이", { it.navLogs }) { "길 안내 기록 ${it.navLogs}회 감지" },
        Rule("분위기 메이커", { it.highlights * 2 + it.laughs }) { "하이라이트 ${it.highlights}회 · 큰 웃음 ${it.laughs}회" },
        Rule("맛집 탐험가", { it.foodPhotos }) { "식당 및 카페 사진 ${it.foodPhotos}장 캡처" },
        Rule("기록 담당", { it.videoMinutes }) { "비디오 촬영 ${it.videoMinutes}분 클립 수록" },
    )

    val roles: List<String> get() = rules.map { it.role }

    fun recommend(activities: List<ParticipantActivity>): List<RoleRecommendation> {
        val maxByRule = rules.map { rule -> activities.maxOfOrNull(rule.score) ?: 0 }

        data class Candidate(val person: Int, val rule: Int, val normalized: Double)

        val candidates = buildList {
            activities.forEachIndexed { p, act ->
                rules.forEachIndexed { r, rule ->
                    val max = maxByRule[r]
                    val s = rule.score(act)
                    if (max > 0 && s > 0) add(Candidate(p, r, s.toDouble() / max))
                }
            }
        }.sortedWith(compareByDescending<Candidate> { it.normalized }.thenBy { it.person }.thenBy { it.rule })

        val result = arrayOfNulls<RoleRecommendation>(activities.size)
        val usedRules = mutableSetOf<Int>()
        for (c in candidates) {
            if (result[c.person] != null || c.rule in usedRules) continue
            val rule = rules[c.rule]
            result[c.person] = RoleRecommendation(rule.role, rule.reason(activities[c.person]))
            usedRules += c.rule
        }
        return result.map { it ?: RoleRecommendation(FALLBACK_ROLE, "여행 내내 함께한 출연진") }
    }
}
