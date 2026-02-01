package com.flappycalculator.presentation.components.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.domain.model.InputFeedback
import com.flappycalculator.presentation.theme.*

/**
 * Display field for the current answer input.
 * Shows shake animation on wrong answer, flash on correct.
 */
@Composable
fun AnswerInput(
    currentInput: String,
    feedback: InputFeedback,
    modifier: Modifier = Modifier
) {
    // Shake animation for wrong answer
    val shakeOffset by animateFloatAsState(
        targetValue = if (feedback == InputFeedback.INCORRECT) 1f else 0f,
        animationSpec = if (feedback == InputFeedback.INCORRECT) {
            keyframes {
                durationMillis = 300
                0f at 0
                -10f at 50
                10f at 100
                -10f at 150
                10f at 200
                -5f at 250
                0f at 300
            }
        } else {
            tween(0)
        },
        label = "shake"
    )

    // Background color animation for feedback
    val backgroundColor by animateColorAsState(
        targetValue = when (feedback) {
            InputFeedback.CORRECT -> CorrectGreen.copy(alpha = 0.3f)
            InputFeedback.INCORRECT -> WrongRed.copy(alpha = 0.3f)
            InputFeedback.NONE -> InputBackground
        },
        animationSpec = tween(150),
        label = "bgColor"
    )

    // Border color animation
    val borderColor by animateColorAsState(
        targetValue = when (feedback) {
            InputFeedback.CORRECT -> CorrectGreen
            InputFeedback.INCORRECT -> WrongRed
            InputFeedback.NONE -> Color.Gray.copy(alpha = 0.5f)
        },
        animationSpec = tween(150),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer {
                translationX = shakeOffset * 10
            }
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (currentInput.isEmpty()) {
            // Placeholder text
            Text(
                text = "Enter answer...",
                color = Color.Gray,
                fontSize = 24.sp,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily
            )
        } else {
            // Current input
            Text(
                text = currentInput,
                color = InputText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.displaySmall.fontFamily,
                textAlign = TextAlign.Center
            )
        }

        // Blinking cursor when input is not empty
        if (currentInput.isNotEmpty()) {
            val infiniteTransition = rememberInfiniteTransition(label = "cursor")
            val cursorAlpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(500),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "cursorAlpha"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Invisible text to measure width
                Text(
                    text = currentInput,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Transparent,
                    fontFamily = MaterialTheme.typography.displaySmall.fontFamily
                )
                // Cursor
                Text(
                    text = "|",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = InputText.copy(alpha = cursorAlpha),
                    fontFamily = MaterialTheme.typography.displaySmall.fontFamily
                )
            }
        }
    }
}
