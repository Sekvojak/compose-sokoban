package com.example.composesokobantemplatev2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composesokobantemplatev2.clearProgress
import com.example.composesokobantemplatev2.game.*
import com.example.composesokobantemplatev2.getBestScore
import com.example.composesokobantemplatev2.loadProgress
import com.example.composesokobantemplatev2.saveBestScore
import com.example.composesokobantemplatev2.saveProgress
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    levelIndexStart: Int = 0,   // cislo z levelSelectScreen
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val levels = remember { loadLevels(context) }

    var levelIndex by remember { mutableStateOf(levelIndexStart) }  // level ktory prave hram
    var currentLevel by remember { mutableStateOf(levels[levelIndex]) }
    var level by remember { mutableStateOf(currentLevel.tiles.toMutableList()) } // aktualna mapa
    var playerPos by remember { mutableStateOf(findPlayer(level, currentLevel.width)) }

    var moves by remember { mutableStateOf(0) }
    var bestScore by remember(levelIndex) { mutableStateOf(getBestScore(context, levelIndex)) }


    fun loadCurrentLevel() {
        currentLevel = levels[levelIndex]
        level = currentLevel.tiles.toMutableList()
        playerPos = findPlayer(level, currentLevel.width)
        moves = 0
        bestScore = getBestScore(context, levelIndex)
    }

    // urobi sa raz na zaciatku alebo po kliknuti na iny level
    LaunchedEffect(levelIndexStart) {
        levelIndex = levelIndexStart
        currentLevel = levels[levelIndex]

        val saved = loadProgress(context, levelIndex)
        if (saved != null) {
            level = saved.tiles.toMutableList()
            moves = saved.moves
            playerPos = findPlayer(level, currentLevel.width)
        } else {
            loadCurrentLevel()
        }
    }



    fun resetLevel() {
        clearProgress(context, levelIndex)
        loadCurrentLevel()
    }
    fun completeLevel() {
        saveBestScore(context, levelIndex, moves)
        clearProgress(context, levelIndex)
        levelIndex++
        if (levelIndex >= levels.size) levelIndex = 0
        loadCurrentLevel()
    }

    Column(Modifier.fillMaxSize()) {

        // top bar
        TopAppBar(
            title = {
                Text(
                    text = "Sokoban",
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        // Score bar
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Level: ${levelIndex + 1}")
            Text("Moves: $moves")
            Text("Best: ${if (bestScore == Int.MAX_VALUE) "-" else bestScore}")
        }

        // Game area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val down = awaitPointerEvent().changes.firstOrNull() ?: continue
                            if (!down.pressed) continue

                            val startX = down.position.x
                            val startY = down.position.y
                            var endX = startX
                            var endY = startY

                            do {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull()
                                change?.takeIf { it.pressed }?.let {
                                    endX = it.position.x
                                    endY = it.position.y
                                }
                            } while (event.changes.any { it.pressed })

                            val dx = endX - startX
                            val dy = endY - startY
                            val thr = 50f

                            val dir = when {
                                kotlin.math.abs(dx) > kotlin.math.abs(dy) && dx > thr -> 1 to 0
                                kotlin.math.abs(dx) > kotlin.math.abs(dy) && dx < -thr -> -1 to 0
                                kotlin.math.abs(dy) > kotlin.math.abs(dx) && dy > thr -> 0 to 1
                                kotlin.math.abs(dy) > kotlin.math.abs(dx) && dy < -thr -> 0 to -1
                                else -> null
                            }

                            dir?.let {
                                moveDir(level, playerPos, it, currentLevel.width, currentLevel.height) { newLevel, newPlayer ->
                                    moves++
                                    level = newLevel.toMutableList()
                                    playerPos = newPlayer

                                    saveProgress(context, levelIndex, level, moves)

                                    if (!level.contains(2)) {
                                        completeLevel()
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            GameCanvas(
                levelData = level,
                levelWidth = currentLevel.width,
                levelHeight = currentLevel.height,
                modifier = Modifier.fillMaxSize()
            )
        }


        // Reset button
        Box(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { resetLevel() }) {
                Text("RESET")
            }
        }
    }
}
