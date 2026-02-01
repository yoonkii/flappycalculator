package com.flappycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flappycalculator.presentation.navigation.FlappyCalculatorNavHost
import com.flappycalculator.presentation.theme.FlappyCalculatorTheme

/**
 * Main entry point for Flappy Calculator.
 * Sets up Compose and navigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FlappyCalculatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FlappyCalculatorNavHost()
                }
            }
        }
    }
}
