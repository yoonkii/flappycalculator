package com.flappycalculator.presentation.screens.title

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.data.local.ScorePreferences
import com.flappycalculator.presentation.theme.*

/**
 * Title screen shown when the app launches.
 * Displays game title, high score, and tap to start prompt.
 */
@Composable
fun TitleScreen(
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { ScorePreferences(context) }
    val highScore = remember { prefs.getHighScore() }

    // Pulsing animation for "Tap to Start"
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SkyBlue)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onStartGame
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Game title
            Text(
                text = "Flappy",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = BirdYellow,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily
            )

            Text(
                text = "Calculator",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = PipeGreen,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline
            Text(
                text = "Your brain is the button",
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
            )

            Spacer(modifier = Modifier.height(48.dp))

            // High score display
            if (highScore > 0) {
                Text(
                    text = "Best: $highScore",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NewHighScoreGold,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Tap to start
            Text(
                text = "Tap to Start",
                modifier = Modifier
                    .scale(scale)
                    .background(
                        color = ProblemBackground,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ProblemText,
                fontFamily = MaterialTheme.typography.headlineSmall.fontFamily
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Instructions
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "How to Play",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "1. Solve the math problem\n2. Enter the answer\n3. Press \u2713 to flap\n4. Navigate through pipes!",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                )
            }
        }
    }
}
