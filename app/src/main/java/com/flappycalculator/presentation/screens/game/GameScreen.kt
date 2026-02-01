package com.flappycalculator.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flappycalculator.data.local.ScorePreferences
import com.flappycalculator.data.repository.ScoreRepository
import com.flappycalculator.domain.model.GameState
import com.flappycalculator.presentation.components.game.GameCanvas
import com.flappycalculator.presentation.components.ui.*
import com.flappycalculator.presentation.theme.KeypadBackground
import com.flappycalculator.util.SoundManager

/**
 * Main game screen composable.
 * Handles the 60/40 split between game view and keypad.
 */
@Composable
fun GameScreen(
    onGameOver: (score: Int, highScore: Int, isNewHighScore: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Create ViewModel with dependencies
    val viewModel = remember {
        val prefs = ScorePreferences(context)
        val repo = ScoreRepository(prefs)
        val soundManager = SoundManager(context)
        GameViewModel(repo, soundManager)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Track screen size for initialization
    var screenSize by remember { mutableStateOf(IntSize.Zero) }

    // Initialize game engine when size is known
    LaunchedEffect(screenSize) {
        if (screenSize.width > 0 && screenSize.height > 0) {
            viewModel.initialize(screenSize)
        }
    }

    // Handle game over navigation
    LaunchedEffect(uiState.gameState) {
        if (uiState.gameState == GameState.GAME_OVER) {
            kotlinx.coroutines.delay(500) // Brief pause before transition
            onGameOver(uiState.score, uiState.highScore, uiState.isNewHighScore)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { screenSize = it }
    ) {
        // Top 60%: Game view
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        if (uiState.gameState == GameState.READY) {
                            viewModel.startGame()
                        }
                    }
                )
        ) {
            // Game canvas
            GameCanvas(
                bird = uiState.bird,
                pipes = uiState.pipes,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay UI elements
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Score display (top left)
                ScoreDisplay(
                    score = uiState.score,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Math problem (centered)
                MathProblemDisplay(
                    problem = uiState.currentProblem,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // "Tap to start" message when ready
                if (uiState.gameState == GameState.READY) {
                    Spacer(modifier = Modifier.weight(1f))
                    TapToStartOverlay(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Bottom 40%: Input area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(KeypadBackground)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Answer input display
            AnswerInput(
                currentInput = uiState.currentInput,
                feedback = uiState.inputFeedback,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Numeric keypad
            NumericKeypad(
                onDigit = viewModel::onDigitPressed,
                onBackspace = viewModel::onBackspacePressed,
                onSubmit = viewModel::onSubmitPressed,
                onKeySound = viewModel::playKeySound,
                enabled = uiState.gameState == GameState.PLAYING,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TapToStartOverlay(
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Text(
        text = "Tap to Start",
        modifier = modifier
            .background(
                color = com.flappycalculator.presentation.theme.ProblemBackground,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        color = com.flappycalculator.presentation.theme.ProblemText,
        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
    )
}
