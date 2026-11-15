package com.cardit.car.vehicle

import org.junit.Assert.assertEquals
import org.junit.Test

class CinemaBrightnessTest {

    @Test
    fun `범위의 하위 20퍼센트로 낮춘다`() {
        assertEquals(20, cinemaBrightness(0, 100))
        assertEquals(3, cinemaBrightness(1, 10)) // 1 + 9*0.2 = 2.8 → 3
    }

    @Test
    fun `범위가 비정상이면 최소값`() {
        assertEquals(5, cinemaBrightness(5, 5))
        assertEquals(7, cinemaBrightness(7, 3))
    }
}
