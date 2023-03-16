package com.seongju.composemediaplayer.domain.player

sealed interface PlayerResult{
    data class Status(val status: PlayerStatus): PlayerResult
    data class Error(val message: String): PlayerResult
}