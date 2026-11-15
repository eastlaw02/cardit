package com.cardit.car.ui.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import kotlin.math.min

/**
 * Stitch 시안은 1600×1280 캔버스 기준으로 그려져 있다.
 * 차량 디스플레이 크기(Connect-S/W, 1:1 분할 창)에 관계없이 시안 비율을 유지하도록
 * 앱 창 크기에 맞춰 density 를 다시 계산한다. 가로로 더 넓은 창에서는 레이아웃이 가로로 늘어난다.
 */
@Composable
fun DesignScale(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    BoxWithConstraints(modifier) {
        val base = LocalDensity.current
        val w = maxWidth.value
        val h = maxHeight.value
        val landscape = w >= h
        val designW = if (landscape) 1600f else 900f
        val designH = if (landscape) 1280f else 1600f
        val factor = min(w / designW, h / designH).coerceIn(0.35f, 2.5f)
        CompositionLocalProvider(LocalDensity provides Density(base.density * factor, base.fontScale)) {
            content()
        }
    }
}
