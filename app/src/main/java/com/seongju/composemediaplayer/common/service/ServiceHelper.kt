package com.seongju.composemediaplayer.common.service

import android.content.Context
import android.content.Intent
import com.seongju.composemediaplayer.common.Constants.PLAYER_URI

object ServiceHelper {

    fun startVideoPlay(context: Context, action: String, uri: String) {
        Intent(context, PlayerService::class.java).apply {
            this.action = action
            this.putExtra(PLAYER_URI, uri)
            context.startService(this)
        }
    }

    fun stopVideoPlay(context: Context, action: String) {
        Intent(context, PlayerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}