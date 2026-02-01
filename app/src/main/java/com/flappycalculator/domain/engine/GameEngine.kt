package com.flappycalculator.domain.engine

import com.flappycalculator.domain.math.DifficultyTier
import com.flappycalculator.domain.math.MathProblemGenerator
import com.flappycalculator.domain.model.*

/**
 * Core game engine that orchestrates all game systems.
 * Manages game state, physics, collisions, and scoring.
 */
class GameEngine(
    private val physicsSystem: PhysicsSystem = PhysicsSystem(),
    private val collisionDetector: CollisionDetector = CollisionDetector(),
    private val pipeSpawner: PipeSpawner = PipeSpawner(),
    private val mathGenerator: MathProblemGenerator = MathProblemGenerator()
) {
    // Game dimensions (set when game starts)
    private var screenWidth: Float = 0f
    private var screenHeight: Float = 0f

    // Game state
    private var bird: Bird = createDefaultBird()
    private var pipes: List<Pipe> = emptyList()
    private var currentProblem: MathProblem = mathGenerator.generateProblemForScore(0)
    private var score: Int = 0
    private var scrollSpeed: Float = GameConfig.INITIAL_SCROLL_SPEED
    private var gameState: GameState = GameState.READY

    /**
     * Initialize the game engine with screen dimensions.
     * Must be called before starting the game.
     *
     * @param width Screen width in pixels
     * @param height Game area height in pixels (top 60% of screen)
     */
    fun initialize(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
        reset()
    }

    /**
     * Update the game state for one frame.
     * Called every frame (target 60fps).
     *
     * @param deltaTime Time elapsed since last frame in seconds
     * @return Current game snapshot for rendering
     */
    fun update(deltaTime: Float): GameSnapshot {
        if (gameState != GameState.PLAYING) {
            return createSnapshot()
        }

        // Cap delta time to prevent physics explosion on frame drops
        val cappedDelta = deltaTime.coerceAtMost(GameConfig.MAX_DELTA_TIME)

        // Update bird physics
        bird = physicsSystem.updateBird(bird, cappedDelta)

        // Update pipes
        pipes = pipeSpawner.updatePipes(
            pipes = pipes,
            scrollSpeed = scrollSpeed,
            deltaTime = cappedDelta,
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            birdHeight = bird.height
        )

        // Check collisions
        val collision = collisionDetector.checkCollision(bird, pipes, screenHeight)
        if (collision.hitObstacle) {
            gameState = GameState.GAME_OVER
            return createSnapshot()
        }

        // Check and update scoring
        val newlyScored = collisionDetector.checkScoring(bird, pipes)
        if (newlyScored > 0) {
            score += newlyScored
            pipes = collisionDetector.updateScoredPipes(bird, pipes)

            // Increase scroll speed
            scrollSpeed = minOf(
                GameConfig.MAX_SCROLL_SPEED,
                scrollSpeed + GameConfig.SPEED_INCREMENT_PER_SCORE * newlyScored
            )
        }

        return createSnapshot()
    }

    /**
     * Check if user's answer is correct.
     *
     * @param input User's answer
     * @return true if correct
     */
    fun checkAnswer(input: Int): Boolean {
        return currentProblem.isCorrect(input)
    }

    /**
     * Handle correct answer submission.
     * Triggers flap and generates new problem.
     */
    fun onCorrectAnswer() {
        // Apply flap
        bird = physicsSystem.applyFlap(bird)

        // Generate new problem based on current score
        val tier = DifficultyTier.forScore(score)
        currentProblem = mathGenerator.generateProblem(tier)
    }

    /**
     * Start the game.
     * Transitions from READY to PLAYING state.
     */
    fun start() {
        if (gameState == GameState.READY) {
            gameState = GameState.PLAYING
        }
    }

    /**
     * Reset the game to initial state.
     * Call when retrying or starting fresh.
     */
    fun reset() {
        bird = if (screenWidth > 0 && screenHeight > 0) {
            Bird.createAtStart(screenWidth, screenHeight)
        } else {
            createDefaultBird()
        }

        // Reset spawner before creating initial pipes to preserve initial distance setting
        pipeSpawner.reset()
        mathGenerator.reset()

        pipes = pipeSpawner.createInitialPipes(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            birdHeight = bird.height
        )

        currentProblem = mathGenerator.generateProblemForScore(0)
        score = 0
        scrollSpeed = GameConfig.INITIAL_SCROLL_SPEED
        gameState = GameState.READY
    }

    /**
     * Get the current game state.
     */
    fun getGameState(): GameState = gameState

    /**
     * Get the current score.
     */
    fun getScore(): Int = score

    /**
     * Get the current math problem.
     */
    fun getCurrentProblem(): MathProblem = currentProblem

    /**
     * Create a snapshot of the current game state.
     */
    private fun createSnapshot(): GameSnapshot {
        return GameSnapshot(
            bird = bird,
            pipes = pipes,
            score = score,
            currentProblem = currentProblem,
            gameState = gameState,
            scrollSpeed = scrollSpeed
        )
    }

    /**
     * Create a default bird for initialization before screen dimensions are known.
     */
    private fun createDefaultBird(): Bird {
        return Bird(
            x = 100f,
            y = 300f,
            velocity = 0f,
            rotation = 0f,
            width = 50f,
            height = 50f
        )
    }
}
