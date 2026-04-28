package org.evolutionsoftware.bookly.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun FilterChips(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = TokenProvider.spacings.lg),
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
    ) {
        filters.forEach { filter ->
            val selected = filter == selectedFilter
            Text(
                text = filter,
                modifier =
                    Modifier
                        .background(
                            color = if (selected) TokenProvider.colors.bgAccent else TokenProvider.colors.bgSurface,
                            shape = RoundedCornerShape(TokenProvider.borderRadius.pill),
                        ).clickable { onFilterSelected(filter) }
                        .padding(
                            horizontal = TokenProvider.spacings.md,
                            vertical = TokenProvider.spacings.sm,
                        ),
                style = TokenProvider.textStyles.eyebrow,
                color = if (selected) TokenProvider.colors.textInverse else TokenProvider.colors.text,
            )
        }
    }
}
