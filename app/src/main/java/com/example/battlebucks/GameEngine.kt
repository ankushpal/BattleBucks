package com.example.battlebucks

import com.example.battlebucks.model.ScoreUpdate
import kotlinx.coroutines.flow.Flow

interface GameEngine {
    val scoreUpdates: Flow<ScoreUpdate>
    fun start()
    fun stop()
}