package com.cardit.car.vehicle

import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

private const val TAG = "PleosVehicle"
private const val TIMEOUT_MS = 3_000L

/**
 * Vehicle SDK 의 onComplete / onFailed 콜백 호출을 suspend 로 감싼다.
 * 실패·예외·타임아웃은 모두 `null`(미지원 또는 권한 없음)로 돌려준다.
 */
internal suspend fun <T> pleosAwait(
    label: String,
    call: (onComplete: (T) -> Unit, onFailed: (Exception) -> Unit) -> Unit,
): T? = withTimeoutOrNull(TIMEOUT_MS) {
    suspendCancellableCoroutine { cont ->
        runCatching {
            call(
                { value -> if (cont.isActive) cont.resume(value) },
                { e ->
                    Log.w(TAG, "$label unavailable: ${e.message}")
                    if (cont.isActive) cont.resume(null)
                },
            )
        }.onFailure { e ->
            Log.w(TAG, "$label call failed", e)
            if (cont.isActive) cont.resume(null)
        }
    }
}
