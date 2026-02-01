package com.flappycalculator.domain.engine

import com.flappycalculator.domain.model.Bird
import com.flappycalculator.domain.model.Pipe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for CollisionDetector.
 */
class CollisionDetectorTest {

    private lateinit var collisionDetector: CollisionDetector
    private lateinit var bird: Bird

    @Before
    fun setup() {
        collisionDetector = CollisionDetector()
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
    fun `no collision when bird is in open space`() {
        val pipes = listOf(
            Pipe(
                x = 300f,  // Far from bird
                gapCenterY = 300f,
                gapHeight = 200f,
                width = 60f
            )
        )

        val result = collisionDetector.checkCollision(bird, pipes, 600f)

        assertFalse("Should not detect collision", result.hitObstacle)
    }

    @Test
    fun `collision detected with top pipe`() {
        val pipes = listOf(
            Pipe(
                x = 75f,  // Overlapping with bird
                gapCenterY = 500f,  // Gap below bird
                gapHeight = 100f,
                width = 60f
            )
        )

        val result = collisionDetector.checkCollision(bird, pipes, 600f)

        assertTrue("Should detect collision with top pipe", result.hitObstacle)
    }

    @Test
    fun `collision detected with bottom pipe`() {
        val pipes = listOf(
            Pipe(
                x = 75f,  // Overlapping with bird
                gapCenterY = 100f,  // Gap above bird
                gapHeight = 100f,
                width = 60f
            )
        )

        val result = collisionDetector.checkCollision(bird, pipes, 600f)

        assertTrue("Should detect collision with bottom pipe", result.hitObstacle)
    }

    @Test
    fun `no collision when bird passes through gap`() {
        val pipes = listOf(
            Pipe(
                x = 75f,  // Overlapping with bird horizontally
                gapCenterY = 300f,  // Gap centered on bird
                gapHeight = 200f,  // Large gap
                width = 60f
            )
        )

        val result = collisionDetector.checkCollision(bird, pipes, 600f)

        assertFalse("Should pass through gap without collision", result.hitObstacle)
    }

    @Test
    fun `collision detected with ceiling`() {
        val birdAtTop = bird.copy(y = 20f)  // Bird near top

        val result = collisionDetector.checkCollision(birdAtTop, emptyList(), 600f)

        assertTrue("Should detect ceiling collision", result.hitObstacle)
        assertTrue("Should flag ceiling hit", result.hitCeiling)
    }

    @Test
    fun `collision detected with floor`() {
        val birdAtBottom = bird.copy(y = 580f)  // Bird near bottom

        val result = collisionDetector.checkCollision(birdAtBottom, emptyList(), 600f)

        assertTrue("Should detect floor collision", result.hitObstacle)
        assertTrue("Should flag floor hit", result.hitFloor)
    }

    @Test
    fun `checkScoring returns zero for unscored pipes ahead of bird`() {
        val pipes = listOf(
            Pipe(x = 200f, gapCenterY = 300f, gapHeight = 150f, width = 60f)
        )

        val scored = collisionDetector.checkScoring(bird, pipes)

        assertEquals("Should not score pipes ahead of bird", 0, scored)
    }

    @Test
    fun `checkScoring returns one for passed unscored pipe`() {
        val pipes = listOf(
            Pipe(x = 20f, gapCenterY = 300f, gapHeight = 150f, width = 60f)  // Behind bird
        )

        val scored = collisionDetector.checkScoring(bird, pipes)

        assertEquals("Should score one passed pipe", 1, scored)
    }

    @Test
    fun `checkScoring ignores already scored pipes`() {
        val pipes = listOf(
            Pipe(x = 20f, gapCenterY = 300f, gapHeight = 150f, width = 60f, scored = true)
        )

        val scored = collisionDetector.checkScoring(bird, pipes)

        assertEquals("Should not count already scored pipes", 0, scored)
    }

    @Test
    fun `updateScoredPipes marks passed pipes as scored`() {
        val pipes = listOf(
            Pipe(x = 20f, gapCenterY = 300f, gapHeight = 150f, width = 60f),  // Passed
            Pipe(x = 200f, gapCenterY = 300f, gapHeight = 150f, width = 60f)  // Not passed
        )

        val updatedPipes = collisionDetector.updateScoredPipes(bird, pipes)

        assertTrue("First pipe should be marked as scored", updatedPipes[0].scored)
        assertFalse("Second pipe should not be scored", updatedPipes[1].scored)
    }

    @Test
    fun `multiple pipes can be scored at once`() {
        val pipes = listOf(
            Pipe(x = 10f, gapCenterY = 300f, gapHeight = 150f, width = 60f),
            Pipe(x = 20f, gapCenterY = 300f, gapHeight = 150f, width = 60f),
            Pipe(x = 200f, gapCenterY = 300f, gapHeight = 150f, width = 60f)
        )

        val scored = collisionDetector.checkScoring(bird, pipes)

        assertEquals("Should score multiple passed pipes", 2, scored)
    }
}
