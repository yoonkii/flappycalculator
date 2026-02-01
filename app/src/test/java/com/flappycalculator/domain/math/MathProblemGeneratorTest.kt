package com.flappycalculator.domain.math

import com.flappycalculator.domain.model.Operator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

/**
 * Unit tests for MathProblemGenerator.
 */
class MathProblemGeneratorTest {

    private lateinit var generator: MathProblemGenerator

    @Before
    fun setup() {
        generator = MathProblemGenerator(Random(42)) // Fixed seed for reproducibility
    }

    @Test
    fun `tier 1 only generates addition problems`() {
        repeat(100) {
            val problem = generator.generateProblem(DifficultyTier.TIER_1)
            assertEquals(Operator.ADD, problem.operator)
        }
    }

    @Test
    fun `tier 2 generates addition and subtraction`() {
        val operators = mutableSetOf<Operator>()
        repeat(100) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2)
            operators.add(problem.operator)
        }
        assertTrue(operators.contains(Operator.ADD))
        assertTrue(operators.contains(Operator.SUBTRACT))
        assertFalse(operators.contains(Operator.MULTIPLY))
        assertFalse(operators.contains(Operator.DIVIDE))
    }

    @Test
    fun `tier 3 includes multiplication`() {
        val operators = mutableSetOf<Operator>()
        repeat(100) {
            val problem = generator.generateProblem(DifficultyTier.TIER_3)
            operators.add(problem.operator)
        }
        assertTrue(operators.contains(Operator.MULTIPLY))
    }

    @Test
    fun `tier 4 includes division`() {
        val operators = mutableSetOf<Operator>()
        repeat(100) {
            val problem = generator.generateProblem(DifficultyTier.TIER_4)
            operators.add(problem.operator)
        }
        assertTrue(operators.contains(Operator.DIVIDE))
    }

    @Test
    fun `all answers are positive`() {
        repeat(500) { i ->
            val tier = DifficultyTier.entries[i % 4]
            val problem = generator.generateProblem(tier)
            assertTrue("Answer should be positive: ${problem.getDisplayString()} = ${problem.answer}",
                problem.answer > 0)
        }
    }

    @Test
    fun `subtraction results are always positive`() {
        repeat(200) {
            val problem = generator.generateProblem(DifficultyTier.TIER_2)
            if (problem.operator == Operator.SUBTRACT) {
                assertTrue("Subtraction result should be positive", problem.answer > 0)
                assertTrue("First operand should be >= second", problem.operand1 >= problem.operand2)
            }
        }
    }

    @Test
    fun `division has clean integer results`() {
        repeat(200) {
            val problem = generator.generateProblem(DifficultyTier.TIER_4)
            if (problem.operator == Operator.DIVIDE) {
                assertEquals(
                    "Division should have clean result",
                    problem.operand1,
                    problem.answer * problem.operand2
                )
            }
        }
    }

    @Test
    fun `answer calculation is correct`() {
        repeat(200) { i ->
            val tier = DifficultyTier.entries[i % 4]
            val problem = generator.generateProblem(tier)

            val expectedAnswer = when (problem.operator) {
                Operator.ADD -> problem.operand1 + problem.operand2
                Operator.SUBTRACT -> problem.operand1 - problem.operand2
                Operator.MULTIPLY -> problem.operand1 * problem.operand2
                Operator.DIVIDE -> problem.operand1 / problem.operand2
            }

            assertEquals(
                "Answer should be correct for ${problem.getDisplayString()}",
                expectedAnswer,
                problem.answer
            )
        }
    }

    @Test
    fun `isCorrect returns true for correct answer`() {
        val problem = generator.generateProblem(DifficultyTier.TIER_1)
        assertTrue(problem.isCorrect(problem.answer))
    }

    @Test
    fun `isCorrect returns false for wrong answer`() {
        val problem = generator.generateProblem(DifficultyTier.TIER_1)
        assertFalse(problem.isCorrect(problem.answer + 1))
        assertFalse(problem.isCorrect(problem.answer - 1))
    }

    @Test
    fun `forScore returns correct tier`() {
        // Tier 1: 0-7 (easy single digit addition)
        assertEquals(DifficultyTier.TIER_1, DifficultyTier.forScore(0))
        assertEquals(DifficultyTier.TIER_1, DifficultyTier.forScore(7))
        // Tier 2: 8-15
        assertEquals(DifficultyTier.TIER_2, DifficultyTier.forScore(8))
        assertEquals(DifficultyTier.TIER_2, DifficultyTier.forScore(15))
        // Tier 3: 16-24
        assertEquals(DifficultyTier.TIER_3, DifficultyTier.forScore(16))
        assertEquals(DifficultyTier.TIER_3, DifficultyTier.forScore(24))
        // Tier 4: 25+
        assertEquals(DifficultyTier.TIER_4, DifficultyTier.forScore(25))
        assertEquals(DifficultyTier.TIER_4, DifficultyTier.forScore(1000))
    }
}
