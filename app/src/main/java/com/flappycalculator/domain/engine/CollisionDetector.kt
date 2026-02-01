package com.flappycalculator.domain.engine

import android.graphics.RectF
import com.flappycalculator.domain.model.Bird
import com.flappycalculator.domain.model.CollisionResult
import com.flappycalculator.domain.model.Pipe

/**
 * Handles collision detection between game entities.
 * Uses AABB (Axis-Aligned Bounding Box) collision detection.
 */
class CollisionDetector {

    // Reusable RectF objects to avoid allocations in hot path
    private val birdBounds = RectF()
    private val pipeBounds = RectF()

    /**
     * Check for collisions between bird and environment.
     *
     * @param bird Current bird state
     * @param pipes List of active pipes
     * @param screenHeight Height of the game area
     * @return CollisionResult indicating what was hit, if anything
     */
    fun checkCollision(
        bird: Bird,
        pipes: List<Pipe>,
        screenHeight: Float
    ): CollisionResult {
        // Get bird bounds
        val birdRect = bird.getBounds()
        birdBounds.set(birdRect)

        // Check ceiling collision
        if (birdBounds.top <= 0) {
            return CollisionResult(
                hitObstacle = true,
                hitCeiling = true
            )
        }

        // Check floor collision
        if (birdBounds.bottom >= screenHeight) {
            return CollisionResult(
                hitObstacle = true,
                hitFloor = true
            )
        }

        // Check pipe collisions
        for (pipe in pipes) {
            // Check top pipe
            pipeBounds.set(pipe.getTopPipeBounds(screenHeight))
            if (RectF.intersects(birdBounds, pipeBounds)) {
                return CollisionResult(hitObstacle = true)
            }

            // Check bottom pipe
            pipeBounds.set(pipe.getBottomPipeBounds(screenHeight))
            if (RectF.intersects(birdBounds, pipeBounds)) {
                return CollisionResult(hitObstacle = true)
            }
        }

        // No collision
        return CollisionResult(hitObstacle = false)
    }

    /**
     * Check which pipes have been passed for scoring.
     * A pipe is considered passed when bird's X position is greater than pipe's right edge.
     *
     * @param bird Current bird state
     * @param pipes List of active pipes
     * @return Number of newly passed (unscored) pipes
     */
    fun checkScoring(bird: Bird, pipes: List<Pipe>): Int {
        return pipes.count { pipe ->
            !pipe.scored && pipe.hasBeenPassed(bird.x)
        }
    }

    /**
     * Mark passed pipes as scored.
     *
     * @param bird Current bird state
     * @param pipes List of active pipes
     * @return Updated list of pipes with scoring flags set
     */
    fun updateScoredPipes(bird: Bird, pipes: List<Pipe>): List<Pipe> {
        return pipes.map { pipe ->
            if (!pipe.scored && pipe.hasBeenPassed(bird.x)) {
                pipe.markAsScored()
            } else {
                pipe
            }
        }
    }

    /**
     * Simple AABB intersection check.
     * Used for external collision checks if needed.
     *
     * @param rect1 First rectangle
     * @param rect2 Second rectangle
     * @return true if rectangles overlap
     */
    fun checkIntersection(rect1: RectF, rect2: RectF): Boolean {
        return RectF.intersects(rect1, rect2)
    }
}
