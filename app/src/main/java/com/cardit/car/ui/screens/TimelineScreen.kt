package com.cardit.car.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.material.icons.rounded.DragIndicator
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.OpenWith
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Subtitles
import androidx.compose.material.icons.rounded.Theaters
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cardit.car.CarditViewModel
import com.cardit.car.R
import com.cardit.car.Screen
import com.cardit.car.data.Chapter
import com.cardit.car.data.SampleData
import com.cardit.car.formatTime
import com.cardit.car.ui.components.CoverImage
import com.cardit.car.ui.components.DarkButton
import com.cardit.car.ui.components.GlassCard
import com.cardit.car.ui.components.GoldButton
import com.cardit.car.ui.components.IconCircle
import com.cardit.car.ui.components.Pill
import com.cardit.car.ui.components.RailScaffold
import com.cardit.car.ui.components.Scrubber
import com.cardit.car.ui.components.pressable
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

@Composable
fun TimelineScreen(vm: CarditViewModel) {
    RailScaffold(Screen.TIMELINE, vm::go, { vm.devPanelOpen = true }, vm.tripActive, scrollable = true) {
        // 헤더
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(Icons.Rounded.EditNote, size = 84.dp, container = CarditColors.SurfaceHigh, tint = CarditColors.Gold, shape = RoundedCornerShape(20.dp))
            Spacer(Modifier.width(24.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("장면 순서 편집", style = CarditType.DisplayLg, color = CarditColors.TextHigh)
                    Spacer(Modifier.width(16.dp))
                    Pill("TOUCH SEQUENCE 07", container = CarditColors.SurfaceHigh, content = CarditColors.GoldSoft, height = 38.dp, shape = RoundedCornerShape(8.dp))
                }
                Text("손가락으로 챕터를 길게 눌러 순서를 바꾸거나, AI 하이라이트를 즉시 삽입하세요.", style = CarditType.BodyXl.copy(fontWeight = null), color = CarditColors.TextHigh.copy(alpha = 0.8f))
            }
            Row(Modifier.background(CarditColors.SurfaceLow, RoundedCornerShape(50)).padding(8.dp)) {
                CutOption("시연판 1분", vm.demoCut) { vm.demoCut = true }
                CutOption("정식판 ${vm.chapters.sumOf { it.durationSec } / 60}분", !vm.demoCut) { vm.demoCut = false }
            }
            Spacer(Modifier.width(24.dp))
            GoldButton("완성하기", leading = Icons.Rounded.Theaters, trailing = Icons.Rounded.ChevronRight, height = 100.dp, textStyle = CarditType.HeadlineLg) { vm.go(Screen.PRESHOW) }
        }
        Spacer(Modifier.height(32.dp))

        Row(Modifier.fillMaxWidth().height(720.dp), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            Column(Modifier.weight(1.6f).fillMaxHeight()) {
                ScenePreview(vm, Modifier.fillMaxWidth().weight(1f))
                Spacer(Modifier.height(22.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    DarkButton("구간 다듬기", leading = Icons.Rounded.ContentCut, height = 64.dp, color = CarditColors.SurfaceMid, textStyle = CarditType.LabelLg) { vm.showToast("구간 다듬기는 다음 버전에서 지원됩니다") }
                    DarkButton("자막/크레딧 수정", leading = Icons.Rounded.Subtitles, height = 64.dp, color = CarditColors.SurfaceMid, textStyle = CarditType.LabelLg) { vm.go(Screen.CAST) }
                    DarkButton("차량 오디오 믹스", leading = Icons.Rounded.VolumeUp, height = 64.dp, color = CarditColors.SurfaceMid, textStyle = CarditType.LabelLg) { vm.go(Screen.STYLE) }
                    Icon(Icons.Rounded.TouchApp, null, tint = CarditColors.TextWarm, modifier = Modifier.size(26.dp))
                    Text("트랙을 길게 눌러 앞뒤로 이동", style = CarditType.BodyLg, color = CarditColors.TextWarm)
                }
            }
            HighlightPanel(vm, Modifier.weight(1f).fillMaxHeight())
        }
        Spacer(Modifier.height(32.dp))

        // 타임라인 트랙
        val selIndex = vm.chapters.indexOfFirst { it.id == vm.selectedChapter }.coerceAtLeast(0)
        val totalSec = vm.chapters.sumOf { it.durationSec }
        val beforeSec = vm.chapters.take(selIndex).sumOf { it.durationSec }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("타임라인 트랙", style = CarditType.HeadlineLg, color = CarditColors.TextHigh)
            Spacer(Modifier.width(16.dp))
            Pill("${vm.chapters.size}개 챕터 구성", container = CarditColors.SurfaceHigh, content = CarditColors.TextWarm, height = 38.dp, shape = RoundedCornerShape(8.dp))
            Spacer(Modifier.weight(1f))
            Box(Modifier.size(14.dp).background(CarditColors.Gold, RoundedCornerShape(3.dp)))
            Spacer(Modifier.width(8.dp))
            Text("현재 재생중: 0${selIndex + 1}. ${vm.chapterById(vm.selectedChapter).title}", style = CarditType.LabelLg, color = CarditColors.TextHigh)
            Spacer(Modifier.width(24.dp))
            Text("전체 길이: ${formatTime(totalSec * 1000L)}", style = CarditType.LabelLg, color = CarditColors.TextHigh)
        }
        Spacer(Modifier.height(18.dp))
        Scrubber((beforeSec + vm.chapterById(vm.selectedChapter).durationSec / 2f) / totalSec, Modifier.fillMaxWidth(), track = 14.dp, thumb = 22.dp)
        Spacer(Modifier.height(22.dp))
        ChapterTrack(vm)
    }
}

