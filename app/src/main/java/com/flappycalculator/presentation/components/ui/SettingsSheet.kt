package com.flappycalculator.presentation.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flappycalculator.presentation.theme.*
import com.flappycalculator.util.SoundManager
import kotlin.math.roundToInt

/**
 * Settings overlay for adjusting BGM volume, SFX volume, and vibration.
 * Presented as a dark-themed panel matching the app's terminal aesthetic.
 */
@Composable
fun SettingsSheet(
    soundManager: SoundManager,
    onDismiss: () -> Unit
) {
    // Local state backed by soundManager properties
    var bgmVol by remember { mutableFloatStateOf(soundManager.bgmVolume) }
    var sfxVol by remember { mutableFloatStateOf(soundManager.sfxVolume) }
    var vibration by remember { mutableStateOf(soundManager.vibrationEnabled) }

    // Full-screen semi-transparent overlay; tapping the backdrop dismisses
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        // Settings card - stop click propagation
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(KeypadBackground)
                .border(
                    width = 1.dp,
                    color = TerminalGreen.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* Consume click to prevent dismiss */ }
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "SETTINGS",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TerminalGreen,
                letterSpacing = 4.sp,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Divider
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(2.dp)
                    .background(TerminalGreen.copy(alpha = 0.4f))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BGM Volume
            SettingsSliderRow(
                label = "BGM VOLUME",
                value = bgmVol,
                onValueChange = { newValue ->
                    bgmVol = newValue
                    soundManager.bgmVolume = newValue
                },
                displayText = "${(bgmVol * 100).roundToInt()}%"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // SFX Volume
            SettingsSliderRow(
                label = "SFX VOLUME",
                value = sfxVol,
                onValueChange = { newValue ->
                    sfxVol = newValue
                    soundManager.sfxVolume = newValue
                },
                displayText = if (sfxVol > 0f) "${(sfxVol * 100).roundToInt()}%" else "OFF"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Vibration toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VIBRATION",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TerminalGreen.copy(alpha = 0.8f),
                    letterSpacing = 2.sp,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (vibration) "ON" else "OFF",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (vibration) TerminalGreen else Color.White.copy(alpha = 0.4f),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Switch(
                        checked = vibration,
                        onCheckedChange = { newValue ->
                            vibration = newValue
                            soundManager.vibrationEnabled = newValue
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TerminalGreen,
                            checkedTrackColor = TerminalGreen.copy(alpha = 0.3f),
                            uncheckedThumbColor = Color.White.copy(alpha = 0.6f),
                            uncheckedTrackColor = Color.White.copy(alpha = 0.1f),
                            uncheckedBorderColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Done button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SubmitButton)
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DONE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily
                )
            }
        }
    }
}

@Composable
private fun SettingsSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    displayText: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TerminalGreen.copy(alpha = 0.8f),
                letterSpacing = 2.sp,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            )

            Text(
                text = displayText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (value > 0f) TerminalGreen else Color.White.copy(alpha = 0.4f),
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f,
            steps = 19, // 20 segments = 5% increments
            colors = SliderDefaults.colors(
                thumbColor = TerminalGreen,
                activeTrackColor = TerminalGreen,
                inactiveTrackColor = TerminalGreen.copy(alpha = 0.15f),
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
