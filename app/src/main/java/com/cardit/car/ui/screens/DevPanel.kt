package com.cardit.car.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.cardit.car.CarditViewModel
import com.cardit.car.SignalMode
import com.cardit.car.data.SampleData
import com.cardit.car.gate.GateDecision
import com.cardit.car.ui.components.DarkButton
import com.cardit.car.ui.components.GoldButton
import com.cardit.car.ui.components.OverlayPanel
import com.cardit.car.ui.components.Pill
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

/** 레일의 조정(tune) 버튼 / 주행·차단 화면 상단 칩으로 여는 시연·점검 패널 */
@Composable
fun DevPanel(vm: CarditViewModel) {
    OverlayPanel(onDismiss = { vm.devPanelOpen = false }) {
        Text("신호 · 차량 데이터 점검", style = CarditType.HeadlineLg, color = CarditColors.TextHigh)
        Spacer(Modifier.height(24.dp))
        Text("신호 소스", style = CarditType.LabelMd, color = CarditColors.TextWarm)
        Spacer(Modifier.height(10.dp))
        Segmented(listOf("실차 (CarUxRestrictions)", "시연 신호"), vm.signalMode.ordinal) { vm.switchSignalMode(SignalMode.entries[it]) }
        Spacer(Modifier.height(20.dp))
        val (label, color) = when (vm.gate) {
            GateDecision.ALLOW -> "주차 중 — 상영 가능" to CarditColors.Success
            GateDecision.BLOCK_DRIVING -> "주행 중 — 상영 차단" to CarditColors.Crimson
            GateDecision.BLOCK_UNKNOWN -> "판정 불가 — 상영 차단 (fail-safe)" to CarditColors.Coral
        }
        Text(label, style = CarditType.HeadlineMd, color = color)
        if (vm.signalMode == SignalMode.DEMO) {
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Pill("주차", container = CarditColors.SurfaceHigh, content = CarditColors.TextHigh, height = 64.dp, style = CarditType.LabelLg) { vm.demoSource.setParked() }
                Pill("주행", container = CarditColors.SurfaceHigh, content = CarditColors.TextHigh, height = 64.dp, style = CarditType.LabelLg) { vm.demoSource.setDriving() }
                Pill("신호 끊김", container = CarditColors.SurfaceHigh, content = CarditColors.TextHigh, height = 64.dp, style = CarditType.LabelLg) { vm.demoSource.setUnknown() }
            }
        }
        Spacer(Modifier.height(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DarkButton("차량 스냅샷 읽기 (Vehicle SDK)", height = 72.dp, textStyle = CarditType.LabelLg) { vm.readSnapshot() }
            DarkButton("닫기", height = 72.dp, textStyle = CarditType.LabelLg, contentColor = CarditColors.Gold) { vm.devPanelOpen = false }
        }
        vm.latestSnapshot?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                "주행거리: ${it.odometer ?: "미지원"}   ·   배터리: ${it.batteryPercent?.let { p -> "$p%" } ?: "미지원"}   ·   " +
                    "와이퍼: ${it.wiperOn?.let { on -> if (on) "작동" else "정지" } ?: "미지원"}",
                style = CarditType.BodyLg, color = CarditColors.TextHigh,
            )
        }
    }
}

/** 출연진 역할 직접 입력 */
@Composable
fun RoleEditor(vm: CarditViewModel) {
    var text by remember { mutableStateOf(vm.roles[vm.selectedCast]) }
    OverlayPanel(onDismiss = { vm.roleEditorOpen = false }, width = 720.dp) {
        Text("역할 직접 입력", style = CarditType.HeadlineLg, color = CarditColors.TextHigh)
        Text("${SampleData.participants[vm.selectedCast].name}님의 엔딩 크레딧 배역", style = CarditType.BodyLg, color = CarditColors.TextWarm)
        Spacer(Modifier.height(24.dp))
        BasicTextField(
            text, { text = it.take(12) }, singleLine = true,
            textStyle = CarditType.HeadlineMd.copy(color = CarditColors.TextHigh),
            cursorBrush = SolidColor(CarditColors.Gold),
            modifier = Modifier.fillMaxWidth().background(CarditColors.SurfaceHigh, RoundedCornerShape(18.dp)).padding(24.dp),
        )
        Spacer(Modifier.height(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DarkButton("취소", height = 80.dp) { vm.roleEditorOpen = false }
            GoldButton("적용", height = 80.dp, textStyle = CarditType.HeadlineSm) {
                if (text.isNotBlank()) vm.assignRole(text.trim())
                vm.roleEditorOpen = false
            }
        }
    }
}
