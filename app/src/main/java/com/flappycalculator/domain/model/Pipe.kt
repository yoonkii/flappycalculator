package com.flappycalculator.domain.model

import android.graphics.RectF

/**
 * Represents a pipe obstacle with a gap for the bird to fly through.
 * Immutable data class - use copy() to create modified instances.
 */
data class Pipe(
    val x: Float,                    // Horizontal position (left edge)
    val gapCenterY: Float,           // Y position of gap center
    val gapHeight: Float,            // Height of the gap
    val width: Float,                // Pipe width
    val scored: Boolean = false,     // Whether player has passed this pipe
    val isRedBar: Boolean = true     // Stock bar color (red=bearish, blue=bullish)
) {
    /**
     * Get the bounds of the top pipe section (above the gap).
     *
     * @param screenHeight Total height of the game area
     * @return RectF for collision detection
     */
    fun getTopPipeBounds(screenHeight: Float): RectF {
        val gapTop = gapCenterY - gapHeight / 2
        return RectF(
            x,
            0f,
            x + width,
            gapTop
        )
    }

    /**
     * Get the bounds of the bottom pipe section (below the gap).
     *
     * @param screenHeight Total height of the game area
     * @return RectF for collision detection
     */
    fun getBottomPipeBounds(screenHeight: Float): RectF {
        val gapBottom = gapCenterY + gapHeight / 2
        return RectF(
            x,
            gapBottom,
            x + width,
            screenHeight
        )
    }

    /**
     * Move the pipe left based on scroll speed.
     *
     * @param scrollSpeed Current scroll speed in pixels/sec
     * @param deltaTime Time elapsed since last frame in seconds
     * @return New Pipe instance at updated position
     */
    fun move(scrollSpeed: Float, deltaTime: Float): Pipe {
        return copy(x = x - scrollSpeed * deltaTime)
    }

    /**
     * Check if pipe has scrolled completely off the left edge.
     *
     * @return true if pipe is no longer visible
     */
    fun isOffScreen(): Boolean {
        return x + width < 0
    }

    /**
     * Check if bird has passed this pipe (for scoring).
     * Scores when bird's center passes the pipe's center.
     *
     * @param birdX Bird's X position (center)
     * @return true if bird has passed the pipe's center
     */
    fun hasBeenPassed(birdX: Float): Boolean {
        return birdX > x + width / 2
    }

    /**
     * Mark this pipe as scored.
     *
     * @return New Pipe instance with scored = true
     */
    fun markAsScored(): Pipe {
        return copy(scored = true)
    }

    companion object {
        /**
         * Create a new pipe at the right edge of the screen.
         *
         * @param screenWidth Screen width to position pipe off-screen
         * @param screenHeight Screen height for gap positioning
         * @param birdHeight Bird height to calculate gap size
         * @param isRedBar Whether this is a red (bearish) or blue (bullish) bar
         * @param random Random instance for gap positioning
         * @return New Pipe instance
         */
        fun createOffScreen(
            screenWidth: Float,
            screenHeight: Float,
            birdHeight: Float,
            isRedBar: Boolean = true,
            random: kotlin.random.Random = kotlin.random.Random.Default
        ): Pipe {
            val pipeWidth = screenWidth * GameConfig.PIPE_WIDTH_RATIO
            val gapHeight = birdHeight * GameConfig.PIPE_GAP_MULTIPLIER

            // Calculate valid range for gap center
            val minGapCenter = screenHeight * GameConfig.MIN_PIPE_HEIGHT_RATIO + gapHeight / 2
            val maxGapCenter = screenHeight * (1 - GameConfig.MIN_PIPE_HEIGHT_RATIO) - gapHeight / 2

            // Random gap position within valid range
            val gapCenterY = random.nextFloat() * (maxGapCenter - minGapCenter) + minGapCenter

            return Pipe(
                x = screenWidth,
                gapCenterY = gapCenterY,
                gapHeight = gapHeight,
                width = pipeWidth,
                scored = false,
                isRedBar = isRedBar
            )
        }
    }
}
