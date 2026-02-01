package com.flappycalculator.domain.math

import com.flappycalculator.domain.model.MathProblem
import com.flappycalculator.domain.model.Operator
import kotlin.random.Random

/**
 * Generates math problems based on difficulty tier.
 * Ensures all answers are positive integers and avoids consecutive same operators.
 */
class MathProblemGenerator(
    private val random: Random = Random.Default
) {
    private var lastOperator: Operator? = null

    /**
     * Generate a new math problem for the given tier.
     *
     * @param tier Current difficulty tier
     * @return A new MathProblem
     */
    fun generateProblem(tier: DifficultyTier): MathProblem {
        val operator = selectOperator(tier.availableOperators)
        lastOperator = operator

        return when (operator) {
            Operator.ADD -> generateAddition(tier)
            Operator.SUBTRACT -> generateSubtraction(tier)
            Operator.MULTIPLY -> generateMultiplication(tier)
            Operator.DIVIDE -> generateDivision(tier)
        }
    }

    /**
     * Generate a problem for a given score.
     * Convenience method that determines tier automatically.
     *
     * @param score Current player score
     * @return A new MathProblem appropriate for the score
     */
    fun generateProblemForScore(score: Int): MathProblem {
        return generateProblem(DifficultyTier.forScore(score))
    }

    /**
     * Select an operator, avoiding consecutive repeats when possible.
     */
    private fun selectOperator(availableOperators: List<Operator>): Operator {
        // If only one operator available, use it
        if (availableOperators.size == 1) {
            return availableOperators.first()
        }

        // Try to avoid repeating the last operator
        val candidates = availableOperators.filter { it != lastOperator }
        val pool = candidates.ifEmpty { availableOperators }

        return pool[random.nextInt(pool.size)]
    }

    /**
     * Generate an addition problem.
     * Operand range depends on tier.
     */
    private fun generateAddition(tier: DifficultyTier): MathProblem {
        val maxValue = getMaxValue(tier)
        val a = random.nextInt(1, maxValue + 1)
        val b = random.nextInt(1, maxValue + 1)
        return MathProblem.addition(a, b)
    }

    /**
     * Generate a subtraction problem.
     * First operand is always larger to ensure positive result.
     */
    private fun generateSubtraction(tier: DifficultyTier): MathProblem {
        val maxValue = getMaxValue(tier)
        val a = random.nextInt(10, maxValue + 1)  // Ensure reasonable subtraction
        val b = random.nextInt(1, a)  // b is always smaller than a
        return MathProblem.subtraction(a, b)
    }

    /**
     * Generate a multiplication problem.
     * Tier 3: 1-digit × 1-digit (2-9 × 2-9)
     * Tier 4: 2-digit × 1-digit (10-99 × 2-9)
     */
    private fun generateMultiplication(tier: DifficultyTier): MathProblem {
        return if (tier == DifficultyTier.TIER_4) {
            // 2-digit × 1-digit for tier 4
            val a = random.nextInt(10, 100)
            val b = random.nextInt(2, 10)
            MathProblem.multiplication(a, b)
        } else {
            // 1-digit × 1-digit for tier 3
            val a = random.nextInt(2, 10)
            val b = random.nextInt(2, 10)
            MathProblem.multiplication(a, b)
        }
    }

    /**
     * Generate a division problem.
     * Creates from quotient × divisor to ensure clean integer result.
     */
    private fun generateDivision(tier: DifficultyTier): MathProblem {
        // Generate divisor and quotient, then calculate dividend
        val divisor = random.nextInt(2, 10)  // 2-9
        val quotient = random.nextInt(2, 13) // 2-12, gives reasonable dividends
        return MathProblem.division(quotient, divisor)
    }

    /**
     * Get maximum value for operands based on tier.
     */
    private fun getMaxValue(tier: DifficultyTier): Int {
        return when (tier.maxDigits) {
            1 -> 9
            2 -> 99
            3 -> 999
            else -> 99
        }
    }

    /**
     * Reset the last operator tracking.
     * Call this when starting a new game.
     */
    fun reset() {
        lastOperator = null
    }
}
