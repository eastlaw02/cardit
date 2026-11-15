package com.cardit.car.vehicle

/**
 * 여행 시작/종료 시점에 읽는 차량 데이터 스냅샷.
 * 각 값이 `null` 이면 해당 차량에서 미지원(또는 권한 없음)이며, 그 통계는 크레딧에서 제외한다.
 */
data class VehicleSnapshot(
    /** Odometer.getCurrentDistanceDriven() — 누적 주행거리 */
    val odometer: Float?,
    /** EvBattery.getEvBatteryStateOfCharge() — 배터리 잔량(%) */
    val batteryPercent: Int?,
    /** Wiper.getCurrentWiperState(FRONT_WINDSHIELD) == ON */
    val wiperOn: Boolean?,
    val takenAtMillis: Long = System.currentTimeMillis(),
)

/**
 * 앱이 포그라운드일 때만 호출하는 1회성 읽기.
 * 백그라운드 상시 수집 / Foreground Service 는 사용하지 않는다.
 */
interface VehicleSnapshotReader : AutoCloseable {
    suspend fun read(): VehicleSnapshot
}

/** 시연/에뮬레이터용 고정값 리더. */
class FakeVehicleSnapshotReader(
    private var odometer: Float = 12_345f,
    private var batteryPercent: Int = 80,
) : VehicleSnapshotReader {
    private var reads = 0

    override suspend fun read(): VehicleSnapshot {
        // 시연용으로 와이퍼를 번갈아 켜고 꺼서 강우 구간이 집계되도록 한다
        val snapshot = VehicleSnapshot(odometer, batteryPercent, wiperOn = reads % 4 < 2)
        reads++
        odometer += 57.3f
        batteryPercent = (batteryPercent - 9).coerceAtLeast(0)
        return snapshot
    }

    override fun close() = Unit
}
