package com.cardit.car.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SatelliteAlt
import androidx.compose.material.icons.rounded.SentimentVerySatisfied
import androidx.compose.material.icons.rounded.SurroundSound
import androidx.compose.material.icons.rounded.Theaters
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cardit.car.CarditViewModel
import com.cardit.car.R
import com.cardit.car.Screen
import com.cardit.car.data.Episode
import com.cardit.car.data.PastTrip
import com.cardit.car.data.SampleData
import com.cardit.car.ui.components.AvatarStack
import com.cardit.car.ui.components.CoverImage
import com.cardit.car.ui.components.DarkButton
import com.cardit.car.ui.components.GlassCard
import com.cardit.car.ui.components.GoldButton
import com.cardit.car.ui.components.IconCircle
import com.cardit.car.ui.components.Pill
import com.cardit.car.ui.components.RailScaffold
import com.cardit.car.ui.components.Scrubber
import com.cardit.car.ui.components.SectionTitle
import com.cardit.car.ui.components.pressable
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

@Composable
fun HomeScreen(vm: CarditViewModel) {
    RailScaffold(Screen.HOME, vm::go, { vm.devPanelOpen = true }, vm.tripActive, scrollable = true) {
        // 헤더
        Row(Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 28.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(width = 18.dp, height = 56.dp).background(CarditColors.Gold, RoundedCornerShape(5.dp)))
            Spacer(Modifier.width(18.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("여행 보관함", style = CarditType.DisplayLg, color = CarditColors.TextHigh)
                    Spacer(Modifier.width(14.dp))
                    Pill("VAULT v2.4", container = CarditColors.SurfaceHigh, content = CarditColors.GoldSoft, height = 36.dp)
                }
                Text("Pleos Connect 차량 시네마틱 여정 아카이브", style = CarditType.BodyLg, color = CarditColors.TextWarm)
            }
            Spacer(Modifier.weight(1f))
            val battery = vm.latestSnapshot?.batteryPercent ?: 84
            Pill("배터리 $battery% · 휴식 모드 ON", icon = Icons.Rounded.BatteryChargingFull, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 48.dp)
            Spacer(Modifier.width(14.dp))
            Pill("시네마 서라운드", icon = Icons.Rounded.SurroundSound, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 48.dp)
            Spacer(Modifier.width(24.dp))
            IconCircle(Icons.Rounded.SentimentVerySatisfied, size = 80.dp, container = CarditColors.SurfaceHigh, tint = CarditColors.Gold)
            Spacer(Modifier.width(24.dp))
            GoldButton("+ 새 여행", leading = Icons.Rounded.AddCircle, height = 100.dp, textStyle = CarditType.HeadlineLg) { vm.go(Screen.NEW_TRIP) }
        }

        // 진행 중 여행 + 지난 여행
        Row(Modifier.fillMaxWidth().height(640.dp), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            CurrentTripCard(vm, Modifier.weight(1.55f).fillMaxSize())
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(32.dp)) {
                SampleData.pastTrips.forEach { PastTripCard(it, Modifier.weight(1f).fillMaxWidth()) { vm.go(Screen.PRESHOW) } }
            }
        }

        Spacer(Modifier.height(48.dp))
        SectionTitle("필름 시리즈 아카이브", "FILM SERIES ARCHIVE") {
            Pill("시즌별 전체보기", icon = Icons.Rounded.CalendarMonth, container = CarditColors.SurfaceMid, content = CarditColors.GoldSoft, height = 52.dp)
        }
        Spacer(Modifier.height(28.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
            SampleData.episodes.forEach { EpisodeCard(it, Modifier.weight(1f)) { vm.go(Screen.PRESHOW) } }
        }

        Spacer(Modifier.height(36.dp))
        BgmBar()
    }
}

