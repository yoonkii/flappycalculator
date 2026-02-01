package com.flappycalculator.presentation.screens.game

import com.flappycalculator.domain.model.*

/**
 * UI state for the game screen.
 * Contains all data needed to render the game.
 */
data class GameUiState(
    val bird: Bird,
    val pipes: List<Pipe>,
    val score: Int,
    val highScore: Int,
    val currentProblem: MathProblem,
    val currentInput: String = "",
    val gameState: GameState = GameState.READY,
    val inputFeedback: InputFeedback = InputFeedback.NONE,
    val scrollSpeed: Float = GameConfig.INITIAL_SCROLL_SPEED,
    val isNewHighScore: Boolean = false
) {
    companion object {
        /**
         * Create initial UI state with default values.
         */
        fun initial(): GameUiState {
            return GameUiState(
                bird = Bird(
                    x = 100f,
                    y = 300f,
                    velocity = 0f,
                    rotation = 0f,
                    width = 50f,
                    height = 50f
                ),
                pipes = emptyList(),
                score = 0,
                highScore = 0,
                currentProblem = MathProblem.addition(1, 1),
                currentInput = "",
                gameState = GameState.READY,
                inputFeedback = InputFeedback.NONE,
                scrollSpeed = GameConfig.INITIAL_SCROLL_SPEED,
                isNewHighScore = false
            )
        }
    }
}
