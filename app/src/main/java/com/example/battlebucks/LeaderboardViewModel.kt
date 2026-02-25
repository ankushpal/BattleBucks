package com.example.battlebucks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.battlebucks.model.Player

class LeaderboardViewModel(
    players: List<Player>
) : ViewModel() {

    private val engine = RandomScoreGameEngine(players)

    private val leaderboardManager = LeaderboardManager(
        player = players,
        scoreUpdate = engine.scoreUpdates,
        scope = viewModelScope
    )
    val leaderboard = leaderboardManager.leaderBoards
    init {
        engine.start()
    }

    override fun onCleared() {
        engine.stop()
    }
}

class LeaderboardViewModelFactory(
    private val players: List<Player>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LeaderboardViewModel(players) as T
    }
}