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
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun Header(
    properties: HeaderProperties,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        IconButton(
            modifier =
                Modifier
                    .align(Alignment.CenterStart),
            properties =
                IconButtonProperties(
                    icon = Icons.ArrowLeft,
                    ariaLabel = "Back",
                ),
            onClick = onBackClick,
        )

        Text(
            text = properties.title,
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = buttonSize),
            style = TokenProvider.textStyles.title,
            color = properties.getTitleColor(),
            textAlign = TextAlign.Center,
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
