package org.evolutionsoftware.bookly.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun LearningCard(
    emoji: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    color = TokenProvider.colors.bgSurface,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.lg),
                ).padding(TokenProvider.spacings.xl),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .background(
                        color = TokenProvider.colors.bgAccentSoft,
                        shape = RoundedCornerShape(TokenProvider.borderRadius.lg),
                    ).padding(TokenProvider.spacings.xl),
        ) {
            Text(
                text = emoji,
                style = TokenProvider.textStyles.headline,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
        ) {
            Text(
                text = title,
                style = TokenProvider.textStyles.headline,
                color = TokenProvider.colors.text,
                textAlign = TextAlign.Center,
            )
            Text(
                text = description,
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
            )
        }
    }
}
