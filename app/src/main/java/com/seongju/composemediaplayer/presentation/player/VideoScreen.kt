package com.seongju.composemediaplayer.presentation.player

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.seongju.composemediaplayer.common.Constants.ACTION_VIDEO_START
import com.seongju.composemediaplayer.common.service.ServiceHelper
import com.seongju.composemediaplayer.common.service.ServiceManager

@Composable
fun VideoScreen(
    serviceManager: ServiceManager
){
    val localContext = LocalContext.current
    val orientation = LocalContext.current.resources.configuration.orientation

    LaunchedEffect(key1 = true) {
        ServiceHelper.startVideoPlay(
            context = localContext,
            action = ACTION_VIDEO_START,
            uri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        )
    }

    when(orientation) {
        ORIENTATION_PORTRAIT -> {
            VideoScreenBody(
                player = serviceManager.playerService.player
            )
        }
        ORIENTATION_LANDSCAPE -> {
            VideoScreenFullBody(
                player = serviceManager.playerService.player
            )
        }
    }
}

@Composable
fun VideoScreenBody(
    player: Player
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = {
                context ->
                PlayerView(context).also {
                    it.player = player
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )
    }
}

@Composable
fun VideoScreenFullBody(
    player: Player
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = {
                    context ->
                PlayerView(context).also {
                    it.player = player
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )
    }
}