package com.cardit.car.gate

import com.cardit.car.signal.UxSignal
import org.junit.Assert.assertEquals
import org.junit.Test

class PlaybackGateTest {

    @Test
    fun `신호가 없으면 fail-safe 로 차단`() {
        assertEquals(GateDecision.BLOCK_UNKNOWN, PlaybackGate.decide(null))
    }

    @Test
    fun `주차 상태면 허용`() {
        assertEquals(GateDecision.ALLOW, PlaybackGate.decide(UxSignal(false, false)))
    }

    @Test
    fun `주의분산 최적화 요구 시 차단`() {
        assertEquals(GateDecision.BLOCK_DRIVING, PlaybackGate.decide(UxSignal(true, false)))
    }

    @Test
    fun `NO_VIDEO 제한만 있어도 차단`() {
        assertEquals(GateDecision.BLOCK_DRIVING, PlaybackGate.decide(UxSignal(false, true)))
    }
}
