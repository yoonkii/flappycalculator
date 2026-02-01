package com.flappycalculator.domain.engine

import com.flappycalculator.domain.model.Bird
import com.flappycalculator.domain.model.GameConfig

/**
 * Handles physics calculations for the game.
 * Applies gravity, flap impulse, and velocity updates.
 */
class PhysicsSystem {

    /**
     * Update bird physics for one frame.
     *
     * @param bird Current bird state
     * @param deltaTime Time elapsed since last frame in seconds
     * @return Updated bird with new position, velocity, and rotation
     */
    fun updateBird(bird: Bird, deltaTime: Float): Bird {
        // Apply gravity and update position
        val afterGravity = bird.applyGravity(
            gravity = GameConfig.GRAVITY,
            deltaTime = deltaTime,
            terminalVelocity = GameConfig.TERMINAL_VELOCITY
        )

        // Update visual rotation based on velocity
        return afterGravity.updateRotation()
    }

    /**
     * Apply flap impulse to bird.
     * Called when player submits correct answer.
     *
     * @param bird Current bird state
     * @return Updated bird with flap velocity applied
     */
    fun applyFlap(bird: Bird): Bird {
        return bird.flap(GameConfig.FLAP_IMPULSE)
    }

    /**
     * Calculate the time for bird to fall a certain distance.
     * Useful for difficulty tuning.
     *
     * @param distance Distance to fall in pixels
     * @return Approximate time in seconds
     */
    fun calculateFallTime(distance: Float): Float {
        // Using kinematic equation: d = 0.5 * g * t^2
        // Solving for t: t = sqrt(2d/g)
        return kotlin.math.sqrt(2 * distance / GameConfig.GRAVITY)
    }

    /**
     * Calculate the distance bird will fall in a given time.
     * Useful for difficulty tuning.
     *
     * @param time Time in seconds
     * @return Distance in pixels (ignoring terminal velocity cap)
     */
    fun calculateFallDistance(time: Float): Float {
        // Using kinematic equation: d = 0.5 * g * t^2
        return 0.5f * GameConfig.GRAVITY * time * time
    }

    /**
     * Calculate the height bird will reach after a flap.
     * Useful for difficulty tuning.
     *
     * @return Maximum height gain from a single flap
     */
    fun calculateFlapHeight(): Float {
        // Using kinematic equation: h = v^2 / (2g)
        // Flap impulse is negative (upward), so we use absolute value
        val impulse = kotlin.math.abs(GameConfig.FLAP_IMPULSE)
        return (impulse * impulse) / (2 * GameConfig.GRAVITY)
    }
}
