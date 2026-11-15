package com.cardit.car.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.AirlineSeatReclineExtra
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.LocalMovies
import androidx.compose.material.icons.rounded.MovieFilter
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Theaters
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.ViewTimeline
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cardit.car.Screen
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditType

private data class RailItem(val label: String, val icon: ImageVector, val screen: Screen)

private val railItems = listOf(
    RailItem("보관함", Icons.Rounded.LocalMovies, Screen.HOME),
    RailItem("새여행", Icons.Rounded.AddCircle, Screen.NEW_TRIP),
    RailItem("스타일", Icons.Rounded.AutoAwesome, Screen.STYLE),
    RailItem("출연진", Icons.Rounded.Groups, Screen.CAST),
    RailItem("타임라인", Icons.Rounded.ViewTimeline, Screen.TIMELINE),
)

/** 좌측 레일 + 상단 바가 있는 제작/보관 화면 공통 틀 */
@Composable
fun RailScaffold(
    current: Screen,
    onNavigate: (Screen) -> Unit,
    onTune: () -> Unit,
    recording: Boolean,
    scrollable: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(Modifier.fillMaxSize().background(CarditColors.Canvas)) {
        SideRail(current, onNavigate, onTune, recording)
        Column(Modifier.weight(1f).fillMaxHeight().cinemaBackground(glowAlpha = 0.06f)) {
            TopBar()
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier)
                    .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 28.dp),
                content = content,
            )
        }
    }
}

@Composable
private fun SideRail(current: Screen, onNavigate: (Screen) -> Unit, onTune: () -> Unit, recording: Boolean) {
    Column(
        Modifier.width(140.dp).fillMaxHeight().background(Color(0xFF14161B)).padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconCircle(
            Icons.Rounded.Theaters, size = 84.dp, container = CarditColors.SurfaceHigh, tint = CarditColors.Gold,
            shape = RoundedCornerShape(22.dp), onClick = { onNavigate(Screen.HOME) },
        )
        Spacer(Modifier.height(18.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) { Box(Modifier.size(width = 10.dp, height = 4.dp).background(CarditColors.TextLow, RoundedCornerShape(2.dp))) }
        }
        Spacer(Modifier.weight(1f))
        Column(verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            railItems.forEach { item ->
                val selected = item.screen == current
                Column(
                    Modifier
                        .pressable { onNavigate(item.screen) }
                        .then(
                            if (selected) Modifier
                                .shadow(20.dp, RoundedCornerShape(20.dp), ambientColor = CarditColors.Gold, spotColor = CarditColors.Gold)
                                .background(CarditColors.Gold, RoundedCornerShape(20.dp))
                            else Modifier,
                        )
                        .size(width = 100.dp, height = 92.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    val c = if (selected) CarditColors.OnGold else CarditColors.TextWarm
                    Icon(item.icon, null, tint = c, modifier = Modifier.size(30.dp))
                    Spacer(Modifier.height(6.dp))
                    Text(item.label, style = CarditType.LabelMd, color = c)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        IconCircle(Icons.Rounded.Tune, size = 64.dp, shape = RoundedCornerShape(16.dp), tint = CarditColors.TextWarm, onClick = onTune)
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier.size(64.dp).background(CarditColors.GoldDeep.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.size(16.dp).background(if (recording) CarditColors.Gold else CarditColors.TextLow, CircleShape))
        }
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().height(100.dp).padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.width(10.dp).height(34.dp).background(CarditColors.Gold, RoundedCornerShape(3.dp)))
        Spacer(Modifier.width(14.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("CARDIT", style = CarditType.HeadlineLg.copy(letterSpacing = CarditType.LabelSm.letterSpacing), color = CarditColors.TextHigh)
                Spacer(Modifier.width(12.dp))
                Box(Modifier.background(CarditColors.SurfaceHigh, RoundedCornerShape(6.dp)).padding(horizontal = 10.dp, vertical = 2.dp)) {
                    Text("카딧", style = CarditType.LabelMd, color = CarditColors.TextWarm)
                }
            }
            Text("PLEOS CINEMATIC CONSOLE", style = CarditType.LabelMd, color = CarditColors.Gold)
        }
        Spacer(Modifier.width(48.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(4) { Box(Modifier.size(width = 8.dp, height = 16.dp).background(CarditColors.SurfaceHighest, RoundedCornerShape(2.dp))) }
        }
        Spacer(Modifier.weight(1f))
        Pill("CINEMA SOUND", icon = Icons.Rounded.MovieFilter, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 48.dp)
        Spacer(Modifier.width(20.dp))
        Pill("LOUNGE MODE", icon = Icons.Rounded.AirlineSeatReclineExtra, container = CarditColors.SurfaceMid, content = CarditColors.TextWarm, height = 48.dp)
        Spacer(Modifier.width(20.dp))
        IconCircle(Icons.Rounded.Person, size = 44.dp, container = CarditColors.GoldSoft, tint = CarditColors.OnGold)
    }
}
