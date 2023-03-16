package com.seongju.composemediaplayer.domain.player

import kotlinx.coroutines.flow.Flow

interface PlayerClient {

    fun startPlay(uri: String): Flow<PlayerResult>
    fun stopPlay()
    fun pausePlay()
    fun resumePlay()

}