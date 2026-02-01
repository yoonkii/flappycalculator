package com.flappycalculator.domain.math

import com.flappycalculator.domain.model.Operator

/**
 * Difficulty tiers for math problems.
 * Higher tiers unlock more complex operations.
 */
enum class DifficultyTier(
    val minScore: Int,
    val maxScore: Int,
    val availableOperators: List<Operator>,
    val maxDigits: Int,
    val description: String
) {
    /**
     * Tier 1: Easy addition only with single digit numbers (1-9).
     * Score range: 0-7 (quick warmup)
     */
    TIER_1(
        minScore = 0,
        maxScore = 7,
        availableOperators = listOf(Operator.ADD),
        maxDigits = 1,  // Single digit only: 1+1 to 9+9
        description = "Easy Addition (single digits)"
    ),

    /**
     * Tier 2: Addition and subtraction with 2-digit numbers.
     * Score range: 8-15
     */
    TIER_2(
        minScore = 8,
        maxScore = 15,
        availableOperators = listOf(Operator.ADD, Operator.SUBTRACT),
        maxDigits = 2,  // Up to 2 digits now
        description = "Addition & Subtraction (2 digits)"
    ),

    /**
     * Tier 3: Adds single-digit multiplication.
     * Score range: 16-24
     */
    TIER_3(
        minScore = 16,
        maxScore = 24,
        availableOperators = listOf(Operator.ADD, Operator.SUBTRACT, Operator.MULTIPLY),
        maxDigits = 2,
        description = "+ Multiplication (1d × 1d)"
    ),

    /**
     * Tier 4: Adds division and harder problems.
     * Score range: 25+
     */
    TIER_4(
        minScore = 25,
        maxScore = Int.MAX_VALUE,
        availableOperators = listOf(Operator.ADD, Operator.SUBTRACT, Operator.MULTIPLY, Operator.DIVIDE),
        maxDigits = 3,
        description = "+ Division, harder problems"
    );

    companion object {
        /**
         * Get the appropriate difficulty tier for a given score.
         *
         * @param score Current player score
         * @return The matching DifficultyTier
         */
        fun forScore(score: Int): DifficultyTier {
            return entries.find { score in it.minScore..it.maxScore }
                ?: TIER_4 // Default to highest tier if score exceeds all ranges
        }
    }
}
