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
        Loading,
    }

    fun isEnabled(): Boolean = state == State.Default

    fun isLoading(): Boolean = state == State.Loading

    @Composable
    fun getContainerColor(): Color =
        when (state) {
            State.Default, State.Loading -> TokenProvider.colors.bgAccent
            State.Disabled -> DISABLED_CONTAINER
        }

    @Composable
    fun getBaseColor(): Color =
        when (state) {
            State.Default, State.Loading -> TokenProvider.colors.bgAccentPressed
            State.Disabled -> DISABLED_BASE
        }

    @Composable
    fun getContentColor(): Color = TokenProvider.colors.textInverse

    @Composable
    fun getCornerRadius(): Dp = TokenProvider.borderRadius.md

    fun getDepth(): Dp = 6.dp

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

    private companion object {
        val DISABLED_CONTAINER = Color(0xFFCFD2C4)
        val DISABLED_BASE = Color(0xFFB8BBAE)
    }
}
