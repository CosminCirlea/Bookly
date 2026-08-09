package org.evolutionsoftware.bookly.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

/**
 * The app's toolbar. Every screen with a leading action uses this, so that action sits
 * at an identical offset throughout.
 *
 * @param onLeadingClick omit for a screen with no leading action; the slot is still
 *   reserved so the title stays optically centred.
 */
@Composable
fun Header(
    properties: HeaderProperties,
    modifier: Modifier = Modifier,
    onLeadingClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val buttonSize = properties.getButtonSize()

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(properties.getBackgroundColor())
                .padding(
                    horizontal = properties.getHorizontalPadding(),
                    vertical = properties.getVerticalPadding(),
                ),
    ) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .size(buttonSize),
            contentAlignment = Alignment.Center,
        ) {
            if (onLeadingClick != null) {
                IconButton(
                    properties =
                        IconButtonProperties(
                            icon = properties.leadingIcon,
                            ariaLabel = properties.leadingAriaLabel,
                        ),
                    onClick = onLeadingClick,
                )
            }
        }

        Text(
            text = properties.title,
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = buttonSize + TokenProvider.spacings.xs),
            style = TokenProvider.textStyles.title,
            color = properties.getTitleColor(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Box(
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .size(buttonSize),
            contentAlignment = Alignment.Center,
        ) {
            trailingContent?.invoke()
        }
    }
}
