package com.flappycalculator.data.repository

import com.flappycalculator.data.local.ScorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for score-related data operations.
 * Abstracts the data source from the rest of the app.
 */
class ScoreRepository(
    private val scorePreferences: ScorePreferences
) {
    /**
     * Get the current high score.
     *
     * @return The high score
     */
    suspend fun getHighScore(): Int = withContext(Dispatchers.IO) {
        scorePreferences.getHighScore()
    }

    /**
     * Save a score and update high score if applicable.
     *
     * @param score The score to save
     * @return true if this was a new high score
     */
    suspend fun saveScore(score: Int): Boolean = withContext(Dispatchers.IO) {
        scorePreferences.incrementGamesPlayed()
        scorePreferences.updateHighScoreIfBetter(score)
    }

    /**
     * Get total games played.
     *
     * @return Number of games played
     */
    suspend fun getGamesPlayed(): Int = withContext(Dispatchers.IO) {
        scorePreferences.getGamesPlayed()
    }

    /**
     * Reset all stats (for testing).
     */
    suspend fun resetStats() = withContext(Dispatchers.IO) {
        scorePreferences.clear()
    }
}
