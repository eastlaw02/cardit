package com.cardit.car.vehicle

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RainAccumulatorTest {

    private val minute = 60_000L

    /** 분 단위 간격 테스트에서는 간격 제한을 넉넉히 둔다 */
    private fun wideGap() = RainAccumulator(maxGapMs = 10 * minute)

    @Test
    fun `샘플이 하나도 지원되지 않으면 null`() {
        val acc = RainAccumulator()
        acc.add(0, null)
        acc.add(10_000, null)
        assertNull(acc.minutes())
    }

    @Test
    fun `와이퍼가 꺼져 있으면 0분`() {
        val acc = RainAccumulator()
        acc.add(0, false)
        acc.add(10_000, false)
        assertEquals(0, acc.minutes())
    }

    @Test
    fun `ON에서 OFF로 바뀌는 구간은 ON이었던 시간만큼 더한다`() {
        val acc = wideGap()
        acc.add(0, true)
        acc.add(minute, false)  // 0~1분 동안 ON 이었음
        assertEquals(1, acc.minutes())
    }

    @Test
    fun `OFF에서 ON으로 바뀌는 구간은 더하지 않는다`() {
        val acc = wideGap()
        acc.add(0, false)
        acc.add(2 * minute, true)  // 0~2분 사이 전환 → 제외
        acc.add(3 * minute, true)  // 2~3분 ON → 1분
        assertEquals(1, acc.minutes())
    }

    @Test
    fun `1분 이상 ON 이면 분 단위로 내림`() {
        val acc = RainAccumulator()
        var t = 0L
        acc.add(t, true)
        repeat(7) { t += 10_000; acc.add(t, true) } // 70초
        assertEquals(1, acc.minutes())
    }

    @Test
    fun `샘플 간격이 최대값을 넘으면 그 구간은 제외`() {
        val acc = RainAccumulator(maxGapMs = 30_000)
        acc.add(0, true)
        acc.add(120_000, true) // 2분 공백 → 제외
        assertEquals(0, acc.minutes())
    }

    @Test
    fun `미지원 샘플은 이전 구간을 잇지 않는다`() {
        val acc = RainAccumulator()
        acc.add(0, true)
        acc.add(10_000, null)
        acc.add(20_000, true) // 직전 샘플이 null 이므로 누적 없음
        assertEquals(0, acc.minutes())
    }

    @Test
    fun `여러 ON 구간을 합산한다`() {
        val acc = wideGap()
        acc.add(0, true)
        acc.add(minute, true)        // 0~1분 ON: 60초
        acc.add(2 * minute, false)   // 1~2분 ON→OFF: 60초
        acc.add(4 * minute, true)    // 2~4분 OFF→ON: 제외
        acc.add(5 * minute, true)    // 4~5분 ON: 60초
        assertEquals(3, acc.minutes())
    }
}
