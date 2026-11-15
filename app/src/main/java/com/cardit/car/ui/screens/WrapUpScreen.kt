package com.cardit.car.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.ElectricCar
import androidx.compose.material.icons.rounded.MovieCreation
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.SentimentVerySatisfied
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Thunderstorm
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cardit.car.CarditViewModel
import com.cardit.car.R
import com.cardit.car.data.SampleData
import com.cardit.car.ui.components.CoverImage
import com.cardit.car.ui.components.DarkButton
import com.cardit.car.ui.components.GlassCard
import com.cardit.car.ui.components.GoldButton
import com.cardit.car.ui.components.IconCircle
import com.cardit.car.ui.components.Pill
import com.cardit.car.ui.components.RailScaffold
import com.cardit.car.ui.components.Waveform
import com.cardit.car.Screen
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

@Composable
fun WrapUpScreen(vm: CarditViewModel) {
    val s = vm.stats
    RailScaffold(Screen.WRAP_UP, vm::go, { vm.devPanelOpen = true }, vm.tripActive, scrollable = true) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Pill("FESTIVAL PREMIERE WRAP", icon = Icons.Rounded.MovieCreation, container = CarditColors.Gold.copy(alpha = 0.18f), content = CarditColors.Gold, height = 40.dp)
            Spacer(Modifier.width(18.dp))
            Text("ROLL #04 · SCENE COMPLETE", style = CarditType.LabelMd, color = CarditColors.TextWarm)
        }
        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(Icons.Rounded.MovieCreation, size = 84.dp, container = CarditColors.SurfaceHigh, tint = CarditColors.Gold, shape = RoundedCornerShape(20.dp))
            Spacer(Modifier.width(24.dp))
            Column(Modifier.weight(1f)) {
                Text("${vm.tripTitle}, 촬영 종료", style = CarditType.DisplayLg, color = CarditColors.TextHigh)
                Row {
                    Text("2026.11.22 19:42 KST", style = CarditType.LabelLg, color = CarditColors.GoldSoft)
                    Text("  •  차량 파킹 감지됨 (Lounge Mode On)", style = CarditType.BodyLg, color = CarditColors.TextWarm)
                }
            }
            Row(
                Modifier.background(CarditColors.SurfaceMid, RoundedCornerShape(20.dp)).padding(horizontal = 28.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Rounded.WorkspacePremium, null, tint = CarditColors.Gold, modifier = Modifier.size(40.dp))
                Spacer(Modifier.width(16.dp))
                Column {
                    Text("OFFICIAL WRAP SELECTION", style = CarditType.LabelSm, color = CarditColors.TextWarm)
                    Text("최우수 주행 다큐멘터리", style = CarditType.HeadlineSm, color = CarditColors.TextHigh)
                }
            }
        }
        Spacer(Modifier.height(36.dp))

        Row(Modifier.fillMaxWidth().height(580.dp), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
            HighlightCollage(s.highlights, Modifier.weight(1.45f).fillMaxHeight())
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(28.dp)) {
                StatCard("TELEMATICS LOG", Icons.Rounded.Route, Modifier.weight(1f)) {
                    Text("총 주행거리", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                    BigValue(s.distanceKm?.toString() ?: "–", "km")
                    Text(s.routeLabel, style = CarditType.LabelMd, color = CarditColors.TextWarm, maxLines = 1)
                    Spacer(Modifier.height(10.dp))
                    RouteLine(Modifier.fillMaxWidth().height(20.dp))
                }
                StatCard("WEATHER SENSOR", Icons.Rounded.Thunderstorm, Modifier.weight(1f), iconTint = CarditColors.LavenderSoft) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.WaterDrop, null, tint = CarditColors.LavenderSoft, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("비 오는 길 주행", style = CarditType.LabelMd, color = CarditColors.TextHigh)
                    }
                    BigValue(s.rainMinutes?.toString() ?: "–", "분", unitColor = CarditColors.LavenderSoft)
                    Row(
                        Modifier.fillMaxWidth().background(CarditColors.SurfaceHigh, RoundedCornerShape(12.dp)).padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Rounded.CloudDone, null, tint = CarditColors.LavenderSoft, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("와이퍼 자동 가동 ${s.wiperCount ?: 0}회\n감성 레인보우 필터 자동 적용됨", style = CarditType.LabelSm, color = CarditColors.TextHigh)
                    }
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(28.dp)) {
                StatCard("ENERGY USAGE", Icons.Rounded.ElectricCar, Modifier.weight(1f)) {
                    Text("배터리 사용량", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            BigValue(s.batteryUsedPct?.toString() ?: "–", "%")
                            Text("잔여 ${s.batteryLeftPct ?: "–"}%", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                        }
                        EnergyRing((s.batteryLeftPct ?: 0) / 100f, Modifier.size(100.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("●  평균전비 ${s.efficiency ?: "–"} 달성", style = CarditType.LabelMd, color = CarditColors.TextHigh)
                }
                StatCard("MEDIA CAPTURE", Icons.Rounded.PhotoCamera, Modifier.weight(1f)) {
                    Text("사진 & 영상", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                    BigValue("${s.mediaCount}", "개")
                    Spacer(Modifier.weight(1f))
                    Row {
                        Text("차량 빌트인 캠\n${s.builtinCam}", style = CarditType.LabelMd, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
                        Text("스마트폰 연동\n${s.phoneCount}", style = CarditType.LabelMd, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(Modifier.height(28.dp))
        Row(Modifier.fillMaxWidth().height(280.dp), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
            StatCard("CURATED SCENES", Icons.Rounded.Star, Modifier.weight(1f).fillMaxHeight()) {
                Spacer(Modifier.weight(1f))
                Text("하이라이트 북마크", style = CarditType.LabelMd, color = CarditColors.TextHigh)
                BigValue("${s.highlights}", "개 씬", valueColor = CarditColors.GoldSoft)
                Spacer(Modifier.height(10.dp))
                Pill("감독판 컷 포함", container = CarditColors.Gold.copy(alpha = 0.18f), content = CarditColors.Gold, height = 34.dp, shape = RoundedCornerShape(6.dp))
            }
            GlassCard(Modifier.weight(2.4f).fillMaxHeight(), fill = CarditColors.SurfaceLow) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.SentimentVerySatisfied, null, tint = CarditColors.GoldSoft, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("CABIN SOUND SENSOR", style = CarditType.LabelMd, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
                    Pill("PEAK JOY LOGGED", container = CarditColors.CoralSoft.copy(alpha = 0.25f), content = CarditColors.CoralSoft, height = 34.dp)
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("차 안의 큰 웃음", style = CarditType.LabelMd, color = CarditColors.TextHigh)
                        BigValue("${s.laughs}", "회 감지")
                        Text("최고 데시벨 구간: 기장 해안도로 통과 시 (${s.peakDb}dB)", style = CarditType.BodyMd, color = CarditColors.TextHigh)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.MusicNote, null, tint = CarditColors.TextWarm, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("가장 많이 들은 곡: Sunset Highway (4회 재생)", style = CarditType.LabelSm, color = CarditColors.TextWarm)
                        }
                    }
                    Waveform(SampleData.laughWave, barWidth = 18.dp, gap = 10.dp, maxHeight = 110.dp, activeCount = 8)
                }
            }
        }
        Spacer(Modifier.height(28.dp))
        GlassCard(Modifier.fillMaxWidth(), radius = 32.dp, fill = CarditColors.SurfaceLow, padding = PaddingValues(horizontal = 28.dp, vertical = 22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconCircle(Icons.Rounded.MovieCreation, size = 64.dp, container = CarditColors.SurfaceHigh, tint = CarditColors.GoldSoft)
                Spacer(Modifier.width(20.dp))
                Column(Modifier.weight(1f)) {
                    Text("시사회 엔딩 크레딧 생성 준비 완료", style = CarditType.HeadlineMd, color = CarditColors.TextHigh)
                    Text("출연진 자막과 차내 주행 사운드트랙이 자동 믹싱됩니다.", style = CarditType.BodyLg, color = CarditColors.TextWarm)
                }
                DarkButton("나중에 하기", height = 84.dp) { vm.go(Screen.HOME) }
                Spacer(Modifier.width(20.dp))
                GoldButton("엔딩 크레딧 만들기", height = 96.dp, modifier = Modifier.width(360.dp)) { vm.go(Screen.STYLE) }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, icon: ImageVector, modifier: Modifier, iconTint: androidx.compose.ui.graphics.Color = CarditColors.Gold, content: @Composable ColumnScope.() -> Unit) {
    GlassCard(modifier, fill = CarditColors.SurfaceLow, padding = PaddingValues(26.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = CarditType.LabelMd, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(30.dp))
        }
        Spacer(Modifier.height(10.dp))
        content()
    }
}

@Composable
private fun BigValue(value: String, unit: String, valueColor: androidx.compose.ui.graphics.Color = CarditColors.TextHigh, unitColor: androidx.compose.ui.graphics.Color = CarditColors.TextHigh) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(value, style = CarditType.DisplayLg, color = valueColor)
        Spacer(Modifier.width(6.dp))
        Text(unit, style = CarditType.HeadlineSm, color = unitColor, modifier = Modifier.padding(bottom = 6.dp))
    }
}

@Composable
private fun RouteLine(modifier: Modifier) {
    Canvas(modifier) {
        val y = size.height / 2
        val p = Path().apply {
            moveTo(0f, y)
            cubicTo(size.width * 0.2f, y - 10f, size.width * 0.35f, y + 10f, size.width * 0.5f, y)
            cubicTo(size.width * 0.65f, y - 10f, size.width * 0.8f, y + 10f, size.width, y)
        }
        drawPath(p, CarditColors.Gold, style = Stroke(5f, cap = StrokeCap.Round))
        listOf(0f, 0.5f, 1f).forEach { drawCircle(CarditColors.GoldSoft, 9f, Offset(size.width * it, y)) }
    }
}

@Composable
private fun EnergyRing(fraction: Float, modifier: Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = Stroke(14f, cap = StrokeCap.Round)
            drawArc(CarditColors.SurfaceHighest, 0f, 360f, false, style = stroke)
            drawArc(CarditColors.Gold, -90f, 360f * fraction.coerceIn(0f, 1f), false, style = stroke)
        }
        Icon(Icons.Rounded.Bolt, null, tint = CarditColors.Gold, modifier = Modifier.size(34.dp))
    }
}

@Composable
private fun HighlightCollage(count: Int, modifier: Modifier) {
    GlassCard(modifier, fill = CarditColors.SurfaceLow) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Pill("HIGHLIGHT MOMENTS", dot = CarditColors.Gold, container = CarditColors.SurfaceHigh, content = CarditColors.TextHigh, height = 40.dp)
            Spacer(Modifier.weight(1f))
            Text("3 CANVASES SELECTED", style = CarditType.LabelMd, color = CarditColors.TextWarm)
        }
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            CollageCard(R.drawable.img_wrap_bridge, "광안대교 해안 드라이브", null, Modifier.offset(x = (-150).dp, y = 14.dp).rotate(-7f))
            CollageCard(R.drawable.img_wrap_rest, "휴식", null, Modifier.offset(x = 150.dp, y = 4.dp).rotate(7f))
            CollageCard(R.drawable.img_wrap_cabin, "선루프 열고 다함께 떼창", "BEST CUT #01", Modifier)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.AutoAwesome, null, tint = CarditColors.TextHigh, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text("AI가 차내 마이크 및 룸미러 비전 센서로 엄선한 순간", style = CarditType.BodyMd, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
            Pill("전체 ${count}컷 보기", container = CarditColors.SurfaceHigh, content = CarditColors.TextHigh, height = 40.dp, shape = RoundedCornerShape(8.dp))
        }
    }
}

@Composable
private fun CollageCard(@DrawableRes res: Int, caption: String, badge: String?, modifier: Modifier) {
    CoverImage(res, modifier.size(width = 300.dp, height = 300.dp), radius = 20.dp, scrim = 0.85f) {
        Column(Modifier.align(Alignment.BottomStart).padding(18.dp)) {
            if (badge != null) Text(badge, style = CarditType.LabelSm, color = CarditColors.Gold)
            Text(caption, style = CarditType.LabelLg, color = CarditColors.TextHigh)
        }
    }
}