@Composable
private fun CurrentTripCard(vm: CarditViewModel, modifier: Modifier) {
    CoverImage(R.drawable.img_home_hero, modifier, radius = 32.dp, scrim = 0f) {
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(0f to CarditColors.Canvas.copy(alpha = 0.35f), 0.45f to Color.Transparent, 1f to CarditColors.Canvas.copy(alpha = 0.95f)),
            ),
        )
        Column(Modifier.fillMaxSize().padding(40.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Pill("진행 중 (RECORDING)", container = CarditColors.Gold, content = CarditColors.OnGold, dot = CarditColors.Rec, height = 46.dp, style = CarditType.LabelLg)
                        Pill("청춘 로드무비", container = CarditColors.SurfaceHigh.copy(alpha = 0.85f), content = CarditColors.GoldSoft, height = 46.dp, style = CarditType.LabelLg)
                    }
                    Pill("AI 컷 어시스트", icon = Icons.Rounded.AutoAwesome, container = CarditColors.SurfaceHigh.copy(alpha = 0.6f), content = CarditColors.TextHigh, height = 42.dp)
                }
                Spacer(Modifier.weight(1f))
                Pill("GPS 동기화됨", icon = Icons.Rounded.SatelliteAlt, container = CarditColors.SurfaceHigh.copy(alpha = 0.9f), content = CarditColors.TextHigh, height = 56.dp)
            }
            Spacer(Modifier.weight(1f))
            Text("CURRENT ROAD TRIP  ·  DAY 2 OF 3", style = CarditType.LabelLg, color = CarditColors.Gold)
            Spacer(Modifier.height(8.dp))
            Text(vm.tripTitle, style = CarditType.DisplayXl, color = CarditColors.TextHigh)
            Spacer(Modifier.height(8.dp))
            Text(
                "해운대 해안도로에서 광안대교 노을까지 · 4명의 잊지 못할 20대 마지막 여름",
                style = CarditType.BodyXl, color = CarditColors.TextHigh.copy(alpha = 0.75f), maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val km = vm.stats.distanceKm?.let { minOf(it, 128) } ?: 128
                StatChip(Icons.Rounded.Place, "$km km", "주행")
                StatChip(Icons.Rounded.PhotoCamera, "214 장", "포착")
                StatChip(Icons.Rounded.Theaters, "${12 + vm.cookiesSaved} 개", "하이라이트")
            }
            Spacer(Modifier.height(36.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Column {
                    Text("탑승한 크리에이터", style = CarditType.LabelLg, color = CarditColors.TextWarm)
                    Spacer(Modifier.height(12.dp))
                    AvatarStack(4, 4)
                }
                Spacer(Modifier.width(28.dp))
                GoldButton(
                    "엔딩 크레딧 프리뷰 제작", leading = Icons.Rounded.Theaters, height = 100.dp,
                    textStyle = CarditType.HeadlineLg, modifier = Modifier.weight(1f),
                ) { vm.wrapUp() }
            }
        }
    }
}

@Composable
private fun StatChip(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Row(
        Modifier.background(CarditColors.SurfaceHigh.copy(alpha = 0.85f), RoundedCornerShape(50)).height(52.dp).padding(horizontal = 22.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = CarditColors.GoldSoft, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(10.dp))
        Text(value, style = CarditType.HeadlineSm, color = CarditColors.TextHigh)
        Spacer(Modifier.width(8.dp))
        Text(label, style = CarditType.LabelMd, color = CarditColors.TextWarm)
    }
}

