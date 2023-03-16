package com.seongju.composemediaplayer.presentation.util

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun KeepScreenOn(
    screenOnState: Boolean
) {
    AndroidView(
        {
            View(it).apply {
                keepScreenOn = screenOnState
            }
        }
    )
}
