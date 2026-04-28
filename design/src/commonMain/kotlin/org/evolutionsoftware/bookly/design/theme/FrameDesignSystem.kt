package org.evolutionsoftware.bookly.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun FrameDesignSystem(
    themeProvider: ThemeProvider,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalTokens provides themeProvider) {
        MaterialTheme(
            colorScheme = themeProvider.colors.toMaterialColors(darkTheme),
            typography = themeProvider.textStyles.toMaterialTypography(),
            content = content,
        )
    }
}

interface ThemeProvider {
    val textStyles: ThemeTextStyles
    val borderRadius: ThemeBorderRadius
    val sizing: ThemeSizing
    val spacings: ThemeSpacing
    val colors: ThemeColors
    val lineHeights: ThemeLineHeights
    val fontSizes: ThemeFontSizes
    val borderWidths: ThemeBorderWidth
}

private val LocalTokens = staticCompositionLocalOf<ThemeProvider?> { null }

@Composable
private fun resolveThemeProvider(): ThemeProvider =
    LocalTokens.current ?: error("No ThemeProvider provided. Wrap your content in FrameDesignSystem.")

object TokenProvider {
    val textStyles: ThemeTextStyles
        @Composable get() = resolveThemeProvider().textStyles

    val borderRadius: ThemeBorderRadius
        @Composable get() = resolveThemeProvider().borderRadius

    val sizing: ThemeSizing
        @Composable get() = resolveThemeProvider().sizing

    val spacings: ThemeSpacing
        @Composable get() = resolveThemeProvider().spacings

    val colors: ThemeColors
        @Composable get() = resolveThemeProvider().colors

    val lineHeights: ThemeLineHeights
        @Composable get() = resolveThemeProvider().lineHeights

    val fontSizes: ThemeFontSizes
        @Composable get() = resolveThemeProvider().fontSizes

    val borderWidths: ThemeBorderWidth
        @Composable get() = resolveThemeProvider().borderWidths
}
