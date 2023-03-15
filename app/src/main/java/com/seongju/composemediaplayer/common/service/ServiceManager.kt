package com.seongju.composemediaplayer.common.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

class ServiceManager(
    private val context: Context
) {

    private val tag = "ServiceManager"

    lateinit var playerService: PlayerService
        private set

    private lateinit var playerServiceConnection: ServiceConnection

    init {
        initServiceConnection()
    }

    fun startPlayerService() {
        if (!::playerService.isInitialized) {
            val intent = Intent(context, PlayerService::class.java)
            context.bindService(intent, playerServiceConnection, Context.BIND_AUTO_CREATE)
        } else {
            Log.e(tag, "Player Service 가 초기화 되어있습니다.")
        }
    }

    fun stopPlayerService() {
        if (::playerService.isInitialized) {
            context.unbindService(playerServiceConnection)
            Log.d(tag, "Player Service 종료에 성공하였습니다.")
        } else {
            Log.e(tag, "Player Service 종료에 실패하였습니다.")
        }
    }

    /**
     * Set Service Connections
     */
    private fun initServiceConnection() {
        playerServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val getPlayerService = service as PlayerService.LocalBinder
                playerService = getPlayerService.getPlayerService()
                Log.d(tag, "Player Service 실행에 성공하였습니다.")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.e(tag, "Player Service 가 비정상적으로 종료되었습니다.")
            }

        }
    }

}