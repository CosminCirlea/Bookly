package org.evolutionsoftware.bookly.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.IconButton
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun AppToolbar(
    title: String,
    subtitle: String,
    profileLabel: String?,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(TokenProvider.sizing.toolbarHeight)
                .padding(horizontal = TokenProvider.spacings.lg),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(TokenProvider.sizing.toolbarHeight - TokenProvider.spacings.sm)
                        .clip(CircleShape)
                        .background(TokenProvider.colors.bgAccentSoft),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = profileLabel ?: "G",
                    style = TokenProvider.textStyles.title,
                    color = TokenProvider.colors.text,
                    textAlign = TextAlign.Center,
                )
            }

            Column {
                Text(
                    text = title,
                    style = TokenProvider.textStyles.title,
                    color = TokenProvider.colors.text,
                )
                Text(
                    text = subtitle,
                    style = TokenProvider.textStyles.eyebrow,
                    color = TokenProvider.colors.textMuted,
                )
            }
        }

        IconButton(
            properties =
                IconButtonProperties(
                    icon = Icons.Settings,
                    ariaLabel = "Settings",
                ),
            onClick = onSettingsClick,
        )
    }
}
