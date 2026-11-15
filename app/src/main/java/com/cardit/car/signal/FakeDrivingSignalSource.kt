package com.cardit.car.signal

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 시연용 신호. 화면 버튼으로 주행/주차/판정불가 상태를 바꿀 수 있다. */
class FakeDrivingSignalSource : DrivingSignalSource {

    private val _signal = MutableStateFlow<UxSignal?>(null)
    override val signal: StateFlow<UxSignal?> = _signal.asStateFlow()

    override fun start() {
        if (_signal.value == null) setParked()
    }

    override fun stop() {
        _signal.value = null
    }

    fun setParked() {
        _signal.value = UxSignal(requiresDistractionOptimization = false, noVideo = false)
    }

    fun setDriving() {
        _signal.value = UxSignal(requiresDistractionOptimization = true, noVideo = true)
    }

    fun setUnknown() {
        _signal.value = null
    }
}
