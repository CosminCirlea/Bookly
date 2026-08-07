package org.evolutionsoftware.bookly.design.components.properties

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.theme.TokenProvider

sealed interface FeedbackProperties {
    val mainAction: FeedbackAction?
    val secondaryAction: FeedbackAction?
    val icon: Icons?
    val iconTint: (@Composable () -> Color)?
    val iconBackgroundTint: (@Composable () -> Color)?

    data object Loading : FeedbackProperties {
        override val mainAction: FeedbackAction? = null
        override val secondaryAction: FeedbackAction? = null
        override val icon: Icons? = null
        override val iconTint: (@Composable () -> Color)? = null
        override val iconBackgroundTint: (@Composable () -> Color)? = null
    }

    data class Error(
        override val mainAction: FeedbackAction? = null,
        override val secondaryAction: FeedbackAction? = null,
    ) : FeedbackProperties {
        override val icon: Icons = Icons.Warning
        override val iconTint: @Composable () -> Color = { TokenProvider.colors.textInverse }
        override val iconBackgroundTint: @Composable () -> Color = { TokenProvider.colors.textDanger }
    }

    data class Empty(
        override val mainAction: FeedbackAction? = null,
        override val secondaryAction: FeedbackAction? = null,
        override val icon: Icons? = Icons.Book,
        override val iconTint: @Composable () -> Color = { TokenProvider.colors.textInverse },
        override val iconBackgroundTint: @Composable () -> Color = { TokenProvider.colors.bgAccent },
    ) : FeedbackProperties

    data class Custom(
        override val mainAction: FeedbackAction? = null,
        override val secondaryAction: FeedbackAction? = null,
        override val icon: Icons? = null,
        override val iconTint: (@Composable () -> Color)? = null,
        override val iconBackgroundTint: (@Composable () -> Color)? = null,
        val iconModifier: Modifier? = null,
    ) : FeedbackProperties
}

data class FeedbackAction(
    val text: String,
    val onClick: () -> Unit,
) {
    fun toButtonProperties(): ButtonProperties =
        ButtonProperties(
            label = text,
            variant = ButtonProperties.Variant.Primary,
            size = ButtonProperties.Size.Large,
            state = ButtonProperties.State.Default,
        )
}
