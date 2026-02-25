package com.flappycalculator.presentation.screens.title

import android.graphics.BitmapFactory
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.R
import com.flappycalculator.data.local.ScorePreferences
import com.flappycalculator.domain.model.GameConfig
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

    // Load background bitmap
    val bgBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_skyline).asImageBitmap()
    }

    // Slow background scroll animation
    var bgOffset by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        var lastNanos = System.nanoTime()
        while (true) {
            withFrameNanos { nanos ->
                val dt = (nanos - lastNanos) / 1_000_000_000f
                lastNanos = nanos
                bgOffset += GameConfig.BACKGROUND_SCROLL_SPEED * dt
            }
        }
    }

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
        // Scrolling background image
        Canvas(modifier = Modifier.fillMaxSize()) {
            val imgScale = size.height / bgBitmap.height.toFloat()
            val scaledWidth = (bgBitmap.width * imgScale).toInt()
            val scaledHeight = size.height.toInt()
            val canvasWidth = size.width.toInt()
            val wrappedOffset = (bgOffset % scaledWidth.toFloat()).toInt()

            var x = -wrappedOffset
            while (x < canvasWidth) {
                drawImage(
                    image = bgBitmap,
                    dstOffset = IntOffset(x, 0),
                    dstSize = IntSize(scaledWidth, scaledHeight)
                )
                x += scaledWidth
            }

            // Dark overlay for readability
            drawRect(
                color = Color.Black.copy(alpha = 0.45f),
                size = size
            )
        }

        // Foreground content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Game title in DSEG14 calculator LCD font
            Text(
                text = "FLAPPY",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 4.sp,
                fontFamily = Dseg14FontFamily
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "CALCULATOR",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = TerminalGreen,
                letterSpacing = 4.sp,
                fontFamily = Dseg14FontFamily
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Your brain is the button",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 1.sp,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            )

            Spacer(modifier = Modifier.height(32.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

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
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tap to start - pinned near bottom
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
        }
    }
}
