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
        Outlined,
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
        when (variant) {
            Variant.Primary ->
                when (state) {
                    State.Default, State.Loading -> TokenProvider.colors.bgPrimaryButton
                    State.Disabled -> TokenProvider.colors.bgDisabled
                }
            Variant.Outlined ->
                when (state) {
                    State.Default, State.Loading -> TokenProvider.colors.bgSecondaryButton
                    State.Disabled -> TokenProvider.colors.bgDisabled
                }
        }

    @Composable
    fun getBaseColor(): Color =
        when (variant) {
            Variant.Primary ->
                when (state) {
                    State.Default, State.Loading -> TokenProvider.colors.bgPrimaryButtonPressed
                    State.Disabled -> TokenProvider.colors.bgDisabledBase
                }
            Variant.Outlined ->
                when (state) {
                    State.Default, State.Loading -> TokenProvider.colors.border
                    State.Disabled -> TokenProvider.colors.bgDisabledBase
                }
        }

    @Composable
    fun getContentColor(): Color =
        when (variant) {
            Variant.Primary ->
                if (state == State.Disabled) TokenProvider.colors.textMuted else TokenProvider.colors.textPrimaryButton
            Variant.Outlined ->
                when (state) {
                    State.Default, State.Loading -> TokenProvider.colors.textSecondaryButton
                    State.Disabled -> TokenProvider.colors.textMuted
                }
        }

    @Composable
    fun getBorderColor(): Color =
        when (state) {
            State.Default, State.Loading -> TokenProvider.colors.bgSecondaryButton
            State.Disabled -> TokenProvider.colors.textMuted.copy(alpha = 0.16f)
        }

    @Composable
    fun getCornerRadius(): Dp = TokenProvider.borderRadius.md

    fun getDepth(): Dp =
        when (variant) {
            Variant.Primary -> 6.dp
            Variant.Outlined -> 0.dp
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
            Size.Medium -> 48.dp
            Size.Large -> 52.dp
        }
}
