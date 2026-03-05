package com.flappycalculator.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * SharedPreferences wrapper for app settings persistence.
 * Stores BGM volume, SFX volume, and vibration toggle.
 */
class SettingsPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    var bgmVolume: Float
        get() = prefs.getFloat(KEY_BGM_VOLUME, DEFAULT_BGM_VOLUME)
        set(value) = prefs.edit { putFloat(KEY_BGM_VOLUME, value) }

    var sfxVolume: Float
        get() = prefs.getFloat(KEY_SFX_VOLUME, DEFAULT_SFX_VOLUME)
        set(value) = prefs.edit { putFloat(KEY_SFX_VOLUME, value) }

    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION_ENABLED, DEFAULT_VIBRATION_ENABLED)
        set(value) = prefs.edit { putBoolean(KEY_VIBRATION_ENABLED, value) }

    companion object {
        private const val PREFS_NAME = "flappy_calculator_settings"
        private const val KEY_BGM_VOLUME = "bgm_volume"
        private const val KEY_SFX_VOLUME = "sfx_volume"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val DEFAULT_BGM_VOLUME = 0.5f
        private const val DEFAULT_SFX_VOLUME = 0.5f
        private const val DEFAULT_VIBRATION_ENABLED = true
    }
}
