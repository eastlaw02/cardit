package com.cardit.car.vehicle

import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.api.model.area.WindowArea
import ai.pleos.playground.vehicle.constant.area.WindowValue
import ai.pleos.playground.vehicle.constant.control.WiperState
import android.content.Context

/**
 * Pleos Vehicle SDK 로 스냅샷을 읽는다.
 *
 * Vehicle SDK 는 IPC 기반이라 결과가 onComplete / onFailed 로 비동기 전달된다.
 * 실패·타임아웃은 모두 `null`(미지원)로 처리한다.
 *
 * 필요 권한: pleos.car.permission.CAR_DRIVE / CAR_ENERGY / CAR_WINDOWS
 */
class PleosVehicleSnapshotReader(context: Context) : VehicleSnapshotReader {

    private val vehicle = Vehicle(context.applicationContext).apply { initialize() }

    override suspend fun read(): VehicleSnapshot = VehicleSnapshot(
        odometer = pleosAwait("odometer") { ok, fail ->
            vehicle.getOdometer().getCurrentDistanceDriven(ok, fail)
        },
        batteryPercent = pleosAwait("battery") { ok, fail ->
            vehicle.getEvBattery().getEvBatteryStateOfCharge(ok, fail)
        },
        wiperOn = pleosAwait<WiperState?>("wiper") { ok, fail ->
            vehicle.getWiper().getCurrentWiperState(WindowArea(WindowValue.FRONT_WINDSHIELD), ok, fail)
        }?.let { it == WiperState.ON },
    )

    override fun close() {
        vehicle.release()
    }
}
