package com.example.composesokobantemplatev2

import android.content.Context
import androidx.core.content.edit

private const val PREF_NAME = "sokoban_scores"

fun getBestScore(context: Context, level: Int): Int {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return prefs.getInt("best_$level", Int.MAX_VALUE) // default = veľké číslo
}

fun saveBestScore(context: Context, level: Int, moves: Int) {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val currentBest = getBestScore(context, level)

    if (moves < currentBest) {
        prefs.edit { putInt("best_$level", moves) }
    }
}