package com.flappycalculator.presentation.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.presentation.theme.BackspaceButton
import com.flappycalculator.presentation.theme.BackspaceButtonPressed
import com.flappycalculator.presentation.theme.KeypadBackground
import com.flappycalculator.presentation.theme.KeypadButton as KeypadButtonColor
import com.flappycalculator.presentation.theme.KeypadButtonPressed
import com.flappycalculator.presentation.theme.KeypadText
import com.flappycalculator.presentation.theme.SubmitButton
import com.flappycalculator.presentation.theme.SubmitButtonPressed

/**
 * Calculator-style numeric keypad for answer input.
 * Layout: 3x4 grid with digits, backspace, and submit.
 */
@Composable
fun NumericKeypad(
    onDigit: (Int) -> Unit,
    onBackspace: () -> Unit,
    onSubmit: () -> Unit,
    onKeySound: (() -> Unit)? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(KeypadBackground)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Row 1: 1, 2, 3
        KeypadRow(
            keys = listOf(
                KeypadKey.Digit(1),
                KeypadKey.Digit(2),
                KeypadKey.Digit(3)
            ),
            onDigit = onDigit,
            onBackspace = onBackspace,
            onSubmit = onSubmit,
            onKeySound = onKeySound,
            enabled = enabled
        )

        // Row 2: 4, 5, 6
        KeypadRow(
            keys = listOf(
                KeypadKey.Digit(4),
                KeypadKey.Digit(5),
                KeypadKey.Digit(6)
            ),
            onDigit = onDigit,
            onBackspace = onBackspace,
            onSubmit = onSubmit,
            onKeySound = onKeySound,
            enabled = enabled
        )

        // Row 3: 7, 8, 9
        KeypadRow(
            keys = listOf(
                KeypadKey.Digit(7),
                KeypadKey.Digit(8),
                KeypadKey.Digit(9)
            ),
            onDigit = onDigit,
            onBackspace = onBackspace,
            onSubmit = onSubmit,
            onKeySound = onKeySound,
            enabled = enabled
        )

        // Row 4: Backspace, 0, Submit
        KeypadRow(
            keys = listOf(
                KeypadKey.Backspace,
                KeypadKey.Digit(0),
                KeypadKey.Submit
            ),
            onDigit = onDigit,
            onBackspace = onBackspace,
            onSubmit = onSubmit,
            onKeySound = onKeySound,
            enabled = enabled
        )
    }
}

@Composable
private fun KeypadRow(
    keys: List<KeypadKey>,
    onDigit: (Int) -> Unit,
    onBackspace: () -> Unit,
    onSubmit: () -> Unit,
    onKeySound: (() -> Unit)?,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        keys.forEach { key ->
            KeypadButton(
                key = key,
                onClick = {
                    onKeySound?.invoke()
                    when (key) {
                        is KeypadKey.Digit -> onDigit(key.value)
                        KeypadKey.Backspace -> onBackspace()
                        KeypadKey.Submit -> onSubmit()
                    }
                },
                enabled = enabled,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KeypadButton(
    key: KeypadKey,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        !enabled -> KeypadButtonColor.copy(alpha = 0.5f)
        isPressed -> when (key) {
            is KeypadKey.Digit -> KeypadButtonPressed
            KeypadKey.Backspace -> BackspaceButtonPressed
            KeypadKey.Submit -> SubmitButtonPressed
        }
        else -> when (key) {
            is KeypadKey.Digit -> KeypadButtonColor
            KeypadKey.Backspace -> BackspaceButton
            KeypadKey.Submit -> SubmitButton
        }
    }

    val textColor = if (enabled) KeypadText else KeypadText.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,  // Using color change instead of ripple
                enabled = enabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (key) {
                is KeypadKey.Digit -> key.value.toString()
                KeypadKey.Backspace -> "⌫"
                KeypadKey.Submit -> "✓"
            },
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily
        )
    }
}

/**
 * Sealed class representing keypad key types.
 */
sealed class KeypadKey {
    data class Digit(val value: Int) : KeypadKey()
    data object Backspace : KeypadKey()
    data object Submit : KeypadKey()
}
