package com.example.battlebucks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.battlebucks.model.RankedPlayer
import com.example.battlebucks.ui.theme.BattleBucksTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModelProvider
import com.example.battlebucks.model.Player
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val players = listOf(
            Player("1", "Anush",200),
            Player("2", "Ronit",101),
            Player("3", "Priya",300),
            Player("4", "Aman",99),
            Player("5", "Neha",90)
        )
        val viewModel = ViewModelProvider(this, LeaderboardViewModelFactory(players))[LeaderboardViewModel::class.java]
        setContent {
            BattleBucksTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    LeaderboardScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel,modifier: Modifier = Modifier) {
    val leaderboard by viewModel.leaderboard.collectAsState()

    LazyColumn(modifier = modifier) {
        items(
            items = leaderboard,
            key = { it.playerId }
        ) { player ->
            LeaderboardRow(player,modifier = Modifier.animateItem() )
        }
    }
}
@Composable
fun LeaderboardRow(
    player: RankedPlayer,
    modifier: Modifier = Modifier
) {

    var previousScore by remember { mutableIntStateOf(player.score) }
    var previousRank by remember { mutableIntStateOf(player.rank) }

    val scoreIncreased = player.score > previousScore
    val rankChanged = player.rank != previousRank
    val animatedScore by animateIntAsState(
        targetValue = player.score,
        label = "scoreAnim"
    )
    LaunchedEffect(player.score, player.rank) {
        delay(500)
        previousScore = player.score
        previousRank = player.rank
    }
    val backgroundColor by animateColorAsState(
        targetValue = if (scoreIncreased)
            Color.Red.copy(alpha = 0.2f)
        else
            Color.Transparent,
        animationSpec = tween(600),
        label = "bgAnim"
    )
    val scale by animateFloatAsState(
        targetValue = if (scoreIncreased) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "scaleAnim"
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(backgroundColor)
            .padding(12.dp)
    ) {

        Text(
            text = "#${player.rank}",
            modifier = Modifier.width(40.dp),
            fontWeight = if (rankChanged) FontWeight.Bold else FontWeight.Normal
        )

        Text(
            text = player.username,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = animatedScore.toString(),
            fontWeight = FontWeight.Bold
        )
    }
}