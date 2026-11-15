package com.cardit.car.signal

import kotlinx.coroutines.flow.StateFlow

/**
 * 주행 상태 판정에 쓰이는 원시 신호 (AAOS CarUxRestrictions 기반).
 *
 * 값이 `null` 이면 "판정 불가"(콜백 미수신, 연결 끊김, 초기 상태)이며,
 * 게이트는 이를 주행 중으로 간주한다 (fail-safe).
 */
data class UxSignal(
    /** CarUxRestrictions.isRequiresDistractionOptimization() */
    val requiresDistractionOptimization: Boolean,
    /** activeRestrictions 에 UX_RESTRICTIONS_NO_VIDEO 포함 여부 */
    val noVideo: Boolean,
)

/**
 * ① 신호 계층. 구현체만 바꾸면 실차 / 에뮬레이터 / 시연 모드를 전환할 수 있다.
 * - [CarUxDrivingSignalSource]: AAOS 표준 CarUxRestrictionsManager
 * - [FakeDrivingSignalSource]: 시연용 가짜 신호
 */
interface DrivingSignalSource {
    val signal: StateFlow<UxSignal?>
    fun start()
    fun stop()
}
