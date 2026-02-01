package com.flappycalculator.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.flappycalculator.presentation.screens.game.GameScreen
import com.flappycalculator.presentation.screens.gameover.GameOverScreen
import com.flappycalculator.presentation.screens.title.TitleScreen

/**
 * Navigation routes for the app.
 */
object Routes {
    const val TITLE = "title"
    const val GAME = "game"
    const val GAME_OVER = "game_over/{score}/{highScore}/{isNewHighScore}"

    fun gameOver(score: Int, highScore: Int, isNewHighScore: Boolean): String {
        return "game_over/$score/$highScore/$isNewHighScore"
    }
}

/**
 * Main navigation host for Flappy Calculator.
 */
@Composable
fun FlappyCalculatorNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.TITLE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Title Screen
        composable(Routes.TITLE) {
            TitleScreen(
                onStartGame = {
                    navController.navigate(Routes.GAME) {
                        // Pop title from back stack so back button exits app
                        popUpTo(Routes.TITLE) { inclusive = true }
                    }
                }
            )
        }

        // Game Screen
        composable(Routes.GAME) {
            GameScreen(
                onGameOver = { score, highScore, isNewHighScore ->
                    navController.navigate(Routes.gameOver(score, highScore, isNewHighScore)) {
                        // Pop game from back stack
                        popUpTo(Routes.GAME) { inclusive = true }
                    }
                }
            )
        }

        // Game Over Screen
        composable(
            route = Routes.GAME_OVER,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("highScore") { type = NavType.IntType },
                navArgument("isNewHighScore") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val highScore = backStackEntry.arguments?.getInt("highScore") ?: 0
            val isNewHighScore = backStackEntry.arguments?.getBoolean("isNewHighScore") ?: false

            GameOverScreen(
                score = score,
                highScore = highScore,
                isNewHighScore = isNewHighScore,
                onRetry = {
                    navController.navigate(Routes.GAME) {
                        popUpTo(Routes.TITLE) { inclusive = false }
                    }
                },
                onMenu = {
                    navController.navigate(Routes.TITLE) {
                        popUpTo(Routes.TITLE) { inclusive = true }
                    }
                }
            )
        }
    }
}
