package org.evolutionsoftware.bookly.design.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

data class ThemeTextStyles(
    val eyebrow: TextStyle,
    val label: TextStyle,
    val body: TextStyle,
    val bodyStrong: TextStyle,
    val input: TextStyle,
    val title: TextStyle,
    val headline: TextStyle,
    val button: TextStyle,
    /** Tappable inline text links, e.g. "Forgot Password?" or "Create an account". */
    val actionText: TextStyle,
    /** Small secondary text: helper copy, metadata, form hints. */
    val caption: TextStyle,
    /** Small secondary text that needs emphasis, e.g. a compact row title. */
    val captionStrong: TextStyle,
    /** Tiny uppercase micro-labels, e.g. eyebrows and tab labels, set in caps by the caller. */
    val overline: TextStyle,
    val cardTitle: TextStyle = bodyStrong,
    val cardSubtitle: TextStyle = caption,
)

fun ThemeTextStyles.toMaterialTypography(): Typography {
    val defaults = Typography()
    return defaults.copy(
        displayLarge = defaults.displayLarge.copy(fontWeight = FontWeight.SemiBold),
        displayMedium = defaults.displayMedium.copy(fontWeight = FontWeight.SemiBold),
        displaySmall = defaults.displaySmall.copy(fontWeight = FontWeight.SemiBold),
        bodySmall = caption,
        bodyMedium = body,
        bodyLarge = input,
        labelLarge = label,
        labelMedium = captionStrong,
        labelSmall = overline,
        titleSmall = bodyStrong,
        titleMedium = title,
        titleLarge = title,
        headlineSmall = headline,
        headlineMedium = headline,
        headlineLarge = headline,
    )
}
