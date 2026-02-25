package com.flappycalculator.domain.engine

import android.graphics.RectF
import com.flappycalculator.domain.model.Bird
import com.flappycalculator.domain.model.CollisionResult
import com.flappycalculator.domain.model.Pipe

/**
 * Handles collision detection between game entities.
 * Uses circle-vs-AABB for the bird and AABB for pipes.
 */
class CollisionDetector {

    // Reusable RectF for pipe bounds to avoid allocations in hot path
    private val pipeBounds = RectF()

    /**
     * Check for collisions between bird and environment.
     * Bird uses a circular hitbox; pipes use axis-aligned rectangles.
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
        val cx = bird.x
        val cy = bird.hitboxCenterY
        val r = bird.hitboxRadius

        // Check ceiling collision
        if (cy - r <= 0) {
            return CollisionResult(
                hitObstacle = true,
                hitCeiling = true
            )
        }

        // Check floor collision
        if (cy + r >= screenHeight) {
            return CollisionResult(
                hitObstacle = true,
                hitFloor = true
            )
        }

        // Check pipe collisions (circle vs rect)
        for (pipe in pipes) {
            pipeBounds.set(pipe.getTopPipeBounds(screenHeight))
            if (circleIntersectsRect(cx, cy, r, pipeBounds)) {
                return CollisionResult(hitObstacle = true)
            }

            pipeBounds.set(pipe.getBottomPipeBounds(screenHeight))
            if (circleIntersectsRect(cx, cy, r, pipeBounds)) {
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
     * Circle-vs-AABB intersection test.
     * Finds the closest point on the rectangle to the circle center,
     * then checks if that point is within the circle's radius.
     */
    private fun circleIntersectsRect(cx: Float, cy: Float, r: Float, rect: RectF): Boolean {
        val closestX = cx.coerceIn(rect.left, rect.right)
        val closestY = cy.coerceIn(rect.top, rect.bottom)
        val dx = cx - closestX
        val dy = cy - closestY
        return (dx * dx + dy * dy) <= r * r
    }
}
