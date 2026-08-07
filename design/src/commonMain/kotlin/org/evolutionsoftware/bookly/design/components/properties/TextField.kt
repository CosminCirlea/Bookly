package org.evolutionsoftware.bookly.design.components.properties

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.theme.TokenProvider

data class TextFieldProperties(
    val label: String? = null,
    val placeholder: String? = null,
    val state: State = State.Default,
) {
    enum class State {
        Default,
        Focused,
        Error,
        Disabled,
        ;

        @Composable
        fun getBackgroundColor(): Color =
            when (this) {
                Disabled -> TokenProvider.colors.bgElevated.copy(alpha = 0.72f)
                else -> TokenProvider.colors.bgElevated
            }

        @Composable
        fun getBorderColor(): Color =
            when (this) {
                Error -> TokenProvider.colors.textDanger
                Focused -> TokenProvider.colors.borderAccent
                Disabled, Default -> Color.Transparent
            }

        // Always 2dp so the field's content does not shift when it gains focus.
        @Composable
        fun getBorderWidth(): Dp = TokenProvider.borderWidths.strong

        @Composable
        fun getTextColor(): Color =
            when (this) {
                Disabled -> TokenProvider.colors.textMuted.copy(alpha = 0.7f)
                else -> TokenProvider.colors.text
            }

        @Composable
        fun getLabelColor(): Color =
            when (this) {
                Disabled -> TokenProvider.colors.textMuted.copy(alpha = 0.8f)
                else -> TokenProvider.colors.text
            }

        @Composable
        fun getPlaceholderColor(): Color = TokenProvider.colors.textMuted.copy(alpha = 0.6f)

        @Composable
        fun getTrailingColor(): Color =
            when (this) {
                Disabled -> TokenProvider.colors.textMuted.copy(alpha = 0.7f)
                else -> TokenProvider.colors.textMuted
            }
    }
}
