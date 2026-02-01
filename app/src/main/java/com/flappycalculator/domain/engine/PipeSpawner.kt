package com.flappycalculator.domain.engine

import com.flappycalculator.domain.model.GameConfig
import com.flappycalculator.domain.model.Pipe
import kotlin.random.Random

/**
 * Manages pipe spawning and lifecycle.
 * Creates new pipes at regular intervals and removes off-screen pipes.
 */
class PipeSpawner(
    private val random: Random = Random.Default
) {
    private var distanceSinceLastPipe = 0f
    private var nextPipeIsRed = true  // Alternates between red and blue bars

    /**
     * Update pipes: move existing pipes, remove off-screen pipes, spawn new ones.
     *
     * @param pipes Current list of pipes
     * @param scrollSpeed Current scroll speed in pixels/sec
     * @param deltaTime Time elapsed since last frame in seconds
     * @param screenWidth Screen width for spawning
     * @param screenHeight Screen height for gap positioning
     * @param birdHeight Bird height for gap size calculation
     * @return Updated list of pipes
     */
    fun updatePipes(
        pipes: List<Pipe>,
        scrollSpeed: Float,
        deltaTime: Float,
        screenWidth: Float,
        screenHeight: Float,
        birdHeight: Float
    ): List<Pipe> {
        // Move all pipes
        val movedPipes = pipes.map { it.move(scrollSpeed, deltaTime) }

        // Remove off-screen pipes
        val visiblePipes = movedPipes.filter { !it.isOffScreen() }.toMutableList()

        // Track distance for spawning
        distanceSinceLastPipe += scrollSpeed * deltaTime

        // Spawn new pipe if needed
        if (shouldSpawnPipe(visiblePipes, screenWidth)) {
            val newPipe = Pipe.createOffScreen(
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                birdHeight = birdHeight,
                isRedBar = nextPipeIsRed,
                random = random
            )
            visiblePipes.add(newPipe)
            distanceSinceLastPipe = 0f
            nextPipeIsRed = !nextPipeIsRed  // Alternate color for next pipe
        }

        return visiblePipes
    }

    /**
     * Determine if a new pipe should be spawned.
     */
    private fun shouldSpawnPipe(pipes: List<Pipe>, screenWidth: Float): Boolean {
        // If no pipes exist, spawn one
        if (pipes.isEmpty()) {
            // Give player some initial space
            return distanceSinceLastPipe >= screenWidth * 0.5f
        }

        // Find the rightmost pipe
        val rightmostPipe = pipes.maxByOrNull { it.x }
            ?: return true

        // Spawn new pipe when rightmost pipe has moved enough
        return rightmostPipe.x <= screenWidth - GameConfig.PIPE_SPACING
    }

    /**
     * Create initial pipe setup for game start.
     *
     * @param screenWidth Screen width
     * @param screenHeight Screen height
     * @param birdHeight Bird height for gap calculation
     * @return List with first pipe positioned off-screen
     */
    fun createInitialPipes(
        screenWidth: Float,
        screenHeight: Float,
        birdHeight: Float
    ): List<Pipe> {
        // Start with no pipes - first pipe spawns quickly after game starts
        distanceSinceLastPipe = screenWidth * 0.45f  // First pipe appears after ~0.5 sec at 100px/s
        return emptyList()
    }

    /**
     * Reset spawner state.
     * Call when starting a new game.
     */
    fun reset() {
        distanceSinceLastPipe = 0f
        nextPipeIsRed = true  // Start with red bar
    }
}
