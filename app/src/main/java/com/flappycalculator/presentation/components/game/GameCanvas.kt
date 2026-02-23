package com.flappycalculator.presentation.components.game

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.flappycalculator.R
import com.flappycalculator.domain.model.Bird
import com.flappycalculator.domain.model.GameConfig
import com.flappycalculator.domain.model.Pipe
import com.flappycalculator.presentation.theme.*


/**
 * Main game canvas that renders the bird, pipes, and background.
 * Wall Street theme with business analyst and stock chart bars.
 */
@Composable
fun GameCanvas(
    bird: Bird,
    pipes: List<Pipe>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Load background bitmap once
    val bgBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.bg_skyline).asImageBitmap()
    }

    // Load character sprites once
    val charDefault = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.char_default).asImageBitmap()
    }
    val charJump2 = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.char_jump_2).asImageBitmap()
    }
    val charJump3 = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.char_jump_3).asImageBitmap()
    }
    val charFall = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.char_fall).asImageBitmap()
    }

    // Select sprite based on bird velocity
    val selectedSprite = when {
        bird.velocity < -200f -> charJump3   // Strong upward, arms high
        bird.velocity < 0f -> charJump2      // Rising
        bird.velocity < 150f -> charDefault  // Neutral/gliding
        else -> charFall                     // Falling
    }

    // Continuous background scroll animation (runs in all game states)
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

    Canvas(
        modifier = modifier.background(SkyBlue)
    ) {
        val canvasHeight = size.height

        // Draw scrolling background
        drawScrollingBackground(bgBitmap, bgOffset)

        // Draw all pipes as stock bars
        pipes.forEach { pipe ->
            drawStockBar(pipe, canvasHeight)
        }

        // Draw character sprite at scaled size (centered on bird position)
        val spriteW = bird.width * GameConfig.BIRD_SPRITE_SCALE
        val spriteH = bird.height * GameConfig.BIRD_SPRITE_SCALE
        rotate(
            degrees = bird.rotation,
            pivot = Offset(bird.x, bird.y)
        ) {
            drawImage(
                image = selectedSprite,
                dstOffset = IntOffset(
                    (bird.x - spriteW / 2).toInt(),
                    (bird.y - spriteH / 2).toInt()
                ),
                dstSize = IntSize(spriteW.toInt(), spriteH.toInt())
            )
        }

        // Draw trading floor ground
        drawTradingFloor(canvasHeight)
    }
}

/**
 * Draw the scrolling NYC skyline background.
 * Scales image to fill canvas height and tiles horizontally.
 */
private fun DrawScope.drawScrollingBackground(bitmap: ImageBitmap, offset: Float) {
    val scale = size.height / bitmap.height.toFloat()
    val scaledWidth = (bitmap.width * scale).toInt()
    val scaledHeight = size.height.toInt()
    val canvasWidth = size.width.toInt()

    // Wrap offset to prevent float precision loss over time
    val wrappedOffset = (offset % scaledWidth.toFloat()).toInt()

    // Draw enough copies to cover the full canvas width
    var x = -wrappedOffset
    while (x < canvasWidth) {
        drawImage(
            image = bitmap,
            dstOffset = IntOffset(x, 0),
            dstSize = IntSize(scaledWidth, scaledHeight)
        )
        x += scaledWidth
    }
}

/**
 * Draw a stock chart bar (pipe) - color is stored in the pipe.
 */
