package com.cardit.car.vehicle

import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.api.model.area.SeatArea
import ai.pleos.playground.vehicle.api.model.capability.AreaBounds
import android.content.Context
import kotlin.math.roundToInt

/** 상영 중 조명 연출 상태 */
enum class CinemaLight {
    /** 상영 중이 아님 (원래 밝기) */
    OFF,
    /** 차량 앰비언트 조명을 시네마 밝기로 제어 중 */
    VEHICLE,
    /** 차량 미지원/권한 없음 → 화면 안 조명 효과로 대체 */
    SCREEN,
}

/**
 * 실내 앰비언트 조명 연출 (개발계획서 6).
 * 상영 시작 시 밝기를 낮추고, 상영이 끝나거나 중단되면 원래 밝기로 즉시 되돌린다.
 */
interface CabinLightController : AutoCloseable {
    /** 시네마 밝기로 전환. 한 구역이라도 차량 조명을 제어했으면 true */
    suspend fun enterCinema(): Boolean

    /** [enterCinema] 이전 밝기로 복원 */
    suspend fun restore()
}

/** 조명을 제어할 수 없는 환경 (일반 기기 / 시연 신호) → 항상 화면 효과로 대체 */
object UnsupportedCabinLight : CabinLightController {
    override suspend fun enterCinema() = false
    override suspend fun restore() = Unit
    override fun close() = Unit
}

/** 밝기 범위 안에서 시네마 밝기(하위 20%)를 고른다. 최소값보다 낮아지지 않는다 */
fun cinemaBrightness(min: Int, max: Int, ratio: Float = 0.2f): Int {
    if (max <= min) return min
    return (min + (max - min) * ratio).roundToInt().coerceIn(min, max)
}

/**
 * Pleos Vehicle SDK Light 로 앰비언트 조명 밝기를 제어한다.
 * 필요 권한: pleos.car.permission.CAR_LIGHTS (조회) / CONTROL_CAR_LIGHTS (제어)
 */
class PleosCabinLightController(context: Context) : CabinLightController {

    private val vehicle = Vehicle(context.applicationContext).apply { initialize() }
    private val saved = linkedMapOf<SeatArea, Int>()

    override suspend fun enterCinema(): Boolean {
        val light = vehicle.getLight()
        val bounds = pleosAwait<Map<SeatArea, AreaBounds<Int>>>("ambient capability") { ok, fail ->
            light.checkAmbientLightBrightnessCapabilities({ _, map -> ok(map) }, fail)
        }
        if (bounds.isNullOrEmpty()) return false

        var controlled = false
        for ((area, range) in bounds) {
            val before = pleosAwait<Int?>("ambient get") { ok, fail -> light.getAmbientLightBrightness(area, ok, fail) }
            val target = cinemaBrightness(range.minValue, range.maxValue)
            val applied = pleosAwait<Int>("ambient set") { ok, fail ->
                light.setAmbientLightBrightness(area, target, { _, value -> ok(value) }, fail)
            }
            if (applied != null) {
                controlled = true
                // 이미 시네마 밝기인 상태에서 다시 불려도 처음 밝기를 유지한다
                if (before != null && area !in saved) saved[area] = before
            }
        }
        return controlled
    }

    override suspend fun restore() {
        if (saved.isEmpty()) return
        val light = vehicle.getLight()
        val toRestore = saved.toMap()
        saved.clear()
        for ((area, value) in toRestore) {
            pleosAwait<Int>("ambient restore") { ok, fail ->
                light.setAmbientLightBrightness(area, value, { _, v -> ok(v) }, fail)
            }
        }
    }

    override fun close() {
        vehicle.release()
    }
}
