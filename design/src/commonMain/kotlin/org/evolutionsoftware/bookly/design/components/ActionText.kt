package org.evolutionsoftware.bookly.design.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import org.evolutionsoftware.bookly.design.theme.TokenProvider

/**
 * A tappable inline text link, e.g. "Forgot Password?" or "Create an account".
 *
 * Every action text in the app shares this single color, size, and weight so
 * links never compete visually with the primary button or with each other.
 */
@Composable
fun ActionText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = TokenProvider.colors.textBrand,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        style = TokenProvider.textStyles.actionText,
        color = if (enabled) color else TokenProvider.colors.textMuted,
        textAlign = textAlign,
    )
}
