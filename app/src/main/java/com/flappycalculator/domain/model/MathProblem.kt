package com.flappycalculator.domain.model

/**
 * Mathematical operators for problems.
 */
enum class Operator(val symbol: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("×"),
    DIVIDE("÷")
}

/**
 * Represents a math problem with two operands and an operator.
 * The answer is pre-calculated to ensure correctness.
 */
data class MathProblem(
    val operand1: Int,
    val operand2: Int,
    val operator: Operator,
    val answer: Int
) {
    /**
     * Get the display string for the problem.
     * Example: "24 + 13 = ?"
     */
    fun getDisplayString(): String {
        return "$operand1 ${operator.symbol} $operand2 = ?"
    }

    /**
     * Get a shorter display string without the "= ?".
     * Example: "24 + 13"
     */
    fun getShortDisplayString(): String {
        return "$operand1 ${operator.symbol} $operand2"
    }

    /**
     * Check if a given input matches the answer.
     *
     * @param input User's answer
     * @return true if correct
     */
    fun isCorrect(input: Int): Boolean {
        return input == answer
    }

    companion object {
        /**
         * Create an addition problem.
         *
         * @param a First operand
         * @param b Second operand
         * @return MathProblem with correct answer
         */
        fun addition(a: Int, b: Int): MathProblem {
            return MathProblem(
                operand1 = a,
                operand2 = b,
                operator = Operator.ADD,
                answer = a + b
            )
        }

        /**
         * Create a subtraction problem.
         * Automatically ensures first operand is larger for positive result.
         *
         * @param a First operand
         * @param b Second operand
         * @return MathProblem with correct answer
         */
        fun subtraction(a: Int, b: Int): MathProblem {
            // Ensure positive result
            val (larger, smaller) = if (a >= b) Pair(a, b) else Pair(b, a)
            return MathProblem(
                operand1 = larger,
                operand2 = smaller,
                operator = Operator.SUBTRACT,
                answer = larger - smaller
            )
        }

        /**
         * Create a multiplication problem.
         *
         * @param a First operand
         * @param b Second operand
         * @return MathProblem with correct answer
         */
        fun multiplication(a: Int, b: Int): MathProblem {
            return MathProblem(
                operand1 = a,
                operand2 = b,
                operator = Operator.MULTIPLY,
                answer = a * b
            )
        }

        /**
         * Create a division problem.
         * Constructs from quotient and divisor to ensure clean division.
         *
         * @param quotient The expected answer
         * @param divisor The number to divide by
         * @return MathProblem with clean integer answer
         */
        fun division(quotient: Int, divisor: Int): MathProblem {
            val dividend = quotient * divisor
            return MathProblem(
                operand1 = dividend,
                operand2 = divisor,
                operator = Operator.DIVIDE,
                answer = quotient
            )
        }
    }
}

/**
 * Game state enumeration.
 */
enum class GameState {
    READY,      // Waiting to start (title screen or pre-game)
    PLAYING,    // Game in progress
    GAME_OVER   // Player has died
}

/**
 * Snapshot of the entire game state at a point in time.
 * Used for rendering and state management.
 */
data class GameSnapshot(
    val bird: Bird,
    val pipes: List<Pipe>,
    val score: Int,
    val currentProblem: MathProblem,
    val gameState: GameState,
    val scrollSpeed: Float
)

/**
 * Result of a collision check.
 */
data class CollisionResult(
    val hitObstacle: Boolean,
    val hitCeiling: Boolean = false,
    val hitFloor: Boolean = false
)

/**
 * Feedback state for user input.
 */
enum class InputFeedback {
    NONE,       // No feedback
    CORRECT,    // Answer was correct - trigger flap animation
    INCORRECT   // Answer was wrong - trigger shake animation
}
