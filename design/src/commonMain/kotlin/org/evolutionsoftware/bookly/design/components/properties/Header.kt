package org.evolutionsoftware.bookly.design.components.properties

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
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
    @Composable
    fun getButtonSize(): Dp = TokenProvider.sizing.iconButtonSize

    @Composable
    fun getHorizontalPadding(): Dp = TokenProvider.spacings.lg

    @Composable
    fun getVerticalPadding(): Dp = TokenProvider.spacings.sm

    @Composable
    fun getBackgroundColor(): Color = TokenProvider.colors.bgBase

    @Composable
    fun getTitleColor(): Color = TokenProvider.colors.text

}