@Composable
private fun PastTripCard(trip: PastTrip, modifier: Modifier, onReplay: () -> Unit) {
    CoverImage(trip.image, modifier, radius = 32.dp, scrim = 0f) {
        Box(
            Modifier.fillMaxSize().background(
                Brush.horizontalGradient(0f to CarditColors.Canvas.copy(alpha = 0.92f), 0.6f to CarditColors.Canvas.copy(alpha = 0.45f), 1f to CarditColors.Canvas.copy(alpha = 0.2f)),
            ),
        )
        Column(Modifier.fillMaxSize().padding(28.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Pill(trip.tag, container = trip.tagColor.copy(alpha = 0.35f), content = CarditColors.TextHigh, height = 40.dp)
                Spacer(Modifier.width(16.dp))
                Text(trip.date, style = CarditType.LabelLg, color = CarditColors.TextWarm)
                Spacer(Modifier.weight(1f))
                Pill(trip.runtime, icon = Icons.Rounded.Timer, container = CarditColors.Canvas.copy(alpha = 0.7f), content = CarditColors.Gold, height = 40.dp)
            }
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f)) {
                    Text(trip.title, style = CarditType.HeadlineLg, color = CarditColors.TextHigh, maxLines = 2)
                    Spacer(Modifier.height(6.dp))
                    Text(trip.desc, style = CarditType.LabelMd, color = CarditColors.TextWarm, maxLines = 2)
                }
                Spacer(Modifier.width(16.dp))
                DarkButton("엔딩 다시 보기", leading = Icons.Rounded.PlayArrow, color = CarditColors.SurfaceHigh.copy(alpha = 0.92f), onClick = onReplay)
            }
        }
    }
}

@Composable
private fun EpisodeCard(ep: Episode, modifier: Modifier, onClick: () -> Unit) {
    GlassCard(modifier.pressable(onClick = onClick), radius = 28.dp, padding = androidx.compose.foundation.layout.PaddingValues(14.dp)) {
        CoverImage(ep.image, Modifier.fillMaxWidth().height(220.dp), radius = 22.dp, scrim = 0.5f) {
            Pill(ep.ep, container = CarditColors.Canvas.copy(alpha = 0.75f), content = CarditColors.Gold, height = 36.dp, modifier = Modifier.padding(14.dp))
            Row(Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Pill(ep.runtime, container = CarditColors.Canvas.copy(alpha = 0.75f), content = CarditColors.TextHigh, height = 34.dp)
                Spacer(Modifier.weight(1f))
                Pill("쿠키 🍪 ${ep.cookies}개", container = CarditColors.CoralSoft, content = CarditColors.OnGold, height = 34.dp, shape = RoundedCornerShape(8.dp))
            }
        }
        Column(Modifier.padding(horizontal = 6.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(ep.season, style = CarditType.LabelMd, color = CarditColors.TextWarm, modifier = Modifier.weight(1f))
                Icon(Icons.Rounded.NorthEast, null, tint = CarditColors.TextWarm, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(4.dp))
            Text(ep.title, style = CarditType.HeadlineMd, color = CarditColors.TextHigh)
            Text(ep.desc, style = CarditType.LabelMd, color = CarditColors.TextWarm, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun BgmBar() {
    GlassCard(Modifier.fillMaxWidth(), radius = 32.dp, padding = androidx.compose.foundation.layout.PaddingValues(horizontal = 40.dp, vertical = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(Icons.Rounded.MusicNote, size = 60.dp, container = CarditColors.Gold, tint = CarditColors.OnGold, shape = RoundedCornerShape(14.dp))
            Spacer(Modifier.width(18.dp))
            Column(Modifier.widthIn(max = 420.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("여정 BGM · Sunset Highway", style = CarditType.LabelLg, color = CarditColors.TextHigh)
                    Spacer(Modifier.width(10.dp))
                    Text("CINEMA SOUND", style = CarditType.LabelMd, color = CarditColors.Gold)
                }
                Text("부산 해안도로 테마 사운드트랙 (Original Score)", style = CarditType.LabelMd, color = CarditColors.TextWarm)
            }
            Spacer(Modifier.width(36.dp))
            Text("01:45", style = CarditType.LabelLg, color = CarditColors.TextWarm)
            Spacer(Modifier.width(16.dp))
            Scrubber(0.45f, Modifier.weight(1f), track = 14.dp, thumb = 0.dp)
            Spacer(Modifier.width(16.dp))
            Text("03:52", style = CarditType.LabelLg, color = CarditColors.TextWarm)
            Spacer(Modifier.width(36.dp))
            Pill("앰비언트 연동: 웜 골드", icon = Icons.Rounded.LightMode, container = CarditColors.SurfaceHigh, content = CarditColors.TextHigh, height = 52.dp)
        }
    }
}