@Composable
private fun CutOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.pressable(onClick = onClick)
            .background(if (selected) CarditColors.SurfaceHighest else Color.Transparent, RoundedCornerShape(50))
            .height(84.dp).padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selected) {
            Box(Modifier.size(10.dp).background(CarditColors.Gold, CircleShape))
            Spacer(Modifier.width(12.dp))
        }
        Text(text, style = CarditType.HeadlineSm, color = if (selected) CarditColors.TextHigh else CarditColors.TextWarm)
    }
}

@Composable
private fun ScenePreview(vm: CarditViewModel, modifier: Modifier) {
    val ch = vm.chapterById(vm.selectedChapter)
    val index = vm.chapters.indexOf(ch)
    val isCredit = ch.id == "ch3"
    val lead = SampleData.participants[0]
    Column(modifier.background(Color.Black, RoundedCornerShape(28.dp))) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(10.dp).background(CarditColors.Rec, CircleShape))
            Spacer(Modifier.width(14.dp))
            Text("CHAPTER 0${index + 1} · SCENE PREVIEW", style = CarditType.LabelLg, color = CarditColors.CoralSoft, modifier = Modifier.weight(1f))
            Text("4K · 24.00 FPS · 2.39:1 CINEMASCOPE", style = CarditType.LabelMd, color = CarditColors.TextWarm)
        }
        CoverImage(if (isCredit) R.drawable.img_tl_preview else ch.image, Modifier.fillMaxWidth().weight(1f), radius = 0.dp, scrim = 0.75f) {
            Pill(
                "${formatTime(vm.chapters.take(index).sumOf { it.durationSec } * 1000L + ch.durationSec * 500L)} / ${formatTime(vm.chapters.sumOf { it.durationSec } * 1000L)}",
                icon = Icons.Rounded.Timer, container = CarditColors.Canvas.copy(alpha = 0.75f), content = CarditColors.TextHigh,
                height = 48.dp, modifier = Modifier.align(Alignment.TopEnd).padding(26.dp),
            )
            Column(Modifier.align(Alignment.BottomStart).padding(40.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(width = 36.dp, height = 2.dp).background(CarditColors.Gold))
                    Spacer(Modifier.width(14.dp))
                    Text(if (isCredit) "CAST  &  CREW  ROLL" else "CHAPTER 0${index + 1}", style = CarditType.LabelLg.copy(letterSpacing = CarditType.LabelLg.letterSpacing * 4), color = CarditColors.Gold)
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(if (isCredit) vm.roles[0] else ch.sub, style = CarditType.DisplayMd.copy(fontWeight = null), color = CarditColors.TextHigh.copy(alpha = 0.8f))
                    Spacer(Modifier.width(18.dp))
                    Text(if (isCredit) lead.name else ch.title, style = CarditType.DisplayXl.copy(fontSize = CarditType.DisplayXl.fontSize * 1.2f), color = CarditColors.TextHigh)
                }
                Text("Busan Coastline Highway · 16:45 PM", style = CarditType.BodyXl.copy(fontWeight = null), color = CarditColors.TextMid)
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 18.dp)) {
            Text("CARDIT AUTOMOTIVE MASTERING", style = CarditType.LabelMd, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
            Text("CINEMA GRADE", style = CarditType.LabelMd, color = CarditColors.Gold)
        }
    }
}

