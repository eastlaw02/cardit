package com.cardit.car.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material.icons.rounded.LocalMovies
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.TheaterComedy
import androidx.compose.material.icons.rounded.WifiTethering
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cardit.car.CarditViewModel
import com.cardit.car.R
import com.cardit.car.Screen
import com.cardit.car.formatTime
import com.cardit.car.gate.GateDecision
import com.cardit.car.ui.components.DarkButton
import com.cardit.car.ui.components.IconCircle
import com.cardit.car.ui.components.Pill
import com.cardit.car.ui.components.Scrubber
import com.cardit.car.ui.components.cinemaBackground
import com.cardit.car.ui.components.pressable
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

/**
 * 주행 중 화면 (Safe Glance).
 * 영상·애니메이션·자동 스크롤 없이 정보 3개 + 큰 버튼 1개만 둔다.
 */
@Composable
fun DrivingScreen(vm: CarditViewModel) {
    Column(Modifier.fillMaxSize().background(CarditColors.Canvas).padding(horizontal = 80.dp, vertical = 48.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Pill("DRIVING MODE · SAFE GLANCE", dot = CarditColors.Gold, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 56.dp) { vm.devPanelOpen = true }
            Spacer(Modifier.weight(1f))
            Pill("PLEOS CONNECT", icon = Icons.Rounded.WifiTethering, container = CarditColors.Canvas, content = CarditColors.TextWarm, height = 56.dp, style = CarditType.LabelLg)
            Spacer(Modifier.width(20.dp))
            Pill("쿠키 ${vm.cookiesSaved}개 저장됨", icon = Icons.Rounded.Cookie, container = CarditColors.Canvas, content = CarditColors.Gold, height = 56.dp, style = CarditType.LabelLg)
        }
        Spacer(Modifier.weight(1f))
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Pill("실시간 주행 중", dot = CarditColors.Gold, container = CarditColors.SurfaceMid, content = CarditColors.GoldSoft, height = 52.dp, style = CarditType.LabelLg)
            Spacer(Modifier.height(24.dp))
            Text("여행 중", style = CarditType.DisplayXl, color = CarditColors.TextHigh)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(vm.tripTitle, style = CarditType.DisplayMd.copy(fontWeight = null), color = CarditColors.TextMid)
                Spacer(Modifier.width(16.dp))
                Pill("DAY 1", container = CarditColors.SurfaceHigh, content = CarditColors.TextWarm, height = 34.dp, shape = RoundedCornerShape(6.dp))
            }
        }
        Spacer(Modifier.weight(1.4f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            val km = vm.stats.distanceKm?.let { minOf(it, 126) } ?: 126
            GlanceTile(Icons.Rounded.Place, "주행 누적 거리", "$km", "km", Modifier.weight(1f))
            GlanceTile(Icons.Rounded.AutoAwesome, "자동 큐레이션 · 하이라이트", "${12 + vm.cookiesSaved}", "개", Modifier.weight(1f), valueColor = CarditColors.Gold)
            RecTile(Modifier.weight(1f))
        }
        Spacer(Modifier.height(36.dp))
        Row(
            Modifier.fillMaxWidth(0.75f).align(Alignment.CenterHorizontally)
                .pressable { vm.saveCookie() }
                .shadow(24.dp, RoundedCornerShape(50), ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold)
                .background(CarditColors.Gold, RoundedCornerShape(50))
                .height(140.dp).padding(horizontal = 60.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconCircle(Icons.Rounded.Cookie, size = 72.dp, container = CarditColors.OnGold.copy(alpha = 0.08f), tint = CarditColors.OnGold)
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("지금 이 순간 쿠키로 저장", style = CarditType.DisplayMd.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold), color = CarditColors.OnGold)
                Text("핸들 즐겨찾기(★) 또는 원터치 탭", style = CarditType.LabelMd, color = CarditColors.GoldDeep)
            }
            IconCircle(Icons.Rounded.ArrowForward, size = 72.dp, container = CarditColors.OnGold.copy(alpha = 0.08f), tint = CarditColors.OnGold)
        }
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun GlanceTile(icon: ImageVector, label: String, value: String, unit: String, modifier: Modifier, valueColor: androidx.compose.ui.graphics.Color = CarditColors.TextHigh) {
    Row(
        modifier.height(164.dp).background(CarditColors.SurfaceLow, RoundedCornerShape(20.dp)).padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconCircle(icon, size = 80.dp, container = CarditColors.SurfaceMid, tint = CarditColors.Gold, shape = RoundedCornerShape(14.dp))
        Spacer(Modifier.width(26.dp))
        Column {
            Text(label, style = CarditType.LabelLg, color = CarditColors.TextWarm)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, style = CarditType.DisplayLg, color = valueColor)
                Spacer(Modifier.width(8.dp))
                Text(unit, style = CarditType.HeadlineMd, color = CarditColors.TextWarm, modifier = Modifier.padding(bottom = 6.dp))
            }
        }
    }
}

