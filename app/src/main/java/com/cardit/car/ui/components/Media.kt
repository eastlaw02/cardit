package com.cardit.car.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

/** 시네마틱 커버 이미지 + 하단 그라데이션 스크림 */
@Composable
fun CoverImage(
    @DrawableRes res: Int,
    modifier: Modifier = Modifier,
    radius: Dp = 28.dp,
    scrim: Float = 0.85f,
    scrimFromTop: Boolean = false,
    alpha: Float = 1f,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(modifier.clip(RoundedCornerShape(radius))) {
        Image(
            painterResource(res), null,
            contentScale = ContentScale.Crop,
            alpha = alpha,
            modifier = Modifier.fillMaxSize(),
        )
        if (scrim > 0f) {
            val stops = listOf(Color.Transparent, CarditColors.Canvas.copy(alpha = scrim))
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(if (scrimFromTop) stops.reversed() else stops, startY = if (scrimFromTop) 0f else 120f),
                ),
            )
        }
        content()
    }
}

/** 웃음/음성 파형 막대 */
@Composable
fun Waveform(
    levels: List<Float>,
    modifier: Modifier = Modifier,
    barWidth: Dp = 12.dp,
    gap: Dp = 8.dp,
    maxHeight: Dp = 64.dp,
    colors: List<Color> = listOf(CarditColors.Gold, CarditColors.CoralSoft, CarditColors.GoldSoft),
    activeCount: Int = levels.size,
) {
    Row(modifier.height(maxHeight), horizontalArrangement = Arrangement.spacedBy(gap), verticalAlignment = Alignment.CenterVertically) {
        levels.forEachIndexed { i, l ->
            val c = if (i < activeCount) colors[i % colors.size] else CarditColors.SurfaceHighest
            Box(Modifier.width(barWidth).height(maxHeight * l.coerceIn(0.12f, 1f)).background(c, RoundedCornerShape(50)))
        }
    }
}

/** 미디어 스크러버 (골드 진행 + 원형 썸). onSeek 은 0..1 */
@Composable
fun Scrubber(
    progress: Float,
    modifier: Modifier = Modifier,
    track: Dp = 10.dp,
    thumb: Dp = 26.dp,
    onSeek: ((Float) -> Unit)? = null,
) {
    val p = progress.coerceIn(0f, 1f)
    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier
            .height(maxOf(thumb, track))
            .then(
                if (onSeek != null) Modifier.pointerInput(Unit) {
                    detectTapGestures { o -> onSeek((o.x / size.width).coerceIn(0f, 1f)) }
                } else Modifier,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(Modifier.fillMaxWidth().height(track).background(CarditColors.SurfaceHighest, RoundedCornerShape(50)))
        Box(
            Modifier.fillMaxWidth(p).height(track)
                .shadow(12.dp, RoundedCornerShape(50), ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold)
                .background(CarditColors.Gold, RoundedCornerShape(50)),
        )
        val x = (maxWidth - thumb) * p
        if (thumb > 0.dp) Box(
            Modifier.offset(x = x).size(thumb)
                .background(CarditColors.GoldSoft, CircleShape)
                .border(4.dp, CarditColors.Gold, CircleShape),
        )
    }
}

/** 탑승자 아바타 스택 */
@Composable
fun AvatarStack(count: Int, extra: Int, modifier: Modifier = Modifier, size: Dp = 60.dp) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(-(size / 5))) {
        repeat(count) {
            Box(
                Modifier.size(size).background(CarditColors.SurfaceHighest, CircleShape).border(3.dp, CarditColors.SurfaceMid, CircleShape),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Rounded.Person, null, tint = CarditColors.GoldSoft, modifier = Modifier.size(size * 0.45f)) }
        }
        if (extra > 0) {
            Box(Modifier.size(size).background(CarditColors.Gold, CircleShape), contentAlignment = Alignment.Center) {
                Text("+$extra", style = CarditType.LabelLg, color = CarditColors.OnGold)
            }
        }
    }
}

/** 이니셜 아바타 (새 여행 화면) */
@Composable
fun InitialAvatar(initial: String, colors: List<Color>, modifier: Modifier = Modifier, size: Dp = 64.dp, online: Boolean = false) {
    Box(modifier.size(size)) {
        Box(
            Modifier.fillMaxSize().background(Brush.linearGradient(colors), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center,
        ) { Text(initial, style = CarditType.HeadlineMd, color = Color.White) }
        if (online) {
            Box(
                Modifier.align(Alignment.BottomEnd).offset(4.dp, 4.dp).size(18.dp)
                    .background(CarditColors.Success, CircleShape).border(3.dp, CarditColors.SurfaceMid, CircleShape),
            )
        }
    }
}

/** 세로 구분 칸 */
@Composable
fun VDivider(modifier: Modifier = Modifier, color: Color = CarditColors.GlassBorder) {
    Box(modifier.width(1.dp).fillMaxHeight().background(color))
}
