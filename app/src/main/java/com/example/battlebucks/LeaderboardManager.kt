package com.example.battlebucks

import com.example.battlebucks.model.Player
import com.example.battlebucks.model.RankedPlayer
import com.example.battlebucks.model.ScoreUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LeaderboardManager(private  val player: List<Player>, scoreUpdate: Flow<ScoreUpdate>, scope: CoroutineScope) {
    private val currentScore = player.associateBy({ it.id }, {it.score}).toMutableMap()
    private val leaderBoard = MutableStateFlow<List<RankedPlayer>>(emptyList())
    val leaderBoards: StateFlow<List<RankedPlayer>> = leaderBoard

    init {
        scope.launch {
            scoreUpdate.collect { update ->
                currentScore[update.playerId] = update.newScore
                recomputeLeaderboard()
            }
        }
    }

    private fun recomputeLeaderboard(){
        val sorted = player.map {
            it.copy(score = currentScore[it.id] ?:0)
        }.sortedByDescending { it.score }
        val ranked = mutableListOf<RankedPlayer>()
        var lastScore = -1
        var rank = 0
        var position = 0

        sorted.forEach { player ->
            position++
            if(player.score != lastScore){
               rank = position
               lastScore = player.score
            }
            ranked.add(RankedPlayer(
                rank = rank,
                playerId = player.id,
                username = player.username,
                score = player.score
                ))
        }
      leaderBoard.value = ranked
    }
}