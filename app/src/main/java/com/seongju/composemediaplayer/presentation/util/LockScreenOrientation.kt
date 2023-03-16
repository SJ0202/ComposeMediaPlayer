package com.seongju.composemediaplayer.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun LockScreenOrientation(orientation: Int) {
    // ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> 세로 고정
    // ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> 가로 고정
    // ActivityInfo.SCREEN_ORIENTATION_SENSOR   -> 센서 사용
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity()?: return@DisposableEffect onDispose {  }
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}