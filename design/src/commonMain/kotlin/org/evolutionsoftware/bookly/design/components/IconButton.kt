package org.evolutionsoftware.bookly.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.jetbrains.compose.resources.painterResource

@Composable
fun IconButton(
    properties: IconButtonProperties,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(properties.getContainerColor())
                .clickable(
                    enabled = properties.isEnabled(),
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (content != null) {
            content()
        } else {
            Icon(
                painter = painterResource(properties.icon.icon),
                contentDescription = properties.ariaLabel,
                modifier = Modifier.size(24.dp),
                tint = properties.getIconTint(),
            )
        }
    }
}
