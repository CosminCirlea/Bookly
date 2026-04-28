package org.evolutionsoftware.bookly.design.components.properties

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.theme.TokenProvider

data class IconButtonProperties(
    val icon: Icons,
    val ariaLabel: String,
    val state: State = State.Default,
    val buttonSize: Dp = 48.dp,
    val iconSize: Dp = 24.dp,
) {
    enum class State {
        Default,
        Disabled,
    }

    fun isEnabled(): Boolean = state == State.Default

    @Composable
    fun getContainerColor(): Color =
        when (state) {
            State.Default -> TokenProvider.colors.bgAccentSoft
            State.Disabled -> TokenProvider.colors.bgAccentSoft.copy(alpha = 0.48f)
        }

    @Composable
    fun getIconTint(): Color =
        when (state) {
            State.Default -> TokenProvider.colors.text
            State.Disabled -> TokenProvider.colors.textMuted.copy(alpha = 0.72f)
        }

}
