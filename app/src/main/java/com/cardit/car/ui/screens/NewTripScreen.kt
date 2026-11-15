package com.cardit.car.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MovieCreation
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Theaters
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.cardit.car.CarditViewModel
import com.cardit.car.R
import com.cardit.car.Screen
import com.cardit.car.data.Permission
import com.cardit.car.data.SampleData
import com.cardit.car.ui.components.CoverImage
import com.cardit.car.ui.components.DarkButton
import com.cardit.car.ui.components.GlassCard
import com.cardit.car.ui.components.GoldButton
import com.cardit.car.ui.components.IconCircle
import com.cardit.car.ui.components.InitialAvatar
import com.cardit.car.ui.components.Pill
import com.cardit.car.ui.components.RailScaffold
import com.cardit.car.ui.components.pressable
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

@Composable
fun NewTripScreen(vm: CarditViewModel) {
    RailScaffold(Screen.NEW_TRIP, vm::go, { vm.devPanelOpen = true }, vm.tripActive) {
        Row(Modifier.fillMaxWidth().padding(vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("PRODUCTION SETUP", style = CarditType.LabelLg, color = CarditColors.Gold)
            Text("   /   ", style = CarditType.LabelLg, color = CarditColors.TextLow)
            Text("SCENE 01: TRIP INITIALIZATION", style = CarditType.LabelLg, color = CarditColors.TextWarm)
            Spacer(Modifier.weight(1f))
            StepDots(0)
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            PosterPreview(vm.tripTitle, Modifier.weight(0.74f).fillMaxHeight())
            TripForm(vm, Modifier.weight(1f).fillMaxHeight())
        }
    }
}

@Composable
fun StepDots(active: Int, total: Int = 3) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        repeat(total) { i ->
            Box(
                Modifier.size(width = if (i == active) 60.dp else 20.dp, height = 8.dp)
                    .background(if (i == active) CarditColors.Gold else CarditColors.SurfaceHighest, RoundedCornerShape(4.dp)),
            )
        }
    }
}

@Composable
private fun PosterPreview(title: String, modifier: Modifier) {
    CoverImage(R.drawable.img_newtrip_poster, modifier, radius = 36.dp, scrim = 0.95f) {
        Column(Modifier.fillMaxSize().padding(40.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Pill("OFFICIAL SELECTION 2026", icon = Icons.Rounded.WorkspacePremium, container = CarditColors.Canvas.copy(alpha = 0.75f), content = CarditColors.GoldSoft, iconTint = CarditColors.Gold, height = 50.dp, style = CarditType.LabelLg)
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Pill("ROAD MOVIE", container = CarditColors.Gold, content = CarditColors.OnGold, height = 36.dp, shape = RoundedCornerShape(6.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("ASPECT 2.39:1", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                }
            }
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.CalendarMonth, null, tint = CarditColors.Gold, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
                Text("2026.11.21 – 11.22", style = CarditType.LabelLg, color = CarditColors.Gold)
                Spacer(Modifier.width(24.dp))
                Text("BUSAN COAST", style = CarditType.LabelLg, color = CarditColors.TextWarm)
            }
            Text(title.ifBlank { "제목 없는 여행" }, style = CarditType.DisplayXl, color = CarditColors.TextHigh)
            Spacer(Modifier.height(12.dp))
            Text("\"어느 늦가을, 차창 너머 번지는 노을과 네 사람의 기록\"", style = CarditType.BodyXl.copy(fontStyle = FontStyle.Italic), color = CarditColors.TextWarm)
            Spacer(Modifier.height(28.dp))
            Column(
                Modifier.fillMaxWidth().background(CarditColors.Canvas.copy(alpha = 0.55f), RoundedCornerShape(16.dp)).padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "STARRING " + SampleData.participants.joinToString(" · ") { it.nameEn },
                    style = CarditType.LabelLg, color = CarditColors.TextHigh,
                )
                Spacer(Modifier.height(6.dp))
                Text("DIRECTED BY CARDIT AI   ·   SOUNDSCAPE CINEMA SOUND", style = CarditType.LabelSm, color = CarditColors.TextWarm)
            }
        }
    }
}

