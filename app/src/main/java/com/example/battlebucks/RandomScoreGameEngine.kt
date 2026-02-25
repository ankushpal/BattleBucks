package com.example.battlebucks

import com.example.battlebucks.model.Player
import com.example.battlebucks.model.ScoreUpdate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RandomScoreGameEngine(private val players: List<Player>, private val dispatcher: CoroutineDispatcher = Dispatchers.Default): GameEngine {

    private val update = MutableSharedFlow<ScoreUpdate>()
    override val scoreUpdates: Flow<ScoreUpdate> = update.asSharedFlow()

    private val currentScore = players.associate { it.id to it.score }.toMutableMap()
    private var job: Job? = null


    override fun start() {
        job = CoroutineScope(dispatcher).launch {
            while (isActive) {
                delay((500..2000).random().toLong())

                val randomPlayer = players.random()
                val increment = (1..20).random()

                val newScore = currentScore.getValue(randomPlayer.id) + increment
                currentScore[randomPlayer.id] = newScore

                update.emit(
                    ScoreUpdate(randomPlayer.id, newScore)
                )
            }
        }
    }

    override fun stop() {
        job?.cancel()
    }

}