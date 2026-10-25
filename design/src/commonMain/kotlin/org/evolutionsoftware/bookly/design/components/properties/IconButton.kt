package org.evolutionsoftware.bookly.design.components.properties

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.theme.TokenProvider

data class IconButtonProperties(
    val icon: Icons,
    val ariaLabel: String,
    val state: State = State.Default,
) {
    enum class State {
        Default,
        Disabled,
    }

    fun isEnabled(): Boolean = state == State.Default

    @Composable
    fun getContainerColor(): Color {
        return when (state) {
            State.Default -> TokenProvider.colors.bgAccentSoft
            State.Disabled -> TokenProvider.colors.bgDisabled
        }
    }

    @Composable
    fun getIconTint(): Color =
        when (state) {
            State.Default ->
                if (icon == Icons.HeartFilled) {
                    TokenProvider.colors.favorite
                } else {
                    TokenProvider.colors.textBrand
                }
            State.Disabled -> TokenProvider.colors.textMuted.copy(alpha = 0.72f)
        }

}
