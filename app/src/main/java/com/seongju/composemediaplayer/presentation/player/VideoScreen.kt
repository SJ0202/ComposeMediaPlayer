package com.seongju.composemediaplayer.presentation.player

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.seongju.composemediaplayer.common.Constants.ACTION_VIDEO_PAUSE
import com.seongju.composemediaplayer.common.Constants.ACTION_VIDEO_RESUME
import com.seongju.composemediaplayer.common.Constants.ACTION_VIDEO_START
import com.seongju.composemediaplayer.common.service.ServiceHelper
import com.seongju.composemediaplayer.common.service.ServiceManager
import com.seongju.composemediaplayer.presentation.util.KeepScreenOn
import com.seongju.composemediaplayer.presentation.util.hideSystemUI
import com.seongju.composemediaplayer.presentation.util.showSystemUi

@Composable
fun VideoScreen(
    serviceManager: ServiceManager
){
    val tag = "VideoScreen"
    val localContext = LocalContext.current
    val orientation = LocalContext.current.resources.configuration.orientation
    val lifecycle = remember{ mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val lifecycleOwner = LocalLifecycleOwner.current

    KeepScreenOn(screenOnState = true)

    LaunchedEffect(key1 = true) {
        ServiceHelper.startVideoPlay(
            context = localContext,
            action = ACTION_VIDEO_START,
            uri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        )
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle.value = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = lifecycle.value) {
        when(lifecycle.value) {
            Lifecycle.Event.ON_PAUSE -> {
                ServiceHelper.triggerVideoPlay(
                    context = localContext,
                    action = ACTION_VIDEO_PAUSE
                )
            }
            Lifecycle.Event.ON_RESUME -> {
                ServiceHelper.triggerVideoPlay(
                    context = localContext,
                    action = ACTION_VIDEO_RESUME
                )
            }
            else -> Unit
        }
    }

    when(orientation) {
        ORIENTATION_PORTRAIT -> {
            localContext.showSystemUi()
            VideoScreenBody(
                player = serviceManager.playerService.player,
                lifecycle = lifecycle
            )
        }
        ORIENTATION_LANDSCAPE -> {
            localContext.hideSystemUI()
            VideoScreenFullBody(
                player = serviceManager.playerService.player,
                lifecycle = lifecycle
            )
        }
    }
}

@Composable
fun VideoScreenBody(
    player: Player,
    lifecycle: MutableState<Lifecycle.Event>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).also {
                    it.player = player
                }
            },
            update = {
                when(lifecycle.value) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }
                    else -> Unit
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
    player: Player,
    lifecycle: MutableState<Lifecycle.Event>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).also {
                    it.player = player
                }
            },
            update = {
                when(lifecycle.value) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }
                    else -> Unit
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}