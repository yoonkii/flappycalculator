package com.flappycalculator.domain.engine

import com.flappycalculator.domain.model.Bird
import com.flappycalculator.domain.model.GameConfig
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for PhysicsSystem.
 */
class PhysicsSystemTest {

    private lateinit var physicsSystem: PhysicsSystem
    private lateinit var bird: Bird

    @Before
    fun setup() {
        physicsSystem = PhysicsSystem()
        bird = Bird(
            x = 100f,
            y = 300f,
            velocity = 0f,
            rotation = 0f,
            width = 50f,
            height = 50f
        )
    }

    @Test
    fun `gravity increases velocity over time`() {
        val deltaTime = 0.016f // ~60fps

        val updatedBird = physicsSystem.updateBird(bird, deltaTime)

        assertTrue("Velocity should increase", updatedBird.velocity > 0)
        assertTrue("Y position should increase (fall down)", updatedBird.y > bird.y)
    }

    @Test
    fun `velocity is capped at terminal velocity`() {
        // Apply gravity many times
        var currentBird = bird.copy(velocity = GameConfig.TERMINAL_VELOCITY - 10f)

        repeat(100) {
            currentBird = physicsSystem.updateBird(currentBird, 0.016f)
        }

        assertTrue(
            "Velocity should not exceed terminal velocity",
            currentBird.velocity <= GameConfig.TERMINAL_VELOCITY
        )
    }

    @Test
    fun `flap applies upward velocity`() {
        val flappedBird = physicsSystem.applyFlap(bird)

        assertTrue("Velocity should be negative (upward)", flappedBird.velocity < 0)
        assertEquals("Velocity should equal flap impulse", GameConfig.FLAP_IMPULSE, flappedBird.velocity)
    }

    @Test
    fun `flap rotation tilts bird up`() {
        val flappedBird = physicsSystem.applyFlap(bird)

        assertTrue("Rotation should be negative (tilted up)", flappedBird.rotation < 0)
    }

    @Test
    fun `falling bird tilts down over time`() {
        var currentBird = bird

        // Let bird fall for several frames
        repeat(30) {
            currentBird = physicsSystem.updateBird(currentBird, 0.016f)
        }

        assertTrue("Rotation should be positive (tilted down)", currentBird.rotation > 0)
    }

    @Test
    fun `delta time affects position change`() {
        val smallDelta = 0.008f  // 120fps
        val largeDelta = 0.016f  // 60fps

        val bird1 = physicsSystem.updateBird(bird, smallDelta)
        val bird2 = physicsSystem.updateBird(bird, largeDelta)

        assertTrue("Larger delta should result in larger position change",
            (bird2.y - bird.y) > (bird1.y - bird.y))
    }

    @Test
    fun `calculateFlapHeight returns positive value`() {
        val height = physicsSystem.calculateFlapHeight()

        assertTrue("Flap height should be positive", height > 0)
    }

    @Test
    fun `calculateFallTime returns positive value`() {
        val time = physicsSystem.calculateFallTime(100f)

        assertTrue("Fall time should be positive", time > 0)
    }

    @Test
    fun `calculateFallDistance returns positive value`() {
        val distance = physicsSystem.calculateFallDistance(1f)

        assertTrue("Fall distance should be positive", distance > 0)
    }

    @Test
    fun `zero delta time produces no change`() {
        val updatedBird = physicsSystem.updateBird(bird, 0f)

        assertEquals("Y position should not change", bird.y, updatedBird.y)
        assertEquals("Velocity should not change", bird.velocity, updatedBird.velocity)
    }
}
