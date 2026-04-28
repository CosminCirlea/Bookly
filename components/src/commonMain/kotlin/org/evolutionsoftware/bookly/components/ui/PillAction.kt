package org.evolutionsoftware.bookly.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun PillAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(
                    color = TokenProvider.colors.bgSurface,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.pill),
                ).clickable(onClick = onClick)
                .padding(
                    horizontal = TokenProvider.spacings.md,
                    vertical = TokenProvider.spacings.sm,
                ),
    ) {
        Text(
            text = label,
            style = TokenProvider.textStyles.eyebrow,
            color = TokenProvider.colors.text,
        )
    }
}
