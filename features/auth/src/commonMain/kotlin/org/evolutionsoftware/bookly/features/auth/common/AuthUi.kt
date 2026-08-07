package org.evolutionsoftware.bookly.features.auth.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.painterResource

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
    Image(
        painter = painterResource(if (visible) Icons.EyeOff.icon else Icons.Eye.icon),
        contentDescription = null,
        modifier =
            Modifier
                .clickable(onClick = onClick)
                .width(22.dp)
                .height(if (visible) 20.dp else 15.dp),
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
    loading: Boolean = false,
): ButtonProperties =
    ButtonProperties(
        label = label,
        size = ButtonProperties.Size.Large,
        state =
            when {
                loading -> ButtonProperties.State.Loading
                enabled -> ButtonProperties.State.Default
                else -> ButtonProperties.State.Disabled
            },
    )
