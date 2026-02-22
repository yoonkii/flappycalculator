package com.flappycalculator.presentation.screens.title

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.data.local.ScorePreferences
import com.flappycalculator.presentation.theme.*

/**
 * Title screen shown when the app launches.
 * Wall Street trading terminal aesthetic with skyline background.
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
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Bobbing animation for analyst character
    val bobOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bob"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onStartGame
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background: Night skyline
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Sky gradient bands
            val skyColors = listOf(
                Color(0xFF0D1B2A),
                Color(0xFF1B263B),
                Color(0xFF152238),
                Color(0xFF1A202C)
            )
            val bandHeight = size.height / 4
            skyColors.forEachIndexed { index, color ->
                drawRect(
                    color = color,
                    topLeft = Offset(0f, bandHeight * index),
                    size = Size(size.width, bandHeight)
                )
            }

            // Building silhouettes
            val buildingColor = Color(0xFF2D3748)
            val buildingHeights = listOf(0.30f, 0.42f, 0.28f, 0.50f, 0.38f, 0.48f, 0.33f, 0.55f)
            val buildingWidth = size.width / buildingHeights.size
            buildingHeights.forEachIndexed { index, heightRatio ->
                val bh = size.height * heightRatio
                drawRect(
                    color = buildingColor,
                    topLeft = Offset(index * buildingWidth, size.height - bh),
                    size = Size(buildingWidth - 4f, bh)
                )
                // Gold window dots
                val windowRows = (bh / 40).toInt()
                for (row in 0 until windowRows) {
                    val hash = ((index * 31 + row * 17) % 100 + 100) % 100
                    if (hash < 60) {
                        drawRect(
                            color = GoldAccent.copy(alpha = 0.6f),
                            topLeft = Offset(
                                index * buildingWidth + buildingWidth / 2 - 3f,
                                size.height - bh + 20f + row * 35f
                            ),
                            size = Size(5f, 7f)
                        )
                    }
                }
            }

            // Gold accent line at bottom
            drawRect(
                color = GoldAccent.copy(alpha = 0.5f),
                topLeft = Offset(0f, size.height - 3f),
                size = Size(size.width, 3f)
            )
        }

        // Foreground content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.statusBarsPadding()
        ) {
            // Game title
            Text(
                text = "FINANCE",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 6.sp,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily
            )

            Text(
                text = "BIRD",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = TerminalGreen,
                letterSpacing = 8.sp,
                fontFamily = MaterialTheme.typography.displayLarge.fontFamily
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Analyst character preview
            Canvas(
                modifier = Modifier
                    .size(80.dp, 100.dp)
                    .offset(y = bobOffset.dp)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val w = size.width * 0.8f
                val h = size.height * 0.8f

                // Body (suit)
                drawRect(
                    color = SuitNavy,
                    topLeft = Offset(centerX - w * 0.35f, centerY - h * 0.1f),
                    size = Size(w * 0.7f, h * 0.5f)
                )
                // White shirt
                drawRect(
                    color = ShirtWhite,
                    topLeft = Offset(centerX - w * 0.1f, centerY - h * 0.1f),
                    size = Size(w * 0.2f, h * 0.35f)
                )
                // Red tie
                drawPath(
                    path = Path().apply {
                        moveTo(centerX - w * 0.05f, centerY - h * 0.1f)
                        lineTo(centerX + w * 0.05f, centerY - h * 0.1f)
                        lineTo(centerX + w * 0.06f, centerY + h * 0.3f)
                        lineTo(centerX, centerY + h * 0.38f)
                        lineTo(centerX - w * 0.06f, centerY + h * 0.3f)
                        close()
                    },
                    color = TieRed
                )
                // Head
                drawOval(
                    color = SkinTone,
                    topLeft = Offset(centerX - w * 0.3f, centerY - h * 0.5f),
                    size = Size(w * 0.6f, h * 0.45f)
                )
                // Hair
                drawArc(
                    color = HairDark,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(centerX - w * 0.32f, centerY - h * 0.55f),
                    size = Size(w * 0.64f, h * 0.25f)
                )
                // Eyes
                val eyeSize = w * 0.08f
                val eyeY = centerY - h * 0.3f
                drawCircle(Color.White, eyeSize, Offset(centerX - w * 0.12f, eyeY))
                drawCircle(Color.Black, eyeSize * 0.5f, Offset(centerX - w * 0.1f, eyeY))
                drawCircle(Color.White, eyeSize, Offset(centerX + w * 0.12f, eyeY))
                drawCircle(Color.Black, eyeSize * 0.5f, Offset(centerX + w * 0.14f, eyeY))
                // Briefcase
                drawRect(
                    color = Color(0xFF8B4513),
                    topLeft = Offset(centerX - w * 0.2f, centerY + h * 0.4f),
                    size = Size(w * 0.4f, h * 0.2f)
                )
                drawRect(
                    color = GoldAccent,
                    topLeft = Offset(centerX - w * 0.08f, centerY + h * 0.38f),
                    size = Size(w * 0.16f, h * 0.05f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Tagline
            Text(
                text = "Your brain is the button",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 1.sp,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            )

            Spacer(modifier = Modifier.height(32.dp))

            // High score display
            if (highScore > 0) {
                Text(
                    text = "HIGH SCORE: $highScore",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    letterSpacing = 2.sp,
                    modifier = Modifier
                        .background(
                            color = ProblemBackground,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = GoldAccent.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Tap to start
            Text(
                text = "TAP TO START",
                modifier = Modifier
                    .scale(scale)
                    .background(
                        color = ProblemBackground,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = TerminalGreen.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 32.dp, vertical = 14.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TerminalGreen,
                letterSpacing = 3.sp,
                fontFamily = MaterialTheme.typography.headlineSmall.fontFamily
            )

            Spacer(modifier = Modifier.height(48.dp))

            // How to Play - terminal window style
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .background(
                        color = ProblemBackground,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = TerminalGreen.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                // Terminal title bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Terminal window dots
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(StockRed, GoldAccent, TerminalGreen).forEach { dotColor ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(dotColor, CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "HOW TO PLAY",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TerminalGreen,
                        letterSpacing = 2.sp,
                        fontFamily = MaterialTheme.typography.titleSmall.fontFamily
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(TerminalGreen.copy(alpha = 0.2f))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "1. Solve the math problem\n2. Enter the answer\n3. Press \u2713 to flap\n4. Navigate through pipes!",
                    fontSize = 13.sp,
                    color = TerminalGreen.copy(alpha = 0.85f),
                    textAlign = TextAlign.Start,
                    lineHeight = 22.sp,
                    letterSpacing = 0.5.sp,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
