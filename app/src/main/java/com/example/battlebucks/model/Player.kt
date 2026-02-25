package com.example.battlebucks.model

data class Player(val id: String,
                  val username: String,
                  val score: Int)

data class ScoreUpdate(
    val playerId: String,
    val newScore: Int
)
