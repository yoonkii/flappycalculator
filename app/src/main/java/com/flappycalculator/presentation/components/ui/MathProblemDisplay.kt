package com.flappycalculator.presentation.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.domain.model.MathProblem
import com.flappycalculator.presentation.theme.ProblemBackground
import com.flappycalculator.presentation.theme.ProblemText

/**
 * Displays the current math problem.
 * Shows in a semi-transparent container for visibility over the game.
 */
@Composable
fun MathProblemDisplay(
    problem: MathProblem,
    modifier: Modifier = Modifier
) {
    Text(
        text = problem.getDisplayString(),
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(ProblemBackground)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        color = ProblemText,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily
    )
}
