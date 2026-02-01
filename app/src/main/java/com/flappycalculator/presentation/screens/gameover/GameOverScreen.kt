package com.flappycalculator.presentation.screens.gameover

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.presentation.theme.*

/**
 * Game over screen shown after the player dies.
 * Displays final score, high score, and options to retry or go to menu.
 */
@Composable
fun GameOverScreen(
    score: Int,
    highScore: Int,
    isNewHighScore: Boolean,
    onRetry: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation for new high score
    val infiniteTransition = rememberInfiniteTransition(label = "highScore")
    val goldPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "goldPulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GameOverBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .background(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(32.dp)
        ) {
            // Game Over title
            Text(
                text = "Game Over",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = WrongRed,
                fontFamily = MaterialTheme.typography.displayMedium.fontFamily
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Score
            Text(
                text = "Score",
                fontSize = 18.sp,
                color = Color.Gray,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
            )

            Text(
                text = score.toString(),
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = ProblemText,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New high score indicator
            if (isNewHighScore) {
                Text(
                    text = "\u2605 New High Score! \u2605",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NewHighScoreGold.copy(alpha = goldPulse),
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                )
            } else {
                Text(
                    text = "Best: $highScore",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Menu button
                GameOverButton(
                    text = "Menu",
                    backgroundColor = KeypadButton,
                    onClick = onMenu,
                    modifier = Modifier.weight(1f)
                )

                // Retry button
                GameOverButton(
                    text = "Retry",
                    backgroundColor = SubmitButton,
                    onClick = onRetry,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun GameOverButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val bgColor = if (isPressed) {
        backgroundColor.copy(alpha = 0.7f)
    } else {
        backgroundColor
    }

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily
        )
    }
}
