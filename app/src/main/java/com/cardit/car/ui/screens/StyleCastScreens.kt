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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowCircleRight
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.LocalMovies
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SportsMotorsports
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TheaterComedy
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material.icons.rounded.VideoCall
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cardit.car.CarditViewModel
import com.cardit.car.Screen
import com.cardit.car.data.Genre
import com.cardit.car.data.SampleData
import com.cardit.car.ui.components.CoverImage
import com.cardit.car.ui.components.DarkButton
import com.cardit.car.ui.components.GlassCard
import com.cardit.car.ui.components.GoldButton
import com.cardit.car.ui.components.IconCircle
import com.cardit.car.ui.components.Pill
import com.cardit.car.ui.components.RailScaffold
import com.cardit.car.ui.components.marquee
import com.cardit.car.ui.components.pressable
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

// ─────────────────────────── 장르(스타일) 선택 ───────────────────────────

@Composable
fun StyleScreen(vm: CarditViewModel) {
    RailScaffold(Screen.STYLE, vm::go, { vm.devPanelOpen = true }, vm.tripActive) {
        PageHeader(
            tag = "PHASE 03 · NARRATIVE TONE", crumb = "/ 엔딩 크레딧 스타일",
            title = "어떤 영화로 만들까요?", sub = "AI가 수집된 주행 데이터와 사진을 바탕으로 영화적 연출을 입힙니다.",
            badgeLeft = "AAOS 시네마틱 엔진", badgeRight = "실시간 메타 분석 완료",
        )
        Spacer(Modifier.height(32.dp))
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(28.dp)) {
            Genre.entries.forEachIndexed { i, g -> GenreCard(g, i, g == vm.genre, Modifier.weight(1f).fillMaxHeight()) { vm.genre = g } }
        }
        Spacer(Modifier.height(32.dp))
        GlassCard(Modifier.fillMaxWidth(), radius = 36.dp, fill = CarditColors.SurfaceLow, padding = PaddingValues(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    Modifier.background(CarditColors.SurfaceHigh, RoundedCornerShape(50)).padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconCircle(
                        if (vm.bgmPreview) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, size = 60.dp,
                        container = CarditColors.Gold, tint = CarditColors.OnGold,
                    ) { vm.bgmPreview = !vm.bgmPreview }
                    Spacer(Modifier.width(20.dp))
                    Column {
                        Text("미리듣기  •  ${vm.genre.bpm} BPM", style = CarditType.LabelMd, color = CarditColors.GoldSoft)
                        Text(vm.genre.bgm, style = CarditType.HeadlineSm, color = CarditColors.TextHigh)
                    }
                    Spacer(Modifier.width(40.dp))
                    Icon(Icons.Rounded.Equalizer, null, tint = if (vm.bgmPreview) CarditColors.Gold else CarditColors.TextLow, modifier = Modifier.size(40.dp))
                }
                Spacer(Modifier.weight(1f))
                DarkButton("이전", leading = Icons.Rounded.ArrowBack, height = 100.dp, textStyle = CarditType.HeadlineMd) { vm.go(Screen.WRAP_UP) }
                Spacer(Modifier.width(20.dp))
                GoldButton("다음 단계", trailing = Icons.Rounded.ArrowCircleRight, height = 100.dp, modifier = Modifier.width(300.dp)) { vm.go(Screen.CAST) }
            }
        }
    }
}

