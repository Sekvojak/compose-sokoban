package com.example.composesokobantemplatev2

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composesokobantemplatev2.ui.GameScreen
import com.example.composesokobantemplatev2.ui.LevelSelectScreen

@Composable
fun AppNav() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "levels") {

        composable("levels") {
            LevelSelectScreen { selected ->
                nav.navigate("game/$selected")
            }
        }
        // herná obrazovka pre konkrétny level
        composable("game/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0

            GameScreen(
                levelIndexStart = id,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
