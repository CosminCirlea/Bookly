package org.evolutionsoftware.bookly.design.components.properties

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.theme.TokenProvider

data class ButtonProperties(
    val label: String,
    val size: Size = Size.Large,
    val variant: Variant = Variant.Primary,
    val state: State = State.Default,
) {
    enum class Size {
        Medium,
        Large,
    }

    enum class Variant {
        Primary,
    }

    enum class State {
        Default,
        Disabled,
    }

    fun isEnabled(): Boolean = state == State.Default

    @Composable
    fun getContainerColor(): Color =
        when (state) {
            State.Default -> TokenProvider.colors.bgAccent
            State.Disabled -> TokenProvider.colors.textMuted.copy(alpha = 0.28f)
        }

    @Composable
    fun getBaseColor(): Color =
        when (state) {
            State.Default -> TokenProvider.colors.bgAccentPressed
            State.Disabled -> Color.Transparent
        }

    @Composable
    fun getContentColor(): Color = TokenProvider.colors.textInverse

    @Composable
    fun getCornerRadius(): Dp = TokenProvider.borderRadius.md

    fun getDepth(): Dp =
        when (state) {
            State.Default -> 6.dp
            State.Disabled -> 0.dp
        }

    @Composable
    fun getHorizontalPadding(): Dp = TokenProvider.spacings.lg

    @Composable
    fun getVerticalPadding(): Dp =
        when (size) {
            Size.Medium -> TokenProvider.spacings.md
            Size.Large -> TokenProvider.spacings.md
        }

    fun getMinHeight(): Dp =
        when (size) {
            Size.Medium -> 56.dp
            Size.Large -> 60.dp
        }
}