private fun DrawScope.drawStockBar(pipe: Pipe, screenHeight: Float) {
    val gapTop = pipe.gapCenterY - pipe.gapHeight / 2
    val gapBottom = pipe.gapCenterY + pipe.gapHeight / 2

    // Use the pipe's stored color (red=bearish, blue=bullish)
    val mainColor = if (pipe.isRedBar) StockRed else StockBlue
    val lightColor = if (pipe.isRedBar) StockRedLight else StockBlueLight
    val darkColor = if (pipe.isRedBar) StockRedDark else StockBlueDark

    // Cap dimensions
    val capWidth = pipe.width * 1.1f
    val capHeight = 24f
    val capOffset = (capWidth - pipe.width) / 2
    val wickWidth = pipe.width * 0.1f

    // Draw top bar — extends off-screen at top, cap only at gap-facing end

    // Main body (from top of screen to just above cap)
    drawRect(
        color = mainColor,
        topLeft = Offset(pipe.x, 0f),
        size = Size(pipe.width, gapTop - capHeight)
    )

    // Highlight (left edge)
    drawRect(
        color = lightColor,
        topLeft = Offset(pipe.x, 0f),
        size = Size(pipe.width * 0.15f, gapTop - capHeight)
    )

    // Shadow (right edge)
    drawRect(
        color = darkColor,
        topLeft = Offset(pipe.x + pipe.width * 0.85f, 0f),
        size = Size(pipe.width * 0.15f, gapTop - capHeight)
    )

    // Cap (gap-facing end only)
    drawRect(
        color = mainColor,
        topLeft = Offset(pipe.x - capOffset, gapTop - capHeight),
        size = Size(capWidth, capHeight)
    )
    drawRect(
        color = lightColor,
        topLeft = Offset(pipe.x - capOffset, gapTop - capHeight),
        size = Size(capWidth * 0.15f, capHeight)
    )

    // Grid lines (trading chart style)
    val gridSpacing = 60f
    var gridY = gridSpacing
    while (gridY < gapTop - capHeight) {
        drawLine(
            color = darkColor.copy(alpha = 0.5f),
            start = Offset(pipe.x, gridY),
            end = Offset(pipe.x + pipe.width, gridY),
            strokeWidth = 1f
        )
        gridY += gridSpacing
    }

    // Draw bottom bar — cap at gap-facing end, extends off-screen at bottom

    // Cap (gap-facing end only)
    drawRect(
        color = mainColor,
        topLeft = Offset(pipe.x - capOffset, gapBottom),
        size = Size(capWidth, capHeight)
    )
    drawRect(
        color = lightColor,
        topLeft = Offset(pipe.x - capOffset, gapBottom),
        size = Size(capWidth * 0.15f, capHeight)
    )

    // Main body (from below cap to bottom of screen)
    drawRect(
        color = mainColor,
        topLeft = Offset(pipe.x, gapBottom + capHeight),
        size = Size(pipe.width, screenHeight - gapBottom - capHeight)
    )

    // Highlight
    drawRect(
        color = lightColor,
        topLeft = Offset(pipe.x, gapBottom + capHeight),
        size = Size(pipe.width * 0.15f, screenHeight - gapBottom - capHeight)
    )

    // Shadow
    drawRect(
        color = darkColor,
        topLeft = Offset(pipe.x + pipe.width * 0.85f, gapBottom + capHeight),
        size = Size(pipe.width * 0.15f, screenHeight - gapBottom - capHeight)
    )

    // Grid lines for bottom
    gridY = gapBottom + capHeight + gridSpacing
    while (gridY < screenHeight) {
        drawLine(
            color = darkColor.copy(alpha = 0.5f),
            start = Offset(pipe.x, gridY),
            end = Offset(pipe.x + pipe.width, gridY),
            strokeWidth = 1f
        )
        gridY += gridSpacing
    }
}

/**
 * Draw the trading floor ground.
 */
private fun DrawScope.drawTradingFloor(screenHeight: Float) {
    val groundHeight = 15f

    // Main floor (dark marble)
    drawRect(
        color = TradingFloor,
        topLeft = Offset(0f, screenHeight - groundHeight),
        size = Size(size.width, groundHeight)
    )

    // Highlight line at top
    drawRect(
        color = TradingFloorLight,
        topLeft = Offset(0f, screenHeight - groundHeight),
        size = Size(size.width, 3f)
    )

    // Gold accent line at bottom
    drawRect(
        color = GoldAccent,
        topLeft = Offset(0f, screenHeight - 3f),
        size = Size(size.width, 3f)
    )

    // Grid pattern
    val gridSpacing = 40f
    var x = 0f
    while (x < size.width) {
        drawLine(
            color = TradingFloorLight.copy(alpha = 0.3f),
            start = Offset(x, screenHeight - groundHeight),
            end = Offset(x, screenHeight),
            strokeWidth = 0.5f
        )
        x += gridSpacing
    }
}
