package com.cardit.car.playback

import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState

/**
 * 상영 상태를 표준 MediaSession 으로 차량 시스템에 노출한다 (개발계획서 6).
 * 스티어링 휠 미디어 버튼 등 시스템의 재생/일시정지 요청도 받는다.
 * 재생 허용 여부는 콜백 쪽(ViewModel)에서 게이트로 다시 판정한다.
 */
class ScreeningSession(
    context: Context,
    onPlay: () -> Unit,
    onPause: () -> Unit,
) : AutoCloseable {

    private val session = MediaSession(context.applicationContext, "CarditScreening").apply {
        setCallback(object : MediaSession.Callback() {
            override fun onPlay() = onPlay()
            override fun onPause() = onPause()
        })
    }

    fun update(title: String, subtitle: String, active: Boolean, playing: Boolean, positionMs: Long, durationMs: Long) {
        if (!active) {
            if (session.isActive) {
                session.setPlaybackState(state(PlaybackState.STATE_STOPPED, 0L, 0f))
                session.isActive = false
            }
            return
        }
        session.setMetadata(
            MediaMetadata.Builder()
                .putString(MediaMetadata.METADATA_KEY_TITLE, title)
                .putString(MediaMetadata.METADATA_KEY_ARTIST, subtitle)
                .putLong(MediaMetadata.METADATA_KEY_DURATION, durationMs)
                .build(),
        )
        session.setPlaybackState(
            state(if (playing) PlaybackState.STATE_PLAYING else PlaybackState.STATE_PAUSED, positionMs, if (playing) 1f else 0f),
        )
        session.isActive = true
    }

    private fun state(state: Int, positionMs: Long, speed: Float) = PlaybackState.Builder()
        .setActions(PlaybackState.ACTION_PLAY or PlaybackState.ACTION_PAUSE or PlaybackState.ACTION_PLAY_PAUSE)
        .setState(state, positionMs, speed)
        .build()

    override fun close() {
        session.isActive = false
        session.release()
    }
}
