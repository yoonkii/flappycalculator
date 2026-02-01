package com.flappycalculator.domain.model

/**
 * Game configuration constants.
 * Adjust these values for playtesting and difficulty tuning.
 */
object GameConfig {
    // Physics - very forgiving, gives player time to solve problems
    const val GRAVITY = 450f                    // pixels/sec² (very gentle, floaty)
    const val TERMINAL_VELOCITY = 350f          // max fall speed (capped)
    const val FLAP_IMPULSE = -520f              // upward velocity - high jump for more thinking time

    // Pipe configuration
    const val PIPE_GAP_MULTIPLIER = 6.0f        // gap height = bird height × this (generous)
    const val PIPE_WIDTH_RATIO = 0.15f          // pipe width = screen width × this
    const val PIPE_SPACING = 550f               // horizontal distance between pipes (more time)
    const val MIN_PIPE_HEIGHT_RATIO = 0.1f      // minimum pipe height as ratio of game area

    // Scroll speed
    const val INITIAL_SCROLL_SPEED = 100f       // pixels/sec at start (slow)
    const val MAX_SCROLL_SPEED = 250f           // maximum scroll speed
    const val SPEED_INCREMENT_PER_SCORE = 1.5f  // speed increase per pipe passed (gradual)

    // Bird configuration
    const val BIRD_X_POSITION_RATIO = 0.2f      // bird X position = 20% from left edge
    const val BIRD_SIZE_RATIO = 0.08f           // bird width = 8% of screen width
    const val BIRD_ASPECT_RATIO = 1.0f          // bird height/width ratio

    // Timing
    const val TARGET_FPS = 60
    const val FRAME_TIME_MS = 1000L / TARGET_FPS
    const val MAX_DELTA_TIME = 0.05f            // cap delta time to prevent physics explosions

    // Rotation
    const val MAX_ROTATION_DOWN = 45f           // max downward rotation (degrees)
    const val MAX_ROTATION_UP = -20f            // max upward rotation on flap (degrees)
    const val ROTATION_LERP_FACTOR = 0.1f       // smoothing for rotation changes

    // Input
    const val MAX_INPUT_LENGTH = 6              // maximum digits for answer input

    // Game area
    const val GAME_AREA_RATIO = 0.6f            // top 60% for game
    const val KEYPAD_AREA_RATIO = 0.4f          // bottom 40% for keypad
}
