package org.evolutionsoftware.bookly.design.components.properties

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.theme.TokenProvider

data class HeaderProperties(
    val title: String,
    val variant: Variant = Variant.Screen,
) {
    enum class Variant {
        Compact,
        Screen,
    }

    fun getButtonSize(): Dp =
        when (variant) {
            Variant.Compact -> 40.dp
            Variant.Screen -> 48.dp
        }

    @Composable
    fun getHorizontalPadding(): Dp =
        when (variant) {
            Variant.Compact -> TokenProvider.spacings.lg
            Variant.Screen -> TokenProvider.spacings.xl
        }

    @Composable
    fun getVerticalPadding(): Dp =
        when (variant) {
            Variant.Compact -> TokenProvider.spacings.sm
            Variant.Screen -> TokenProvider.spacings.lg
        }

    @Composable
    fun getBackgroundColor(): Color =
        TokenProvider.colors.bgBase.copy(
            alpha =
                when (variant) {
                    Variant.Compact -> 0.82f
                    Variant.Screen -> 1f
                },
        )

    @Composable
    fun getTitleColor(): Color = TokenProvider.colors.borderAccent
}
