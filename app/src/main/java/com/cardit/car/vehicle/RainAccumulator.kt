package com.cardit.car.vehicle

/**
 * 와이퍼 작동 구간을 누적해 강우 시간(분)을 계산한다.
 *
 * - 연속한 두 샘플 사이에 와이퍼가 ON 이었던 시간만 더한다.
 * - 샘플 간격이 [maxGapMs] 를 넘으면 앱이 떠 있지 않았던 구간으로 보고 더하지 않는다.
 * - 미지원(null) 샘플이 한 번도 없었다면 [minutes] 는 null → 크레딧에서 제외.
 */
class RainAccumulator(private val maxGapMs: Long = DEFAULT_MAX_GAP_MS) {

    private var lastAtMs: Long? = null
    private var lastWiperOn: Boolean? = null
    private var accumulatedMs = 0L
    private var sawSupported = false

    fun add(takenAtMs: Long, wiperOn: Boolean?) {
        if (wiperOn == null) {
            // 미지원/읽기 실패 샘플: 이전 구간과 잇지 않는다
            lastAtMs = null
            lastWiperOn = null
            return
        }
        sawSupported = true
        val prevAt = lastAtMs
        if (prevAt != null && lastWiperOn == true) {
            val gap = takenAtMs - prevAt
            if (gap in 0..maxGapMs) accumulatedMs += gap
        }
        lastAtMs = takenAtMs
        lastWiperOn = wiperOn
    }

    fun minutes(): Int? = if (sawSupported) (accumulatedMs / 60_000L).toInt() else null

    companion object {
        /** 기본 샘플 주기(10초)의 3배를 넘는 간격은 끊김으로 본다 */
        const val DEFAULT_MAX_GAP_MS = 30_000L
    }
}
