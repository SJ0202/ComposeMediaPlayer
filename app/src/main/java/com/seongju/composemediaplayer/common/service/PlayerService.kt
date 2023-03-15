package com.seongju.composemediaplayer.common.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.seongju.composemediaplayer.common.Constants.ACTION_VIDEO_START
import com.seongju.composemediaplayer.common.Constants.ACTION_VIDEO_STOP
import com.seongju.composemediaplayer.common.Constants.PLAYER_URI
import com.seongju.composemediaplayer.data.player.VideoPlayer
import com.seongju.composemediaplayer.domain.player.PlayerClient
import com.seongju.composemediaplayer.domain.player.PlayerResult
import com.seongju.composemediaplayer.domain.player.PlayerStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PlayerService: Service() {

    private val tag = "PlayerService"
    private val binder: IBinder = LocalBinder()

    private val serviceVideoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var videoClient: PlayerClient

    lateinit var player: Player
        private set
    var playerState = mutableStateOf(PlayerStatus.STATE_ENDED)
        private set

    private fun startVideoPlay(uri: String) {
        videoClient.startPlay(uri = uri)
            .catch { e -> e.printStackTrace() }
            .onEach { playerResult ->
                when(playerResult) {
                    is PlayerResult.Error -> {
                        Log.e(tag, playerResult.message)
                    }
                    is PlayerResult.Status -> {
                        playerState.value = playerResult.status
                    }
                }
            }.launchIn(serviceVideoScope)
    }

    private fun stopVideoPlay(){
        videoClient.stopPlay()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_VIDEO_START -> {
                val videoUri = intent.getStringExtra(PLAYER_URI)
                if (videoUri == null) {
                    Log.e(tag, "영상 URI 를 받지 못하였습니다.")
                    return START_NOT_STICKY
                }

                if (playerState.value == PlayerStatus.STATE_ENDED) {
                    Log.e(tag, "이미 영상이 재생중 입니다.")
                    return START_NOT_STICKY
                }
                startVideoPlay(videoUri)
            }
            ACTION_VIDEO_STOP -> {
                stopVideoPlay()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(applicationContext)
            .build()
        videoClient = VideoPlayer(
            player = player
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceVideoScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class LocalBinder(): Binder() {
        fun getPlayerService(

        ): PlayerService {
            return this@PlayerService
        }
    }
}