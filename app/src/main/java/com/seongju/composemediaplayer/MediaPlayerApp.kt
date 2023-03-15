package com.seongju.composemediaplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MediaPlayerApp: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}