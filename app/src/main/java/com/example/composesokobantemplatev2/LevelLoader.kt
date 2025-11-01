package com.example.composesokobantemplatev2.game

import android.content.Context

data class Level(
    val width: Int,
    val height: Int,
    val tiles: List<Int>
)

fun loadLevels(context: Context): List<Level> {
    val text = context.assets.open("levels.txt").bufferedReader().readText()
    val lines = text.lines()

    val levels = mutableListOf<Level>()
    val currentLines = mutableListOf<String>()

    for (line in lines) {

        if (line.trim().startsWith("Level", ignoreCase = true)) {
            if (currentLines.isNotEmpty()) {
                levels.add(parseLevel(currentLines))
                currentLines.clear()
            }
        }
        else if (line.isNotBlank()) {
            currentLines.add(line)
        }
    }

    if (currentLines.isNotEmpty()) {
        levels.add(parseLevel(currentLines))
    }

    println("Loaded ${levels.size} levels")
    levels.forEachIndexed { i, lvl ->
        println("Level $i: ${lvl.width}x${lvl.height}")
    }

    return levels
}

private fun parseLevel(lines: List<String>): Level {
    val height = lines.size
    val width = lines.maxOf { it.length }

    val tiles = mutableListOf<Int>()

    for (line in lines) {
        for (ch in line) {
            val tile = when (ch) {
                '#' -> 1
                ' ' -> 0
                '.' -> 3
                '$' -> 2
                '*' -> 5
                '@' -> 4
                '+' -> 6
                else -> 0
            }
            tiles.add(tile)
        }


        repeat(width - line.length) { tiles.add(0) }
    }

    return Level(width, height, tiles)
}