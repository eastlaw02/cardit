package com.cardit.car

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.cardit.car.ui.components.ScreenAmbientGlow
import com.cardit.car.vehicle.CinemaLight
import com.cardit.car.ui.screens.BlockedScreen
import com.cardit.car.ui.screens.CastScreen
import com.cardit.car.ui.screens.CookieScreen
import com.cardit.car.ui.screens.CreditsScreen
import com.cardit.car.ui.screens.DevPanel
import com.cardit.car.ui.screens.DrivingScreen
import com.cardit.car.ui.screens.HomeScreen
import com.cardit.car.ui.screens.NewTripScreen
import com.cardit.car.ui.screens.PreShowScreen
import com.cardit.car.ui.screens.RoleEditor
import com.cardit.car.ui.screens.StyleScreen
import com.cardit.car.ui.screens.TimelineScreen
import com.cardit.car.ui.screens.WrapUpScreen
import com.cardit.car.ui.theme.CarditColors
import com.cardit.car.ui.theme.CarditTheme
import com.cardit.car.ui.theme.CarditType
import com.cardit.car.ui.theme.DesignScale

class MainActivity : ComponentActivity() {

    private val vm: CarditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarditTheme {
                BackHandler(enabled = vm.screen != Screen.HOME) { vm.back() }
                DesignScale(Modifier.fillMaxSize().background(CarditColors.Canvas)) {
                    Box(Modifier.fillMaxSize()) {
                        AnimatedContent(
                            targetState = vm.screen,
                            transitionSpec = { fadeIn(tween(280)) togetherWith fadeOut(tween(200)) },
                            label = "screen",
                        ) { screen ->
                            when (screen) {
                                Screen.HOME -> HomeScreen(vm)
                                Screen.NEW_TRIP -> NewTripScreen(vm)
                                Screen.DRIVING -> DrivingScreen(vm)
                                Screen.BLOCKED -> BlockedScreen(vm)
                                Screen.WRAP_UP -> WrapUpScreen(vm)
                                Screen.STYLE -> StyleScreen(vm)
                                Screen.CAST -> CastScreen(vm)
                                Screen.TIMELINE -> TimelineScreen(vm)
                                Screen.PRESHOW -> PreShowScreen(vm)
                                Screen.CREDITS -> CreditsScreen(vm)
                                Screen.COOKIE -> CookieScreen(vm)
                            }
                        }
                        // 차량 조명 미지원 시 화면 안 앰비언트 효과로 대체
                        if (vm.cinemaLight == CinemaLight.SCREEN) ScreenAmbientGlow()
                        // Pleos 토스트 팝업 (하단 24dp 여백)
                        AnimatedVisibility(
                            visible = vm.toast != null,
                            enter = fadeIn() + slideInVertically { it / 2 },
                            exit = fadeOut() + slideOutVertically { it / 2 },
                            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp),
                        ) {
                            Text(
                                vm.toast.orEmpty(),
                                style = CarditType.HeadlineSm,
                                color = CarditColors.TextHigh,
                                modifier = Modifier
                                    .shadow(24.dp, RoundedCornerShape(50))
                                    .background(CarditColors.SurfaceHighest, RoundedCornerShape(50))
                                    .padding(horizontal = 40.dp, vertical = 22.dp),
                            )
                        }
                        if (vm.roleEditorOpen) RoleEditor(vm)
                        if (vm.devPanelOpen) DevPanel(vm)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        vm.onBackground()
    }
}
