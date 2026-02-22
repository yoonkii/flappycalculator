package com.flappycalculator.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * SharedPreferences wrapper for score persistence.
 * Handles local storage of high scores (top 10).
 */
class ScorePreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * Get the stored high score (best score).
     *
     * @return The high score, or 0 if none exists
     */
    fun getHighScore(): Int {
        return getTopScores().firstOrNull() ?: 0
    }

    /**
     * Get top 10 scores, sorted from highest to lowest.
     *
     * @return List of top scores (up to 10)
     */
    fun getTopScores(): List<Int> {
        val scoresString = prefs.getString(KEY_TOP_SCORES, "") ?: ""
        if (scoresString.isEmpty()) {
            // Migration: check for legacy high score
            val legacyHighScore = prefs.getInt(KEY_HIGH_SCORE, 0)
            return if (legacyHighScore > 0) listOf(legacyHighScore) else emptyList()
        }
        return scoresString.split(",")
            .mapNotNull { it.toIntOrNull() }
            .sortedDescending()
    }

    /**
     * Add a score to the top 10 list.
     *
     * @param score The score to add
     * @return The rank (1-10) if it made the top 10, null otherwise
     */
    fun addScore(score: Int): Int? {
        if (score <= 0) return null

        val currentScores = getTopScores().toMutableList()
        currentScores.add(score)
        val sortedScores = currentScores.sortedDescending().take(MAX_TOP_SCORES)

        val rank = sortedScores.indexOf(score) + 1

        // Save the updated list
        prefs.edit {
            putString(KEY_TOP_SCORES, sortedScores.joinToString(","))
        }

        return if (rank in 1..MAX_TOP_SCORES) rank else null
    }

    /**
     * Update high score only if new score is higher.
     * Also updates top 10 list.
     *
     * @param score The new score to compare
     * @return true if this was a new high score (#1)
     */
    fun updateHighScoreIfBetter(score: Int): Boolean {
        val currentHigh = getHighScore()
        addScore(score)
        return score > currentHigh
    }

    /**
     * Get total games played (for stats).
     */
    fun getGamesPlayed(): Int {
        return prefs.getInt(KEY_GAMES_PLAYED, 0)
    }

    /**
     * Increment games played counter.
     */
    fun incrementGamesPlayed() {
        val current = getGamesPlayed()
        prefs.edit {
            putInt(KEY_GAMES_PLAYED, current + 1)
        }
    }

    /**
     * Clear all stored data.
     * Primarily for testing/debugging.
     */
    fun clear() {
        prefs.edit {
            clear()
        }
    }

    companion object {
        private const val PREFS_NAME = "flappy_calculator_prefs"
        private const val KEY_HIGH_SCORE = "high_score"  // Legacy, kept for migration
        private const val KEY_TOP_SCORES = "top_scores"
        private const val KEY_GAMES_PLAYED = "games_played"
        private const val MAX_TOP_SCORES = 10
    }
}
