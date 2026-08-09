package org.evolutionsoftware.bookly.design.components.properties

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.theme.TokenProvider

/**
 * The app's single toolbar.
 *
 * Metrics are deliberately fixed rather than varied per screen: the leading button
 * must land in the same place everywhere, so navigating between screens never shifts
 * it. Anything that needs to differ goes in the title or the trailing slot.
 */
data class HeaderProperties(
    val title: String,
    /** Leading glyph. The reader closes rather than goes back, hence the override. */
    val leadingIcon: Icons = Icons.ArrowLeft,
    val leadingAriaLabel: String = "Back",
) {
    /** Matches the design system's circular icon button. */
    fun getButtonSize(): Dp = BUTTON_SIZE

    @Composable
    fun getHorizontalPadding(): Dp = TokenProvider.spacings.lg

    @Composable
    fun getVerticalPadding(): Dp = TokenProvider.spacings.sm

    @Composable
    fun getBackgroundColor(): Color = TokenProvider.colors.bgBase

    @Composable
    fun getTitleColor(): Color = TokenProvider.colors.borderAccent

    private companion object {
        val BUTTON_SIZE = 44.dp
    }
}
