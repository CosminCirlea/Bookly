package org.evolutionsoftware.bookly.design.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

data class ThemeTextStyles(
    val eyebrow: TextStyle,
    val label: TextStyle,
    val body: TextStyle,
    val bodyStrong: TextStyle,
    val input: TextStyle,
    val title: TextStyle,
    val headline: TextStyle,
    val button: TextStyle,
)

fun ThemeTextStyles.toMaterialTypography(): Typography =
    Typography(
        bodySmall = eyebrow,
        bodyMedium = body,
        bodyLarge = input,
        labelLarge = label,
        titleMedium = title,
        headlineSmall = headline,
    )
