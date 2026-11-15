@file:OptIn(ExperimentalTextApi::class)

package com.cardit.car.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.cardit.car.R

/** Stitch "Cinematic Cockpit" 디자인 시스템 색상 토큰 (design/DESIGN.md). */
object CarditColors {
    val Canvas = Color(0xFF0B0D12)
    val Surface = Color(0xFF111318)
    val SurfaceLow = Color(0xFF1A1B21)
    val SurfaceMid = Color(0xFF1E1F25)
    val SurfaceHigh = Color(0xFF282A2F)
    val SurfaceHighest = Color(0xFF33353A)

    val Gold = Color(0xFFF5C518)
    val GoldSoft = Color(0xFFFFE5A0)
    val OnGold = Color(0xFF3D2F00)
    val GoldDeep = Color(0xFF695200)

    val Lavender = Color(0xFF9B8CFF)
    val LavenderSoft = Color(0xFFC8BFFF)
    val Coral = Color(0xFFFF8A5B)
    val CoralSoft = Color(0xFFFFBBA2)
    val Crimson = Color(0xFFFF3B3B)
    val Success = Color(0xFF4ADE80)
    val Rec = Color(0xFFFF8F8F)

    val TextHigh = Color.White
    val TextMid = Color.White.copy(alpha = 0.60f)
    val TextLow = Color.White.copy(alpha = 0.38f)
    val TextWarm = Color(0xFFD1C5AC)

    val Glass = Color.White.copy(alpha = 0.06f)
    val GlassHigh = Color.White.copy(alpha = 0.10f)
    val GlassBorder = Color.White.copy(alpha = 0.10f)
}

private fun sg(w: Int) = Font(R.font.space_grotesk, FontWeight(w), variationSettings = FontVariation.Settings(FontVariation.weight(w)))
private fun pj(w: Int) = Font(R.font.plus_jakarta_sans, FontWeight(w), variationSettings = FontVariation.Settings(FontVariation.weight(w)))

/** 헤드라인·라벨: Space Grotesk / 본문: Plus Jakarta Sans. 한글은 시스템 폰트로 대체 렌더링된다. */
val SpaceGrotesk = FontFamily(sg(400), sg(500), sg(600), sg(700))
val PlusJakarta = FontFamily(pj(400), pj(500), pj(600), pj(700), pj(800))

object CarditType {
    val DisplayXl = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 64.sp, lineHeight = 72.sp, letterSpacing = (-0.02).em)
    val DisplayLg = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 48.sp, lineHeight = 56.sp, letterSpacing = (-0.02).em)
    val DisplayMd = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 40.sp, lineHeight = 48.sp, letterSpacing = (-0.01).em)
    val HeadlineLg = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = (-0.01).em)
    val HeadlineMd = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 26.sp, lineHeight = 34.sp)
    val HeadlineSm = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp)
    val BodyXl = TextStyle(fontFamily = PlusJakarta, fontWeight = FontWeight.Medium, fontSize = 20.sp, lineHeight = 28.sp)
    val BodyLg = TextStyle(fontFamily = PlusJakarta, fontWeight = FontWeight.Normal, fontSize = 18.sp, lineHeight = 26.sp)
    val BodyMd = TextStyle(fontFamily = PlusJakarta, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp)
    val LabelLg = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 24.sp, letterSpacing = 0.04.em)
    val LabelMd = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp, letterSpacing = 0.06.em)
    val LabelSm = TextStyle(fontFamily = SpaceGrotesk, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.08.em)
}

@Composable
fun CarditTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = CarditColors.Gold,
            onPrimary = CarditColors.OnGold,
            background = CarditColors.Canvas,
            surface = CarditColors.Surface,
            onSurface = CarditColors.TextHigh,
            secondary = CarditColors.Lavender,
            tertiary = CarditColors.Coral,
        ),
        content = content,
    )
}