@Composable
private fun TripForm(vm: CarditViewModel, modifier: Modifier) {
    var bookmarked by remember { mutableStateOf(false) }
    GlassCard(modifier, radius = 40.dp, fill = CarditColors.SurfaceMid, padding = PaddingValues(40.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Field("여행 제목", Icons.Rounded.MovieCreation, vm.tripTitle, { vm.tripTitle = it }, Icons.Rounded.Edit, Modifier.weight(1f))
            Field("목적지", Icons.Rounded.Place, vm.destination, { vm.destination = it }, Icons.Rounded.Navigation, Modifier.weight(1f))
        }
        Spacer(Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Group, null, tint = CarditColors.TextWarm, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(10.dp))
            Text("함께하는 사람 (${SampleData.participants.size}명)", style = CarditType.LabelLg, color = CarditColors.TextWarm)
            Spacer(Modifier.weight(1f))
            Pill("차량 블루투스 동기화 완료", container = CarditColors.Gold.copy(alpha = 0.15f), content = CarditColors.Gold, height = 38.dp, shape = RoundedCornerShape(6.dp))
        }
        Spacer(Modifier.height(18.dp))
        SampleData.participants.chunked(2).forEachIndexed { row, pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(bottom = 18.dp)) {
                pair.forEachIndexed { col, p ->
                    val idx = row * 2 + col
                    Row(
                        Modifier.weight(1f).background(CarditColors.SurfaceHigh, RoundedCornerShape(22.dp)).padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        InitialAvatar(p.name.take(1), p.avatar, size = 64.dp, online = true)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(p.name, style = CarditType.HeadlineSm, color = CarditColors.TextHigh)
                            Text(p.seat, style = CarditType.LabelSm, color = CarditColors.TextWarm, maxLines = 1)
                        }
                        Segmented(Permission.entries.map { it.label }, vm.permissions[idx].ordinal) { vm.setPermission(idx, Permission.entries[it]) }
                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Pill("+ 초대", icon = Icons.Rounded.PersonAdd, container = CarditColors.SurfaceHigh, content = CarditColors.GoldSoft, height = 56.dp, style = CarditType.LabelLg) {
                vm.showToast("모바일 웹 초대 링크는 2단계(Firebase 연동)에서 제공됩니다")
            }
            Spacer(Modifier.width(18.dp))
            Text("스마트폰 카드키 또는 패스 공유 링크 생성 가능", style = CarditType.BodyMd, color = CarditColors.TextWarm)
        }
        Spacer(Modifier.weight(1f))
        Row(
            Modifier.fillMaxWidth().background(CarditColors.Canvas.copy(alpha = 0.6f), RoundedCornerShape(24.dp)).padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconCircle(Icons.Rounded.Mic, size = 56.dp, container = CarditColors.Rec.copy(alpha = 0.18f), tint = CarditColors.CoralSoft, shape = RoundedCornerShape(14.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text("AI CONSOLE PRIVACY  ●", style = CarditType.LabelMd, color = CarditColors.TextHigh)
                Text("차량 내 대화 기록은 참여자 전원 동의 후에만 켜집니다", style = CarditType.BodyMd, color = CarditColors.TextWarm)
            }
            Text("4/4 승인됨", style = CarditType.LabelLg, color = CarditColors.Gold)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Rounded.Verified, null, tint = CarditColors.Gold, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            DarkButton("스토리 스타일 사전설정", leading = Icons.Rounded.Tune, height = 88.dp, modifier = Modifier.weight(1f)) { vm.go(Screen.STYLE) }
            IconCircle(
                if (bookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder, size = 88.dp,
                shape = RoundedCornerShape(24.dp), tint = CarditColors.TextHigh,
            ) { bookmarked = !bookmarked }
            GoldButton("여행 시작하기", trailing = Icons.Rounded.Theaters, sub = "ACTION SCENE", height = 100.dp, modifier = Modifier.weight(1f)) { vm.startTrip() }
        }
    }
}

@Composable
private fun Field(label: String, icon: ImageVector, value: String, onChange: (String) -> Unit, trailing: ImageVector, modifier: Modifier) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = CarditColors.TextWarm, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text(label, style = CarditType.LabelLg, color = CarditColors.TextWarm)
        }
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth().height(76.dp).background(CarditColors.SurfaceHigh, RoundedCornerShape(18.dp)).padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value, onChange, singleLine = true,
                textStyle = CarditType.HeadlineMd.copy(color = CarditColors.TextHigh),
                cursorBrush = SolidColor(CarditColors.Gold),
                modifier = Modifier.weight(1f),
            )
            Icon(trailing, null, tint = CarditColors.Gold, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun Segmented(options: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Row(Modifier.background(CarditColors.Canvas.copy(alpha = 0.8f), RoundedCornerShape(14.dp)).padding(4.dp)) {
        options.forEachIndexed { i, o ->
            val on = i == selected
            Box(
                Modifier.pressable { onSelect(i) }
                    .background(if (on) CarditColors.Gold else androidx.compose.ui.graphics.Color.Transparent, RoundedCornerShape(10.dp))
                    .height(52.dp).padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center,
            ) { Text(o, style = CarditType.LabelMd, color = if (on) CarditColors.OnGold else CarditColors.TextWarm) }
        }
    }
}
