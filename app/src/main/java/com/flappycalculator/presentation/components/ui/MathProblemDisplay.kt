package com.flappycalculator.presentation.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.domain.model.MathProblem
import com.flappycalculator.presentation.theme.Dseg7FontFamily
import com.flappycalculator.presentation.theme.ProblemBackground
import com.flappycalculator.presentation.theme.ProblemText

/**
 * Displays the current math problem.
 * Numbers use DSEG7 (calculator LCD), operators use regular monospace.
 */
@Composable
fun MathProblemDisplay(
    problem: MathProblem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(ProblemBackground)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Operand 1 — DSEG7
        SegmentText(problem.operand1.toString())
        // Operator — regular font
        SymbolText(" ${problem.operator.symbol} ")
        // Operand 2 — DSEG7
        SegmentText(problem.operand2.toString())
        // = ? — regular font
        SymbolText(" = ?")
    }
}

@Composable
private fun SegmentText(text: String) {
    Text(
        text = text,
        color = ProblemText,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Dseg7FontFamily
    )
}

@Composable
private fun SymbolText(text: String) {
    Text(
        text = text,
        color = ProblemText,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily
    )
}