@Composable
private fun RecTile(modifier: Modifier) {
    Row(
        modifier.height(164.dp).background(CarditColors.SurfaceLow, RoundedCornerShape(20.dp)).padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(80.dp).background(CarditColors.SurfaceMid, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Box(Modifier.size(28.dp).background(CarditColors.Rec, CircleShape))
        }
        Spacer(Modifier.width(26.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("녹음 중", style = CarditType.HeadlineLg, color = CarditColors.TextHigh)
                Spacer(Modifier.width(12.dp))
                Pill("LIVE", container = androidx.compose.ui.graphics.Color(0xFF9A0007), content = CarditColors.TextHigh, height = 30.dp, shape = RoundedCornerShape(8.dp))
            }
            Text("탑승객 4인 동의 완료", style = CarditType.BodyXl, color = CarditColors.TextWarm)
        }
    }
}

/** 상영 차단 화면 — 주행 중이거나 주행 상태를 판정할 수 없을 때(fail-safe) */
@Composable
fun BlockedScreen(vm: CarditViewModel) {
    val unknown = vm.gate == GateDecision.BLOCK_UNKNOWN
    Box(Modifier.fillMaxSize().cinemaBackground(glowAlpha = 0.08f)) {
        Image(
            painterResource(R.drawable.img_blocked_bg), null, contentScale = ContentScale.Crop, alpha = 0.06f,
            modifier = Modifier.fillMaxSize(),
        )
        Column(Modifier.fillMaxSize().padding(48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Pill(if (unknown) "SIGNAL CHECK" else "DRIVE MODE ACTIVE", dot = CarditColors.Gold, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 56.dp) { vm.devPanelOpen = true }
                Spacer(Modifier.weight(1f))
                Pill("ACOUSTIC CINEMA COCKPIT", icon = Icons.Rounded.TheaterComedy, container = CarditColors.SurfaceMid, content = CarditColors.TextHigh, iconTint = CarditColors.Gold, height = 56.dp)
            }
            Spacer(Modifier.weight(1f))
            Box(
                Modifier.size(140.dp).shadow(40.dp, CircleShape, ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold)
                    .background(CarditColors.SurfaceHigh, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.size(76.dp).border(5.dp, CarditColors.Gold, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Pause, null, tint = CarditColors.Gold, modifier = Modifier.size(44.dp))
                }
            }
            Spacer(Modifier.height(40.dp))
            Text(
                if (unknown) "주행 상태를 확인하고 있어요" else "주차 후에 상영할 수 있어요",
                style = CarditType.DisplayLg, color = CarditColors.TextHigh,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                if (unknown) "차량 신호를 받을 때까지 안전을 위해 재생을 멈춰 둡니다.\n주차 상태가 확인되면 이어서 재생돼요."
                else "안전을 위해 주행 중에는 엔딩 크레딧이 재생되지 않습니다. 정차하면\n이어서 재생돼요.",
                style = CarditType.HeadlineSm.copy(fontWeight = null), color = CarditColors.TextMid, textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(48.dp))
            Column(Modifier.width(640.dp).background(CarditColors.SurfaceLow, RoundedCornerShape(24.dp)).padding(26.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LocalMovies, null, tint = CarditColors.Gold, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("${vm.tripTitle} · 엔딩 크레딧", style = CarditType.LabelLg, color = CarditColors.TextHigh, modifier = Modifier.weight(1f))
                    Pill("자동 재개 대기중", container = CarditColors.Gold.copy(alpha = 0.12f), content = CarditColors.Gold, height = 34.dp)
                }
                Spacer(Modifier.height(18.dp))
                Scrubber(vm.positionMs / vm.durationMs.toFloat(), Modifier.fillMaxWidth(), track = 14.dp, thumb = 18.dp)
                Spacer(Modifier.height(14.dp))
                Row {
                    Text("${formatTime(vm.positionMs)} / ${formatTime(vm.durationMs)}에서 일시정지됨", style = CarditType.LabelMd, color = CarditColors.Gold, modifier = Modifier.weight(1f))
                    Text("남은 시간 ${formatTime(vm.durationMs - vm.positionMs)}", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("P   P(주차) 기어 체결 시 화면이 자동으로 전환됩니다", style = CarditType.LabelMd, color = CarditColors.TextLow)
            Spacer(Modifier.weight(1f))
            DarkButton("홈으로", leading = Icons.Rounded.ArrowBack, iconTint = CarditColors.Gold, height = 80.dp, modifier = Modifier.width(232.dp)) { vm.go(Screen.HOME) }
        }
    }
}
