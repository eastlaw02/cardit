package com.cardit.car.signal

import android.car.Car
import android.car.drivingstate.CarUxRestrictions
import android.car.drivingstate.CarUxRestrictionsManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * AAOS 표준 [CarUxRestrictionsManager] 로 주행 제한 상태를 받는다.
 * 연결 실패/끊김 시 신호를 `null` 로 되돌려 게이트가 재생을 막도록 한다.
 */
class CarUxDrivingSignalSource(private val context: Context) : DrivingSignalSource {

    private val _signal = MutableStateFlow<UxSignal?>(null)
    override val signal: StateFlow<UxSignal?> = _signal.asStateFlow()

    private var car: Car? = null
    private var manager: CarUxRestrictionsManager? = null

    private val listener = CarUxRestrictionsManager.OnUxRestrictionsChangedListener { restrictions ->
        _signal.value = restrictions.toSignal()
    }

    override fun start() {
        if (car != null) return
        car = runCatching {
            Car.createCar(context, null, Car.CAR_WAIT_TIMEOUT_DO_NOT_WAIT) { connectedCar, ready ->
                if (ready) {
                    val m = connectedCar.getCarManager(Car.CAR_UX_RESTRICTION_SERVICE) as? CarUxRestrictionsManager
                    manager = m
                    m?.registerListener(listener)
                    _signal.value = m?.currentCarUxRestrictions?.toSignal()
                } else {
                    manager = null
                    _signal.value = null
                }
            }
        }.onFailure { Log.w(TAG, "Car service unavailable", it) }.getOrNull()
    }

    override fun stop() {
        runCatching { manager?.unregisterListener() }
        manager = null
        runCatching { car?.disconnect() }
        car = null
        _signal.value = null
    }

    private fun CarUxRestrictions.toSignal() = UxSignal(
        requiresDistractionOptimization = isRequiresDistractionOptimization,
        noVideo = (activeRestrictions and CarUxRestrictions.UX_RESTRICTIONS_NO_VIDEO) != 0,
    )

    private companion object {
        const val TAG = "CarUxSignal"
    }
}
