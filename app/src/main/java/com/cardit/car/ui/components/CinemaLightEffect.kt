package com.cardit.car.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType
import com.cardit.car.vehicle.CinemaLight

/**
 * 차량 조명을 제어할 수 없을 때의 대체 연출 — 화면 가장자리에 은은하게 숨 쉬는 앰비언트 빛.
 * 터치를 가로채지 않도록 그리기만 한다.
 */
@Composable
fun ScreenAmbientGlow(modifier: Modifier = Modifier) {
    val pulse by rememberInfiniteTransition(label = "ambient").animateFloat(
        initialValue = 0.10f, targetValue = 0.22f,
        animationSpec = infiniteRepeatable(tween(2_400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse",
    )
    Box(
        modifier.fillMaxSize().drawBehind {
            val edge = size.minDimension * 0.10f
            val c = CarditColors.Gold.copy(alpha = pulse)
            drawRect(Brush.verticalGradient(listOf(c, Color.Transparent), endY = edge), size = Size(size.width, edge))
            drawRect(
                Brush.verticalGradient(listOf(Color.Transparent, c), startY = size.height - edge, endY = size.height),
                topLeft = Offset(0f, size.height - edge), size = Size(size.width, edge),
            )
            drawRect(Brush.horizontalGradient(listOf(c, Color.Transparent), endX = edge), size = Size(edge, size.height))
            drawRect(
                Brush.horizontalGradient(listOf(Color.Transparent, c), startX = size.width - edge, endX = size.width),
                topLeft = Offset(size.width - edge, 0f), size = Size(edge, size.height),
            )
        },
    )
}

/** 재생 화면 상단 조명 상태 칩 */
@Composable
fun CinemaLightBadge(state: CinemaLight) {
    val text = when (state) {
        CinemaLight.VEHICLE -> "실내 무드 조명 시네마 모드"
        CinemaLight.SCREEN -> "화면 앰비언트 효과 (조명 대체)"
        CinemaLight.OFF -> "조명 원래 밝기"
    }
    Pill(
        text, icon = Icons.Rounded.Lightbulb,
        container = CarditColors.SurfaceMid,
        content = if (state == CinemaLight.OFF) CarditColors.TextWarm else CarditColors.TextHigh,
        iconTint = if (state == CinemaLight.OFF) CarditColors.TextWarm else CarditColors.Gold,
        height = 52.dp, style = CarditType.LabelLg,
    )
}
