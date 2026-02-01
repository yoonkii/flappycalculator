package com.flappycalculator.presentation.screens.game

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flappycalculator.data.repository.ScoreRepository
import com.flappycalculator.domain.engine.GameEngine
import com.flappycalculator.domain.model.GameConfig
import com.flappycalculator.domain.model.GameState
import com.flappycalculator.domain.model.InputFeedback
import com.flappycalculator.util.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel for the game screen.
 * Manages game state, input handling, and the game loop.
 */
class GameViewModel(
    private val scoreRepository: ScoreRepository,
    private val soundManager: SoundManager? = null
) : ViewModel() {

    private val gameEngine = GameEngine()

    private val _uiState = MutableStateFlow(GameUiState.initial())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameLoopJob: Job? = null
    private var lastFrameTimeNanos: Long = 0L
    private var isInitialized = false

    init {
        loadHighScore()
    }

    /**
     * Initialize the game with screen dimensions.
     * Must be called before starting the game.
     */
    fun initialize(size: IntSize) {
        // Already initialized - skip
        if (isInitialized) return

        // Invalid size - skip
        if (size.width <= 0 || size.height <= 0) return

        // Calculate game area height (60% of screen)
        val gameAreaHeight = size.height * GameConfig.GAME_AREA_RATIO

        gameEngine.initialize(size.width.toFloat(), gameAreaHeight)
        isInitialized = true

        // Update UI state with initial game snapshot
        val snapshot = gameEngine.update(0f)
        _uiState.update { current ->
            current.copy(
                bird = snapshot.bird,
                pipes = snapshot.pipes,
                currentProblem = snapshot.currentProblem,
                gameState = snapshot.gameState
            )
        }
    }

    /**
     * Start the game.
     */
    fun startGame() {
        if (!isInitialized) return
        if (_uiState.value.gameState != GameState.READY) return

        gameEngine.start()

        // Update UI state to PLAYING before starting the loop
        _uiState.update { it.copy(gameState = GameState.PLAYING) }

        startGameLoop()
    }

    /**
     * Handle digit input from keypad.
     */
    fun onDigitPressed(digit: Int) {
        val currentInput = _uiState.value.currentInput
        if (currentInput.length < GameConfig.MAX_INPUT_LENGTH) {
            _uiState.update { it.copy(currentInput = currentInput + digit.toString()) }
        }
    }

    /**
     * Handle backspace from keypad.
     */
    fun onBackspacePressed() {
        val currentInput = _uiState.value.currentInput
        if (currentInput.isNotEmpty()) {
            _uiState.update { it.copy(currentInput = currentInput.dropLast(1)) }
        }
    }

    /**
     * Play key press sound.
     */
    fun playKeySound() {
        soundManager?.playKeyPress()
    }

    /**
     * Handle submit from keypad.
     */
    fun onSubmitPressed() {
        val input = _uiState.value.currentInput.toIntOrNull() ?: return

        if (gameEngine.checkAnswer(input)) {
            // Correct answer - play sounds
            soundManager?.playCorrect()
            soundManager?.playFlap()

            gameEngine.onCorrectAnswer()
            _uiState.update {
                it.copy(
                    currentInput = "",
                    inputFeedback = InputFeedback.CORRECT,
                    currentProblem = gameEngine.getCurrentProblem()
                )
            }

            // Clear feedback after short delay
            viewModelScope.launch {
                delay(150)
                _uiState.update { it.copy(inputFeedback = InputFeedback.NONE) }
            }
        } else {
            // Wrong answer - play error sound
            soundManager?.playWrong()

            _uiState.update {
                it.copy(
                    currentInput = "",
                    inputFeedback = InputFeedback.INCORRECT
                )
            }

            // Clear feedback after animation
            viewModelScope.launch {
                delay(300)
                _uiState.update { it.copy(inputFeedback = InputFeedback.NONE) }
            }
        }
    }

    /**
     * Reset and restart the game.
     */
    fun resetGame() {
        gameLoopJob?.cancel()
        gameEngine.reset()
        lastFrameTimeNanos = 0L

        val snapshot = gameEngine.update(0f)
        _uiState.update { current ->
            current.copy(
                bird = snapshot.bird,
                pipes = snapshot.pipes,
                score = 0,
                currentProblem = snapshot.currentProblem,
                currentInput = "",
                gameState = GameState.READY,
                inputFeedback = InputFeedback.NONE,
                scrollSpeed = GameConfig.INITIAL_SCROLL_SPEED,
                isNewHighScore = false
            )
        }
    }

    /**
     * Start the game loop coroutine.
     */
    private fun startGameLoop() {
        gameLoopJob?.cancel()
        lastFrameTimeNanos = System.nanoTime()
        var previousScore = 0

        gameLoopJob = viewModelScope.launch {
            while (isActive && _uiState.value.gameState == GameState.PLAYING) {
                val currentTimeNanos = System.nanoTime()
                val deltaTimeSeconds = if (lastFrameTimeNanos == 0L) {
                    1f / GameConfig.TARGET_FPS
                } else {
                    (currentTimeNanos - lastFrameTimeNanos) / 1_000_000_000f
                }
                lastFrameTimeNanos = currentTimeNanos

                // Update game engine
                val snapshot = gameEngine.update(deltaTimeSeconds)

                // Play score sound when score increases
                if (snapshot.score > previousScore) {
                    soundManager?.playScore()
                    previousScore = snapshot.score
                }

                // Update UI state
                _uiState.update { current ->
                    current.copy(
                        bird = snapshot.bird,
                        pipes = snapshot.pipes,
                        score = snapshot.score,
                        currentProblem = snapshot.currentProblem,
                        gameState = snapshot.gameState,
                        scrollSpeed = snapshot.scrollSpeed
                    )
                }

                // Handle game over
                if (snapshot.gameState == GameState.GAME_OVER) {
                    soundManager?.playHit()
                    handleGameOver(snapshot.score)
                    break
                }

                // Target frame time
                delay(GameConfig.FRAME_TIME_MS)
            }
        }
    }

    /**
     * Handle game over - save score and check for new high score.
     */
    private fun handleGameOver(finalScore: Int) {
        viewModelScope.launch {
            val isNewHigh = scoreRepository.saveScore(finalScore)
            val highScore = scoreRepository.getHighScore()

            _uiState.update {
                it.copy(
                    highScore = highScore,
                    isNewHighScore = isNewHigh
                )
            }
        }
    }

    /**
     * Load the current high score.
     */
    private fun loadHighScore() {
        viewModelScope.launch {
            val highScore = scoreRepository.getHighScore()
            _uiState.update { it.copy(highScore = highScore) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameLoopJob?.cancel()
        soundManager?.release()
    }
}
