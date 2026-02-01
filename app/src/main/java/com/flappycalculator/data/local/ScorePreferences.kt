package com.flappycalculator.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * SharedPreferences wrapper for score persistence.
 * Handles local storage of high score.
 */
class ScorePreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * Get the stored high score.
     *
     * @return The high score, or 0 if none exists
     */
    fun getHighScore(): Int {
        return prefs.getInt(KEY_HIGH_SCORE, 0)
    }

    /**
     * Save a new high score.
     *
     * @param score The score to save
     */
    fun saveHighScore(score: Int) {
        prefs.edit {
            putInt(KEY_HIGH_SCORE, score)
        }
    }

    /**
     * Update high score only if new score is higher.
     *
     * @param score The new score to compare
     * @return true if this was a new high score
     */
    fun updateHighScoreIfBetter(score: Int): Boolean {
        val currentHigh = getHighScore()
        return if (score > currentHigh) {
            saveHighScore(score)
            true
        } else {
            false
        }
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
        private const val KEY_HIGH_SCORE = "high_score"
        private const val KEY_GAMES_PLAYED = "games_played"
    }
}
