package org.evolutionsoftware.bookly.design.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

data class ThemeColors(
    val bgBase: Color,
    val bgSurface: Color,
    val bgElevated: Color,
    val bgAccent: Color,
    val bgAccentPressed: Color,
    val bgAccentSoft: Color,
    val bgAccentStrong: Color,
    val bgSuccessSoft: Color,
    val bgInfoSoft: Color,
    val bgWarningSoft: Color,
    val bgDangerSoft: Color,
    val text: Color,
    val textMuted: Color,
    val textSubtle: Color,
    val textInverse: Color,
    val textAccent: Color,
    val textBrand: Color,
    val textSuccess: Color,
    val textDanger: Color,
    val border: Color,
    val borderAccent: Color,
    val success: Color,
    val warning: Color,
    val favorite: Color,
    val socialGoogle: Color,
    val socialFacebook: Color,
)

fun ThemeColors.toMaterialColors(darkTheme: Boolean): ColorScheme =
    if (darkTheme) {
        darkColorScheme(
            primary = bgAccent,
            secondary = warning,
            surface = bgSurface,
            background = bgBase,
            onPrimary = textInverse,
            onSecondary = textInverse,
            onBackground = text,
            onSurface = text,
        )
    } else {
        lightColorScheme(
            primary = bgAccent,
            secondary = warning,
            surface = bgSurface,
            background = bgBase,
            onPrimary = textInverse,
            onSecondary = textInverse,
            onBackground = text,
            onSurface = text,
        )
    }