@Composable
private fun GenreCard(g: Genre, index: Int, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(32.dp)
    Column(
        modifier
            .pressable(onClick = onClick)
            .marquee(selected, 32.dp)
            .alpha(if (selected) 1f else 0.6f)
            .background(CarditColors.SurfaceMid, shape),
    ) {
        CoverImage(g.image, Modifier.fillMaxWidth().weight(1.1f), radius = 32.dp, scrim = 0f) {
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0.5f to Color.Transparent, 1f to CarditColors.SurfaceMid)))
            Row(Modifier.fillMaxWidth().padding(22.dp), verticalAlignment = Alignment.CenterVertically) {
                Pill(g.subLabel, container = g.accent.copy(alpha = 0.18f), content = g.accent, height = 42.dp)
                Spacer(Modifier.weight(1f))
                if (selected) Pill("선택됨", icon = Icons.Rounded.CheckCircle, container = CarditColors.Gold, content = CarditColors.OnGold, height = 42.dp)
                else Pill("후보 0${index + 1}", container = CarditColors.Canvas.copy(alpha = 0.7f), content = CarditColors.TextWarm, height = 42.dp)
            }
            Row(Modifier.align(Alignment.BottomStart).padding(horizontal = 30.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(if (index == 0) Icons.Rounded.WorkspacePremium else if (index == 1) Icons.Rounded.SportsMotorsports else Icons.Rounded.AutoAwesome, null, tint = CarditColors.GoldSoft, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(10.dp))
                Text(g.badge, style = CarditType.LabelMd, color = CarditColors.GoldSoft)
            }
        }
        Column(Modifier.weight(0.9f).padding(horizontal = 30.dp, vertical = 20.dp)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(g.label, style = CarditType.DisplayLg, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
                Text(g.mood, style = CarditType.LabelMd, color = CarditColors.GoldSoft, modifier = Modifier.padding(bottom = 10.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(g.description, style = CarditType.BodyXl.copy(fontWeight = null), color = CarditColors.TextHigh.copy(alpha = 0.85f))
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Pill(g.tagline, dot = g.accent, container = CarditColors.SurfaceHigh, content = g.accent, height = 48.dp)
                Spacer(Modifier.weight(1f))
                if (selected) IconCircle(Icons.Rounded.Check, size = 56.dp, container = CarditColors.Gold, tint = CarditColors.OnGold)
                else IconCircle(Icons.Rounded.ArrowForward, size = 56.dp, container = CarditColors.SurfaceHigh, tint = CarditColors.TextWarm)
            }
        }
    }
}

// ─────────────────────────── 출연진 ───────────────────────────

private val roleIcons: Map<String, ImageVector> = mapOf(
    "감독" to Icons.Rounded.LocalMovies,
    "조연출" to Icons.Rounded.VideoCall,
    "음악 감독" to Icons.Rounded.MusicNote,
    "스턴트" to Icons.Rounded.SportsMotorsports,
    "카메오" to Icons.Rounded.Star,
)

