package com.cardit.car.gate

import com.cardit.car.signal.UxSignal

enum class GateDecision {
    /** 주차 상태 — 재생 허용 */
    ALLOW,
    /** 주행 중 — 재생 차단 */
    BLOCK_DRIVING,
    /** 판정 불가 — fail-safe 로 재생 차단 */
    BLOCK_UNKNOWN,
}

/**
 * ② 게이트 로직. 순수 함수로 유지해 단위 테스트한다.
 * 신호 소스가 바뀌거나 보조 신호가 추가되어도 이 판정 규칙은 그대로 둔다.
 */
object PlaybackGate {
    fun decide(signal: UxSignal?): GateDecision = when {
        signal == null -> GateDecision.BLOCK_UNKNOWN
        signal.requiresDistractionOptimization || signal.noVideo -> GateDecision.BLOCK_DRIVING
        else -> GateDecision.ALLOW
    }
}
