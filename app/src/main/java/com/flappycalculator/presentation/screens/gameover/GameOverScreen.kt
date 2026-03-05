package com.flappycalculator.presentation.screens.gameover

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.data.local.ScorePreferences
import com.flappycalculator.presentation.theme.*

/**
 * Game over screen with Wall Street terminal aesthetic.
 * Displays final score, leaderboard, and retry/menu options.
 */
@Composable
fun GameOverScreen(
    score: Int,
    highScore: Int,
    isNewHighScore: Boolean,
    onRetry: () -> Unit,
    onMenu: () -> Unit,
    vibrationEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val topScores = remember {
        ScorePreferences(context).getTopScores()
    }

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
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .background(
                    color = ProblemBackground,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    color = GoldAccent.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp)
        ) {
            // Game Over title
            Text(
                text = "GAME OVER",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = WrongRed,
                letterSpacing = 4.sp,
                fontFamily = MaterialTheme.typography.displayMedium.fontFamily
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Gold divider
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(2.dp)
                    .background(GoldAccent)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Score label
            Text(
                text = "SCORE",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 3.sp,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Score number
            Text(
                text = score.toString(),
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = TerminalGreen,
                letterSpacing = 2.sp,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily
            )

            // New high score indicator
            if (isNewHighScore) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\u2605 NEW HIGH SCORE \u2605",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NewHighScoreGold.copy(alpha = goldPulse),
                    letterSpacing = 2.sp,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                )
            }

            // Top 10 Leaderboard
            if (topScores.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "LEADERBOARD",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TerminalGreen.copy(alpha = 0.7f),
                    letterSpacing = 3.sp,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = KeypadBackground,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = KeypadButton.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    topScores.forEachIndexed { index, topScore ->
                        LeaderboardRow(
                            rank = index + 1,
                            scoreValue = topScore,
                            isCurrentScore = topScore == score && index == topScores.indexOf(score),
                            isHighlighted = topScore == score
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GameOverButton(
                    text = "MENU",
                    backgroundColor = KeypadButton,
                    borderColor = Color.White.copy(alpha = 0.2f),
                    textColor = Color.White.copy(alpha = 0.8f),
                    onClick = onMenu,
                    vibrationEnabled = vibrationEnabled,
                    modifier = Modifier.weight(1f)
                )

                GameOverButton(
                    text = "RETRY",
                    backgroundColor = SubmitButton,
                    onClick = onRetry,
                    vibrationEnabled = vibrationEnabled,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LeaderboardRow(
    rank: Int,
    scoreValue: Int,
    isCurrentScore: Boolean,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isHighlighted -> GoldAccent.copy(alpha = 0.15f)
        rank == 1 -> Color(0xFFFFD700).copy(alpha = 0.1f)
        rank == 2 -> Color(0xFFC0C0C0).copy(alpha = 0.08f)
        rank == 3 -> Color(0xFFCD7F32).copy(alpha = 0.08f)
        else -> Color.Transparent
    }

    val rankEmoji = when (rank) {
        1 -> "\uD83E\uDD47"
        2 -> "\uD83E\uDD48"
        3 -> "\uD83E\uDD49"
        else -> ""
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .then(
                if (isHighlighted) Modifier.border(
                    width = 1.dp,
                    color = GoldAccent.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                ) else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (rankEmoji.isNotEmpty()) rankEmoji else "#$rank",
                fontSize = if (rankEmoji.isNotEmpty()) 18.sp else 14.sp,
                fontWeight = if (rank <= 3) FontWeight.Bold else FontWeight.Normal,
                color = if (isHighlighted) GoldAccent else Color.White.copy(alpha = 0.5f),
                modifier = Modifier.width(36.dp)
            )
        }

        Text(
            text = scoreValue.toString(),
            fontSize = 16.sp,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) GoldAccent else TerminalGreen.copy(alpha = 0.9f),
            letterSpacing = 1.sp,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
        )
    }
}

@Composable
private fun GameOverButton(
    text: String,
    backgroundColor: Color,
    borderColor: Color = Color.Transparent,
    textColor: Color = Color.White,
    onClick: () -> Unit,
    vibrationEnabled: Boolean = true,
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
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .then(
                if (borderColor != Color.Transparent) Modifier.border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                ) else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (vibrationEnabled) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 2.sp,
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily
        )
    }
}
