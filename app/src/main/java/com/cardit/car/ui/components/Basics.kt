package com.cardit.car.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

/** Pleos 인터랙션 가이드: 누르면 축소(96%) */
@Composable
fun Modifier.pressable(enabled: Boolean = true, onClick: () -> Unit): Modifier {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val s by animateFloatAsState(if (pressed) 0.96f else 1f, label = "press")
    return this
        .scale(s)
        .clickable(interactionSource = source, indication = null, enabled = enabled, onClick = onClick)
}

/** Frosted bento 카드 (28dp, 반투명 + 1px 테두리) */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    radius: Dp = 28.dp,
    fill: Color = CarditColors.Glass,
    border: Color = CarditColors.GlassBorder,
    padding: PaddingValues = PaddingValues(28.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(radius)
    Column(
        modifier
            .clip(shape)
            .background(fill, shape)
            .border(1.dp, border, shape)
            .padding(padding),
        content = content,
    )
}

/** 골드 마키 강조 테두리 (선택 상태) */
fun Modifier.marquee(selected: Boolean, radius: Dp = 28.dp): Modifier =
    if (!selected) this else this
        .shadow(28.dp, RoundedCornerShape(radius), ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold)
        .border(2.dp, CarditColors.Gold, RoundedCornerShape(radius))

@Composable
fun GoldButton(
    text: String,
    modifier: Modifier = Modifier,
    leading: ImageVector? = null,
    trailing: ImageVector? = null,
    sub: String? = null,
    height: Dp = 88.dp,
    textStyle: TextStyle = CarditType.HeadlineMd,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(percent = 50)
    Row(
        modifier
            .pressable(onClick = onClick)
            .shadow(24.dp, shape, ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold)
            .background(CarditColors.Gold, shape)
            .height(height)
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (leading != null) {
            Icon(leading, null, tint = CarditColors.OnGold, modifier = Modifier.size(34.dp))
            Spacer(Modifier.width(18.dp))
        }
        Column(Modifier.weight(1f, fill = false), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, style = textStyle, color = CarditColors.OnGold, maxLines = 2)
            if (sub != null) Text(sub, style = CarditType.LabelSm, color = CarditColors.GoldDeep, maxLines = 1)
        }
        if (trailing != null) {
            Spacer(Modifier.width(18.dp))
            Box(
                Modifier.size(56.dp).background(CarditColors.OnGold.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center,
            ) { Icon(trailing, null, tint = CarditColors.OnGold, modifier = Modifier.size(30.dp)) }
        }
    }
}

@Composable
fun DarkButton(
    text: String,
    modifier: Modifier = Modifier,
    leading: ImageVector? = null,
    trailing: ImageVector? = null,
    height: Dp = 80.dp,
    color: Color = CarditColors.SurfaceHigh,
    contentColor: Color = CarditColors.TextHigh,
    iconTint: Color = contentColor,
    textStyle: TextStyle = CarditType.HeadlineSm,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(percent = 50)
    Row(
        modifier
            .pressable(onClick = onClick)
            .background(color, shape)
            .height(height)
            .padding(horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (leading != null) {
            Icon(leading, null, tint = iconTint, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(14.dp))
        }
        Text(text, style = textStyle, color = contentColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (trailing != null) {
            Spacer(Modifier.width(14.dp))
            Icon(trailing, null, tint = iconTint, modifier = Modifier.size(28.dp))
        }
    }
}

/** 메타 칩 / 장르 태그 (pill) */
@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    container: Color = CarditColors.SurfaceHigh,
    content: Color = CarditColors.TextHigh,
    iconTint: Color = content,
    border: Color? = null,
    style: TextStyle = CarditType.LabelMd,
    dot: Color? = null,
    height: Dp = 44.dp,
    shape: Shape = RoundedCornerShape(percent = 50),
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier
            .then(if (onClick != null) Modifier.pressable(onClick = onClick) else Modifier)
            .clip(shape)
            .background(container, shape)
            .then(if (border != null) Modifier.border(1.dp, border, shape) else Modifier)
            .height(height)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (dot != null) {
            Box(Modifier.size(10.dp).background(dot, CircleShape))
            Spacer(Modifier.width(10.dp))
        }
        if (icon != null) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, style = style, color = content, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun IconCircle(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    container: Color = CarditColors.SurfaceHigh,
    tint: Color = CarditColors.TextHigh,
    iconSize: Dp = size * 0.45f,
    shape: Shape = CircleShape,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier
            .then(if (onClick != null) Modifier.pressable(onClick = onClick) else Modifier)
            .size(size)
            .background(container, shape),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, null, tint = tint, modifier = Modifier.size(iconSize)) }
}

/** 노란 세로 바 + 섹션 제목 */
@Composable
fun SectionTitle(title: String, sub: String? = null, modifier: Modifier = Modifier, trailing: @Composable RowScope.() -> Unit = {}) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(6.dp).height(30.dp).background(CarditColors.Gold, RoundedCornerShape(3.dp)))
        Spacer(Modifier.width(18.dp))
        Text(title, style = CarditType.HeadlineLg, color = CarditColors.TextHigh)
        if (sub != null) {
            Spacer(Modifier.width(16.dp))
            Text(sub, style = CarditType.LabelMd, color = CarditColors.TextMid, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(Modifier.weight(1f))
        trailing()
    }
}

/** "— EPILOGUE & CREDITS" 형태의 오버라인 */
@Composable
fun Overline(text: String, color: Color = CarditColors.Gold, modifier: Modifier = Modifier, line: Boolean = true) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        if (line) {
            Box(Modifier.width(40.dp).height(2.dp).background(color))
            Spacer(Modifier.width(16.dp))
        }
        Text(text, style = CarditType.LabelMd.copy(letterSpacing = CarditType.LabelSm.letterSpacing * 2), color = color)
    }
}

/** 화면 배경: 딥 네이비 + 상단 골드 글로우 */
fun Modifier.cinemaBackground(glow: Color = CarditColors.Gold, glowAlpha: Float = 0.10f): Modifier = this
    .background(CarditColors.Canvas)
    .background(
        Brush.radialGradient(
            colors = listOf(glow.copy(alpha = glowAlpha), Color.Transparent),
            radius = 1400f,
        ),
    )

/**
 * 앱 내부 오버레이 패널. Android Dialog 는 별도 창이라 DesignScale 의 density 를 물려받지 못하므로
 * 루트 Box 위에 직접 그린다.
 */
@Composable
fun OverlayPanel(onDismiss: () -> Unit, width: Dp = 900.dp, content: @Composable ColumnScope.() -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onDismiss),
        contentAlignment = Alignment.Center,
    ) {
        GlassCard(
            Modifier
                .width(width)
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
            radius = 32.dp, fill = CarditColors.SurfaceMid, padding = PaddingValues(40.dp), content = content,
        )
    }
}
