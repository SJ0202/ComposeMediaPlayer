package com.seongju.composemediaplayer.data.player

import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.*
import com.seongju.composemediaplayer.domain.player.PlayerClient
import com.seongju.composemediaplayer.domain.player.PlayerResult
import com.seongju.composemediaplayer.domain.player.PlayerStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class VideoPlayer(
    private val player: Player
): PlayerClient {

    private val tag: String = "VideoPlayer"
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private lateinit var playerListener: Listener

    override fun startPlay(uri: String): Flow<PlayerResult> {
        return callbackFlow {

            playerListener = object : Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, reason)

                    if (playWhenReady) {
                        launch { send(PlayerResult.Status(PlayerStatus.STATE_PLAYING)) }
                    } else {
                        launch { send(PlayerResult.Status(PlayerStatus.STATE_PAUSE)) }
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)

                    when(playbackState) {
                        STATE_ENDED -> {
                            launch { send(PlayerResult.Status(PlayerStatus.STATE_ENDED)) }
                        }
                        STATE_BUFFERING -> {
                            launch { send(PlayerResult.Status(PlayerStatus.STATE_BUFFERING)) }
                        }
                        STATE_IDLE -> {

                        }
                        STATE_READY -> {

                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    launch { send(PlayerResult.Error(message = error.toString()))}
                }

                override fun onPlayerErrorChanged(error: PlaybackException?) {
                    super.onPlayerErrorChanged(error)
                    launch { send(PlayerResult.Error(message = error.toString()))}
                }
            }

            mainThreadHandler.post {
                player.prepare()
                player.addListener(playerListener)
                player.setMediaItem(MediaItem.fromUri(uri))
                player.play()
            }

            awaitClose {
                mainThreadHandler.post{
                    player.removeListener(playerListener)
                    player.release()
                }
            }
        }
    }

    override fun stopPlay() {
        mainThreadHandler.post {
            player.stop()
            player.removeMediaItem(0)
            player.removeListener(playerListener)
        }
    }
}