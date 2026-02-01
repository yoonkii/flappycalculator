package com.flappycalculator.presentation.components.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import com.flappycalculator.domain.model.Bird
import com.flappycalculator.domain.model.Pipe
import com.flappycalculator.presentation.theme.*
import kotlin.random.Random

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
    Canvas(
        modifier = modifier.background(SkyBlue)
    ) {
        val canvasHeight = size.height

        // Draw Wall Street background (night skyline)
        drawWallStreetBackground()

        // Draw all pipes as stock bars
        pipes.forEach { pipe ->
            drawStockBar(pipe, canvasHeight)
        }

        // Draw business analyst bird
        drawAnalyst(bird)

        // Draw trading floor ground
        drawTradingFloor(canvasHeight)
    }
}

/**
 * Draw the Wall Street night skyline background.
 */
private fun DrawScope.drawWallStreetBackground() {
    // Night sky gradient
    val skyColors = listOf(
        Color(0xFF0D1B2A),  // Dark blue at top
        Color(0xFF1B263B),  // Mid blue
        Color(0xFF152238),  // Darker blue
        Color(0xFF1A202C)   // Nearly black at horizon
    )

    val bandHeight = size.height / 4
    skyColors.forEachIndexed { index, color ->
        drawRect(
            color = color,
            topLeft = Offset(0f, bandHeight * index),
            size = Size(size.width, bandHeight)
        )
    }

    // Draw distant buildings (simple silhouettes)
    val buildingColor = Color(0xFF2D3748)
    val buildingHeights = listOf(0.35f, 0.45f, 0.3f, 0.55f, 0.4f, 0.5f, 0.35f, 0.6f, 0.45f, 0.38f)
    val buildingWidth = size.width / buildingHeights.size

    buildingHeights.forEachIndexed { index, heightRatio ->
        val buildingHeight = size.height * heightRatio
        val buildingTop = size.height - buildingHeight - 10f

        // Building body
        drawRect(
            color = buildingColor,
            topLeft = Offset(index * buildingWidth, buildingTop),
            size = Size(buildingWidth - 4f, buildingHeight)
        )

        // Window lights (gold dots)
        val windowRows = (buildingHeight / 40).toInt()
        for (row in 0 until windowRows) {
            if (Random.nextFloat() > 0.3f) {  // 70% chance of lit window
                drawRect(
                    color = WindowGold.copy(alpha = 0.8f),
                    topLeft = Offset(
                        index * buildingWidth + buildingWidth / 2 - 3f,
                        buildingTop + 20f + row * 35f
                    ),
                    size = Size(6f, 8f)
                )
            }
        }
    }

    // Freedom Tower silhouette (tallest building)
    val towerWidth = size.width * 0.04f
    val towerX = size.width * 0.5f - towerWidth / 2
    val towerBottom = size.height * 0.4f

    drawRect(
        color = Color(0xFF4A5568),
        topLeft = Offset(towerX, size.height * 0.15f),
        size = Size(towerWidth, towerBottom - size.height * 0.15f)
    )

    // Tower spire
    val spireTop = size.height * 0.08f
    drawPath(
        path = Path().apply {
            moveTo(towerX + towerWidth / 2, spireTop)
            lineTo(towerX, size.height * 0.15f)
            lineTo(towerX + towerWidth, size.height * 0.15f)
            close()
        },
        color = Color(0xFF718096)
    )

    // Stock ticker bar at top
    drawRect(
        color = Color(0xFF1A202C),
        topLeft = Offset(0f, 0f),
        size = Size(size.width, 24f)
    )

    // Ticker symbols (simplified)
    val tickerColors = listOf(TerminalGreen, StockRed, TerminalGreen, TerminalGreen, StockRed, TerminalGreen)
    tickerColors.forEachIndexed { index, color ->
        drawRect(
            color = color,
            topLeft = Offset(20f + index * 60f, 8f),
            size = Size(40f, 3f)
        )
        drawRect(
            color = color,
            topLeft = Offset(30f + index * 60f, 14f),
            size = Size(25f, 3f)
        )
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

    // Draw top bar (candlestick style)

    // Wick (thin line at top)
    drawRect(
        color = darkColor,
        topLeft = Offset(pipe.x + pipe.width / 2 - wickWidth / 2, 0f),
        size = Size(wickWidth, 20f)
    )

    // Main body
    drawRect(
        color = mainColor,
        topLeft = Offset(pipe.x, 20f),
        size = Size(pipe.width, gapTop - capHeight - 20f)
    )

    // Highlight (left edge)
    drawRect(
        color = lightColor,
        topLeft = Offset(pipe.x, 20f),
        size = Size(pipe.width * 0.15f, gapTop - capHeight - 20f)
    )

    // Shadow (right edge)
    drawRect(
        color = darkColor,
        topLeft = Offset(pipe.x + pipe.width * 0.85f, 20f),
        size = Size(pipe.width * 0.15f, gapTop - capHeight - 20f)
    )

    // Top cap
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

    // Draw bottom bar

    // Bottom cap
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

    // Main body
    drawRect(
        color = mainColor,
        topLeft = Offset(pipe.x, gapBottom + capHeight),
        size = Size(pipe.width, screenHeight - gapBottom - capHeight - 20f)
    )

    // Highlight
    drawRect(
        color = lightColor,
        topLeft = Offset(pipe.x, gapBottom + capHeight),
        size = Size(pipe.width * 0.15f, screenHeight - gapBottom - capHeight - 20f)
    )

    // Shadow
    drawRect(
        color = darkColor,
        topLeft = Offset(pipe.x + pipe.width * 0.85f, gapBottom + capHeight),
        size = Size(pipe.width * 0.15f, screenHeight - gapBottom - capHeight - 20f)
    )

    // Wick (thin line at bottom)
    drawRect(
        color = darkColor,
        topLeft = Offset(pipe.x + pipe.width / 2 - wickWidth / 2, screenHeight - 20f),
        size = Size(wickWidth, 20f)
    )

    // Grid lines for bottom
    gridY = gapBottom + capHeight + gridSpacing
    while (gridY < screenHeight - 20f) {
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
 * Draw the business analyst character.
 */
private fun DrawScope.drawAnalyst(bird: Bird) {
    rotate(
        degrees = bird.rotation,
        pivot = Offset(bird.x, bird.y)
    ) {
        val centerX = bird.x
        val centerY = bird.y
        val width = bird.width
        val height = bird.height

        // Body (suit jacket) - navy blue
        drawRect(
            color = SuitNavy,
            topLeft = Offset(centerX - width * 0.35f, centerY - height * 0.1f),
            size = Size(width * 0.7f, height * 0.5f)
        )

        // Suit lapels
        drawPath(
            path = Path().apply {
                moveTo(centerX - width * 0.2f, centerY - height * 0.1f)
                lineTo(centerX, centerY + height * 0.15f)
                lineTo(centerX, centerY + height * 0.4f)
                lineTo(centerX - width * 0.2f, centerY + height * 0.4f)
                close()
            },
            color = SuitNavyLight
        )
        drawPath(
            path = Path().apply {
                moveTo(centerX + width * 0.2f, centerY - height * 0.1f)
                lineTo(centerX, centerY + height * 0.15f)
                lineTo(centerX, centerY + height * 0.4f)
                lineTo(centerX + width * 0.2f, centerY + height * 0.4f)
                close()
            },
            color = SuitNavyLight
        )

        // White shirt
        drawRect(
            color = ShirtWhite,
            topLeft = Offset(centerX - width * 0.1f, centerY - height * 0.1f),
            size = Size(width * 0.2f, height * 0.35f)
        )

        // Red tie
        drawPath(
            path = Path().apply {
                moveTo(centerX - width * 0.05f, centerY - height * 0.1f)
                lineTo(centerX + width * 0.05f, centerY - height * 0.1f)
                lineTo(centerX + width * 0.06f, centerY + height * 0.3f)
                lineTo(centerX, centerY + height * 0.38f)
                lineTo(centerX - width * 0.06f, centerY + height * 0.3f)
                close()
            },
            color = TieRed
        )

        // Head
        drawOval(
            color = SkinTone,
            topLeft = Offset(centerX - width * 0.3f, centerY - height * 0.5f),
            size = Size(width * 0.6f, height * 0.45f)
        )

        // Hair
        drawArc(
            color = HairDark,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(centerX - width * 0.32f, centerY - height * 0.55f),
            size = Size(width * 0.64f, height * 0.25f)
        )

        // Eyes
        val eyeSize = width * 0.1f
        val eyeY = centerY - height * 0.3f

        // Left eye
        drawCircle(
            color = Color.White,
            radius = eyeSize,
            center = Offset(centerX - width * 0.12f, eyeY)
        )
        drawCircle(
            color = Color.Black,
            radius = eyeSize * 0.5f,
            center = Offset(centerX - width * 0.1f, eyeY)
        )

        // Right eye
        drawCircle(
            color = Color.White,
            radius = eyeSize,
            center = Offset(centerX + width * 0.12f, eyeY)
        )
        drawCircle(
            color = Color.Black,
            radius = eyeSize * 0.5f,
            center = Offset(centerX + width * 0.14f, eyeY)
        )

        // Determined smile
        drawArc(
            color = Color(0xFF8B4513),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(centerX - width * 0.1f, centerY - height * 0.2f),
            size = Size(width * 0.2f, height * 0.1f)
        )

        // Arms (based on velocity - up when rising, down when falling)
        val armAngle = if (bird.velocity < 0) -30f else 30f
        val armColor = SuitNavy

        // Left arm
        rotate(armAngle, Offset(centerX - width * 0.35f, centerY)) {
            drawRect(
                color = armColor,
                topLeft = Offset(centerX - width * 0.55f, centerY - height * 0.05f),
                size = Size(width * 0.25f, height * 0.15f)
            )
            // Hand
            drawCircle(
                color = SkinTone,
                radius = width * 0.08f,
                center = Offset(centerX - width * 0.55f, centerY)
            )
        }

        // Right arm
        rotate(-armAngle, Offset(centerX + width * 0.35f, centerY)) {
            drawRect(
                color = armColor,
                topLeft = Offset(centerX + width * 0.3f, centerY - height * 0.05f),
                size = Size(width * 0.25f, height * 0.15f)
            )
            // Hand
            drawCircle(
                color = SkinTone,
                radius = width * 0.08f,
                center = Offset(centerX + width * 0.55f, centerY)
            )
        }

        // Briefcase (below the analyst)
        drawRect(
            color = Color(0xFF8B4513),  // Brown
            topLeft = Offset(centerX - width * 0.2f, centerY + height * 0.4f),
            size = Size(width * 0.4f, height * 0.2f)
        )
        // Briefcase handle
        drawRect(
            color = GoldAccent,
            topLeft = Offset(centerX - width * 0.08f, centerY + height * 0.38f),
            size = Size(width * 0.16f, height * 0.05f)
        )
        // Briefcase latch
        drawRect(
            color = GoldAccent,
            topLeft = Offset(centerX - width * 0.05f, centerY + height * 0.48f),
            size = Size(width * 0.1f, height * 0.04f)
        )
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
