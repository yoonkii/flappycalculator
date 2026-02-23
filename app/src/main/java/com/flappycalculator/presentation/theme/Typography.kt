package com.flappycalculator.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.flappycalculator.R

// System monospace for general UI text
val GameFontFamily = FontFamily.Monospace

// DSEG7 Classic Bold - 7-segment LCD font for numeric displays (score, input, keypad)
val Dseg7FontFamily = FontFamily(Font(R.font.dseg7_classic_bold, FontWeight.Bold))

// DSEG14 Classic Bold - 14-segment LCD font for math problems (supports +, -, =, ?)
val Dseg14FontFamily = FontFamily(Font(R.font.dseg14_classic_bold, FontWeight.Bold))

val GameTypography = Typography(
    // Large display text (Score display, Game Over)
    displayLarge = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        letterSpacing = 2.sp
    ),
    displayMedium = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        letterSpacing = 1.5.sp
    ),
    displaySmall = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 1.sp
    ),

    // Headlines (Screen titles)
    headlineLarge = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),

    // Titles (Math problems, buttons)
    titleLarge = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),

    // Body text
    bodyLarge = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    // Labels (Keypad, small UI elements)
    labelLarge = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = GameFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)
