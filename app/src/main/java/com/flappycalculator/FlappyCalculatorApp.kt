package com.flappycalculator

import android.app.Application

/**
 * Application class for Flappy Calculator.
 * Initializes app-wide dependencies.
 */
class FlappyCalculatorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide dependencies here
        // Sound manager, preferences, etc. will be initialized as needed
    }
}
