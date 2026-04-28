package org.evolutionsoftware.bookly.features.auth.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
internal fun AuthIllustration(symbol: String) {
    Box(
        modifier =
            Modifier
                .size(TokenProvider.sizing.toolbarHeight + TokenProvider.spacings.xl)
                .clip(CircleShape)
                .background(TokenProvider.colors.bgAccentSoft),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol,
            color = TokenProvider.colors.text,
            style = TokenProvider.textStyles.headline.copy(fontWeight = FontWeight.Black),
        )
    }
}

@Composable
internal fun PasswordSuffix(
    visible: Boolean,
    onClick: () -> Unit,
) {
    Text(
        text = if (visible) "🙈" else "👁",
        modifier = Modifier.clickable(onClick = onClick),
        style = TokenProvider.textStyles.bodyStrong,
        color = TokenProvider.colors.textMuted,
    )
}

internal fun resolveDisplayName(emailOrPhone: String): String {
    val normalized = emailOrPhone.trim()
    if (normalized.isBlank()) return ""
    val nameSource = normalized.substringBefore("@").substringBefore("+").replace(".", " ").replace("-", " ")
    return nameSource
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") { part -> part.replaceFirstChar { char -> char.uppercase() } }
        .ifBlank { normalized }
}

internal fun primaryButtonProperties(
    label: String,
    enabled: Boolean,
): ButtonProperties =
    ButtonProperties(
        label = label,
        size = ButtonProperties.Size.Large,
        state = if (enabled) ButtonProperties.State.Default else ButtonProperties.State.Disabled,
    )
