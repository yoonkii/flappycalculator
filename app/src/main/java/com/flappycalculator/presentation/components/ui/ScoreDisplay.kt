package com.flappycalculator.presentation.components.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.presentation.theme.ScoreWhite

/**
 * Displays the current score with an outline for visibility.
 */
@Composable
fun ScoreDisplay(
    score: Int,
    modifier: Modifier = Modifier
) {
    // Text with outline effect using two Text composables overlaid in a Box
    val textStyle = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = MaterialTheme.typography.displayMedium.fontFamily
    )

    Box(modifier = modifier.padding(8.dp)) {
        // Outline text (drawn first, behind)
        Text(
            text = score.toString(),
            style = textStyle.copy(
                color = Color.Black,
                drawStyle = Stroke(
                    width = 8f,
                    join = StrokeJoin.Round
                )
            )
        )

        // Fill text (drawn on top)
        Text(
            text = score.toString(),
            style = textStyle.copy(color = ScoreWhite)
        )
    }
}

/**
 * Compact score display for smaller UI areas.
 */
@Composable
fun ScoreDisplayCompact(
    score: Int,
    label: String = "Score",
    modifier: Modifier = Modifier,
    textColor: Color = ScoreWhite
) {
    Text(
        text = "$label: $score",
        modifier = modifier,
        color = textColor,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = MaterialTheme.typography.titleMedium.fontFamily
    )
}