@Composable
fun CastScreen(vm: CarditViewModel) {
    RailScaffold(Screen.CAST, vm::go, { vm.devPanelOpen = true }, vm.tripActive) {
        PageHeader(
            tag = "AI NEURAL CASTING · AAOS ROADSCRIPT", crumb = "ACT VI / SCENE 04",
            title = "출연진 및 역할", sub = "주행 중 AI가 감지한 활약상으로 배정된 공식 엔딩 크레딧 배역입니다.",
            badgeLeft = "동승자 4인 인식 완료", badgeRight = "센서 로그 84개 분석", dot = true,
        )
        Spacer(Modifier.height(32.dp))
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            SampleData.participants.forEachIndexed { i, p ->
                val selected = vm.selectedCast == i
                val role = vm.roles[i]
                val rec = vm.recommendations[i]
                val aiPick = role == rec.role
                GlassCard(
                    Modifier.weight(1f).fillMaxHeight().marquee(selected, 32.dp).pressable { vm.selectedCast = i },
                    radius = 32.dp, fill = CarditColors.SurfaceMid, padding = PaddingValues(18.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Pill(
                            if (aiPick) "AI 추천" else "직접 지정", icon = if (aiPick) Icons.Rounded.ChevronLeft else Icons.Rounded.Edit,
                            container = CarditColors.Gold.copy(alpha = 0.18f), content = CarditColors.GoldSoft, height = 38.dp,
                        )
                        Spacer(Modifier.weight(1f))
                        IconCircle(Icons.Rounded.Edit, size = 56.dp, tint = CarditColors.TextHigh) {
                            vm.selectedCast = i
                            vm.roleEditorOpen = true
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    CoverImage(p.photo, Modifier.fillMaxWidth().weight(1f), radius = 24.dp, scrim = 0.9f) {
                        Row(Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.Bottom) {
                            Column(Modifier.weight(1f)) {
                                Text("STARRING 0${i + 1}", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                                Text(p.name, style = CarditType.DisplayLg, color = CarditColors.TextHigh)
                            }
                            Icon(p.badgeIcon, null, tint = CarditColors.Gold, modifier = Modifier.size(36.dp))
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Pill(role, icon = roleIcons[role] ?: p.roleIcon, container = CarditColors.Gold, content = CarditColors.OnGold, height = 50.dp, style = CarditType.LabelLg)
                        Spacer(Modifier.weight(1f))
                        Text(p.metric, style = CarditType.LabelSm, color = CarditColors.TextWarm)
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(p.reasonIcon, null, tint = CarditColors.TextHigh, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(rec.reason, style = CarditType.BodyLg, color = CarditColors.TextHigh, maxLines = 1)
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        Spacer(Modifier.height(32.dp))
        GlassCard(Modifier.fillMaxWidth(), radius = 32.dp, fill = CarditColors.SurfaceLow, padding = PaddingValues(26.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.TheaterComedy, null, tint = CarditColors.GoldSoft, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("대체 배역 빠른 교체 (선택된 카드에 즉시 적용)", style = CarditType.LabelLg, color = CarditColors.TextWarm)
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        SampleData.altRoles.forEach { r ->
                            Pill(r, icon = roleIcons[r], container = CarditColors.SurfaceHigh, content = CarditColors.TextHigh, height = 68.dp, style = CarditType.LabelLg) { vm.assignRole(r) }
                        }
                        Pill("+ 직접 입력", container = Color.Transparent, content = CarditColors.Gold, height = 68.dp, style = CarditType.LabelLg) { vm.roleEditorOpen = true }
                    }
                }
                Spacer(Modifier.width(24.dp))
                GoldButton("다음: 장면 확인", trailing = Icons.Rounded.ArrowForward, height = 100.dp, textStyle = CarditType.HeadlineLg) { vm.go(Screen.TIMELINE) }
            }
        }
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Tv, null, tint = CarditColors.TextLow, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(10.dp))
            Text("PLEOS CINEMATIC OS v4.2 • VEHICLE COCKPIT PREVIEW", style = CarditType.LabelMd, color = CarditColors.TextLow, modifier = Modifier.weight(1f))
            Text("총 러닝타임: 03:24   •   크레딧 롤 지속: 18초", style = CarditType.LabelMd, color = CarditColors.TextWarm)
        }
    }
}


/** 제작 단계 공통 헤더 */
@Composable
fun PageHeader(tag: String, crumb: String, title: String, sub: String, badgeLeft: String, badgeRight: String, dot: Boolean = false) {
    Row(verticalAlignment = Alignment.Bottom) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Pill(tag, icon = Icons.Rounded.AutoAwesome, container = CarditColors.Gold.copy(alpha = 0.15f), content = CarditColors.Gold, iconTint = CarditColors.Gold, height = 40.dp)
                Spacer(Modifier.width(14.dp))
                Text(crumb, style = CarditType.LabelMd, color = CarditColors.TextWarm)
            }
            Spacer(Modifier.height(10.dp))
            Text(title, style = CarditType.DisplayXl, color = CarditColors.TextHigh)
            Text(sub, style = CarditType.BodyXl.copy(fontWeight = null), color = CarditColors.TextHigh.copy(alpha = 0.8f))
        }
        Row(
            Modifier.background(CarditColors.SurfaceMid, RoundedCornerShape(50)).padding(horizontal = 26.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (dot) Box(Modifier.size(12.dp).background(CarditColors.Gold, CircleShape))
            else Icon(Icons.Rounded.AutoAwesome, null, tint = CarditColors.Gold, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Text(badgeLeft, style = CarditType.LabelLg, color = CarditColors.TextHigh)
            Text("   |   ", style = CarditType.LabelLg, color = CarditColors.TextLow)
            Text(badgeRight, style = CarditType.LabelMd, color = CarditColors.TextWarm)
        }
    }
}
