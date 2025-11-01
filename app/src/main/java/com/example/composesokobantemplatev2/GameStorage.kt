package com.example.composesokobantemplatev2

import android.content.Context
import androidx.core.content.edit

private const val PREF = "sokoban_progress"

fun saveProgress(context: Context, levelIndex: Int, tiles: List<Int>, moves: Int) {
    val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    prefs.edit {
        putString("tiles_$levelIndex", tiles.joinToString(",")) // mapa
        putInt("moves_$levelIndex", moves) // tahy hraca
    }
}

data class SavedProgress(val tiles: List<Int>, val moves: Int)

fun loadProgress(context: Context, levelIndex: Int): SavedProgress? {
    val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    val tilesStr = prefs.getString("tiles_$levelIndex", null) ?: return null
    val moves = prefs.getInt("moves_$levelIndex", 0)

    val tiles = tilesStr.split(",").map { it.toInt() }
    return SavedProgress(tiles, moves)
}

fun clearProgress(context: Context, levelIndex: Int) {
    val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    prefs.edit {
        remove("tiles_$levelIndex")
        remove("moves_$levelIndex")
    }
}

fun hasProgress(context: Context, levelIndex: Int): Boolean {
    val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    return prefs.contains("tiles_$levelIndex")
}