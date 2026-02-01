package com.flappycalculator.domain.model

import android.graphics.RectF

/**
 * Represents the bird entity with physics properties.
 * Immutable data class - use copy() to create modified instances.
 */
data class Bird(
    val x: Float,                    // Horizontal position (fixed during gameplay)
    val y: Float,                    // Vertical position (changes with physics)
    val velocity: Float = 0f,        // Vertical velocity (negative = up, positive = down)
    val rotation: Float = 0f,        // Visual rotation in degrees
    val width: Float,                // Bird hitbox width
    val height: Float                // Bird hitbox height
) {
    /**
     * Apply gravity and update position for one frame.
     *
     * @param gravity Gravity acceleration in pixels/sec²
     * @param deltaTime Time elapsed since last frame in seconds
     * @param terminalVelocity Maximum fall speed
     * @return New Bird instance with updated position and velocity
     */
    fun applyGravity(
        gravity: Float,
        deltaTime: Float,
        terminalVelocity: Float
    ): Bird {
        // Apply gravity to velocity, cap at terminal velocity
        val newVelocity = minOf(
            velocity + gravity * deltaTime,
            terminalVelocity
        )

        // Update position based on velocity
        val newY = y + newVelocity * deltaTime

        return copy(
            y = newY,
            velocity = newVelocity
        )
    }

    /**
     * Apply flap impulse (triggered by correct answer).
     *
     * @param flapImpulse Upward velocity to apply (should be negative)
     * @return New Bird instance with flap applied
     */
    fun flap(flapImpulse: Float): Bird {
        return copy(
            velocity = flapImpulse,
            rotation = GameConfig.MAX_ROTATION_UP
        )
    }

    /**
     * Update rotation based on current velocity.
     * Bird tilts down when falling, up when rising.
     *
     * @return New Bird instance with updated rotation
     */
    fun updateRotation(): Bird {
        // Calculate target rotation based on velocity
        val targetRotation = (velocity / GameConfig.TERMINAL_VELOCITY) * GameConfig.MAX_ROTATION_DOWN

        // Smoothly interpolate to target rotation
        val newRotation = lerp(rotation, targetRotation, GameConfig.ROTATION_LERP_FACTOR)

        return copy(rotation = newRotation)
    }

    /**
     * Get the collision bounds rectangle.
     *
     * @return RectF representing the bird's hitbox
     */
    fun getBounds(): RectF {
        return RectF(
            x - width / 2,
            y - height / 2,
            x + width / 2,
            y + height / 2
        )
    }

    /**
     * Check if bird is within screen bounds.
     *
     * @param screenHeight Height of the game area
     * @return true if bird is within bounds
     */
    fun isWithinBounds(screenHeight: Float): Boolean {
        val bounds = getBounds()
        return bounds.top > 0 && bounds.bottom < screenHeight
    }

    companion object {
        /**
         * Create a bird at the starting position.
         *
         * @param screenWidth Screen width for calculating position
         * @param screenHeight Screen height for calculating starting Y
         * @return New Bird instance at starting position
         */
        fun createAtStart(screenWidth: Float, screenHeight: Float): Bird {
            val birdWidth = screenWidth * GameConfig.BIRD_SIZE_RATIO
            val birdHeight = birdWidth * GameConfig.BIRD_ASPECT_RATIO
            val birdX = screenWidth * GameConfig.BIRD_X_POSITION_RATIO
            val birdY = screenHeight / 2  // Start at center

            return Bird(
                x = birdX,
                y = birdY,
                velocity = 0f,
                rotation = 0f,
                width = birdWidth,
                height = birdHeight
            )
        }

        /**
         * Linear interpolation helper.
         */
        private fun lerp(start: Float, end: Float, fraction: Float): Float {
            return start + (end - start) * fraction
        }
    }
}