@Composable
private fun HighlightPanel(vm: CarditViewModel, modifier: Modifier) {
    GlassCard(modifier, radius = 32.dp, fill = CarditColors.SurfaceLow, padding = PaddingValues(28.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.AutoAwesome, null, tint = CarditColors.Gold, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(10.dp))
            Text("AI 추천 하이라이트", style = CarditType.HeadlineLg, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
            Pill("${SampleData.highlights.size}개 감지됨", container = CarditColors.SurfaceHigh, content = CarditColors.TextWarm, height = 36.dp, shape = RoundedCornerShape(6.dp))
        }
        Spacer(Modifier.height(12.dp))
        Text("인캐빈 마이크 감정 분석과 주행 텔레매틱스 기반으로 추출된 명장면입니다.", style = CarditType.BodyLg, color = CarditColors.TextHigh.copy(alpha = 0.8f))
        Spacer(Modifier.height(20.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SampleData.highlights.forEach { h ->
                val added = h.id in vm.addedHighlights
                Row(
                    Modifier.fillMaxWidth().background(CarditColors.SurfaceMid, RoundedCornerShape(22.dp)).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CoverImage(h.image, Modifier.size(72.dp), radius = 12.dp, scrim = 0f)
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(h.title, style = CarditType.LabelLg, color = CarditColors.TextHigh, maxLines = 1)
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(h.icon, null, tint = CarditColors.TextWarm, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("${h.time}  ·  ", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                            Text(h.meta, style = CarditType.LabelMd, color = h.metaColor, maxLines = 1)
                        }
                    }
                    Pill(
                        if (added) "추가됨" else "추가", icon = if (added) Icons.Rounded.Check else Icons.Rounded.Add,
                        container = if (added) CarditColors.Gold else CarditColors.SurfaceHigh,
                        content = if (added) CarditColors.OnGold else CarditColors.TextHigh, height = 60.dp,
                    ) { vm.toggleHighlight(h.id) }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        val share = 0.78f
        Column(Modifier.fillMaxWidth().background(CarditColors.SurfaceMid, RoundedCornerShape(20.dp)).padding(20.dp)) {
            Row {
                Text("차량 센서 연동 자동 감지", style = CarditType.LabelLg, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
                Text("ACTIVE", style = CarditType.LabelLg, color = CarditColors.Gold)
            }
            Spacer(Modifier.height(14.dp))
            Box(Modifier.fillMaxWidth().height(10.dp).background(CarditColors.SurfaceHighest, RoundedCornerShape(5.dp))) {
                Box(Modifier.fillMaxWidth(share).fillMaxHeight().background(CarditColors.Gold, RoundedCornerShape(5.dp)))
            }
            Spacer(Modifier.height(14.dp))
            Row {
                Text("하이라이트 점유율 ${(share * 100).toInt()}%", style = CarditType.LabelMd, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
                Text("총 ${4 + vm.addedHighlights.size}개 클립 조합됨", style = CarditType.LabelMd, color = CarditColors.TextHigh)
            }
        }
    }
}

@Composable
private fun ChapterTrack(vm: CarditViewModel) {
    val itemW = 320.dp
    val gap = 24.dp
    val stepPx = with(LocalDensity.current) { (itemW + gap).toPx() }
    var draggingId by remember { mutableStateOf<String?>(null) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(gap)) {
        vm.chapters.forEachIndexed { index, ch ->
            key(ch.id) {
                val dragging = draggingId == ch.id
                ChapterCard(
                    ch, index, selected = ch.id == vm.selectedChapter, dragging = dragging,
                    modifier = Modifier
                        .width(itemW).height(180.dp)
                        .zIndex(if (dragging) 1f else 0f)
                        .graphicsLayer {
                            translationX = if (dragging) dragOffset else 0f
                            val s = if (dragging) 1.05f else 1f
                            scaleX = s; scaleY = s
                        }
                        .pointerInput(ch.id) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = { draggingId = ch.id; dragOffset = 0f; vm.selectedChapter = ch.id },
                                onDrag = { change, amount ->
                                    change.consume()
                                    dragOffset += amount.x
                                    val cur = vm.chapters.indexOfFirst { it.id == ch.id }
                                    if (dragOffset > stepPx / 2 && cur < vm.chapters.lastIndex) {
                                        vm.moveChapter(cur, cur + 1); dragOffset -= stepPx
                                    } else if (dragOffset < -stepPx / 2 && cur > 0) {
                                        vm.moveChapter(cur, cur - 1); dragOffset += stepPx
                                    }
                                },
                                onDragEnd = { draggingId = null; dragOffset = 0f },
                                onDragCancel = { draggingId = null; dragOffset = 0f },
                            )
                        }
                        .pressable { vm.selectedChapter = ch.id },
                )
            }
        }
    }
}

@Composable
private fun ChapterCard(ch: Chapter, index: Int, selected: Boolean, dragging: Boolean, modifier: Modifier) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier
            .then(if (dragging) Modifier.shadow(30.dp, shape) else Modifier)
            .then(if (selected) Modifier.border(3.dp, CarditColors.Gold, shape) else Modifier),
    ) {
        CoverImage(ch.image, Modifier.fillMaxSize(), radius = 24.dp, scrim = 0.85f) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                if (dragging) {
                    Icon(Icons.Rounded.DragIndicator, null, tint = CarditColors.TextHigh, modifier = Modifier.size(20.dp))
                    Text("CH 0${index + 1}", style = CarditType.LabelLg, color = CarditColors.TextHigh)
                    Spacer(Modifier.weight(1f))
                    Pill("순서 이동중", icon = Icons.Rounded.OpenWith, container = CarditColors.GoldSoft, content = CarditColors.OnGold, height = 34.dp)
                } else {
                    Pill(
                        "CH 0${index + 1}" + if (ch.cookie) " 🍪" else "", icon = if (selected) Icons.Rounded.PlayArrow else null,
                        container = if (selected) CarditColors.Gold else CarditColors.Canvas.copy(alpha = 0.7f),
                        content = if (selected) CarditColors.OnGold else CarditColors.TextHigh, height = 36.dp,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(formatTime(ch.durationSec * 1000L), style = CarditType.LabelLg, color = CarditColors.TextHigh)
                }
            }
            Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(ch.title, style = CarditType.HeadlineMd, color = if (selected) CarditColors.Gold else CarditColors.TextHigh, maxLines = 1)
                Text(ch.sub, style = CarditType.LabelMd, color = if (selected) CarditColors.TextHigh else CarditColors.GoldSoft, maxLines = 1)
            }
        }
    }
}
