package org.evolutionsoftware.bookly.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun BookCard(
    emoji: String,
    title: String,
    description: String,
    badge: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .height(TokenProvider.sizing.bookCardHeight)
                .background(
                    color = TokenProvider.colors.bgSurface,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.lg),
                ).clickable(onClick = onClick)
                .padding(TokenProvider.spacings.lg),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = title,
                style = TokenProvider.textStyles.title,
                color = TokenProvider.colors.text,
            )
            Box(
                modifier =
                    Modifier.background(
                        color = TokenProvider.colors.bgAccentSoft,
                        shape = RoundedCornerShape(TokenProvider.borderRadius.pill),
                    ).padding(
                        horizontal = TokenProvider.spacings.sm,
                        vertical = TokenProvider.spacings.xs,
                    ),
            ) {
                Text(
                    text = badge,
                    style = TokenProvider.textStyles.eyebrow,
                    color = TokenProvider.colors.text,
                )
            }
        }

        Text(
            text = emoji,
            style = TokenProvider.textStyles.headline,
        )

        Text(
            text = description,
            style = TokenProvider.textStyles.body,
            color = TokenProvider.colors.textMuted,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
