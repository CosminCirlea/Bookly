package org.evolutionsoftware.bookly.design.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun Button(
    properties: ButtonProperties,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val enabled = properties.isEnabled()
    val isSecondary = properties.variant == ButtonProperties.Variant.Outlined
    val interactionPressed by interactionSource.collectIsPressedAsState()
    val containerColor by animateColorAsState(
        targetValue =
            if (enabled && interactionPressed) {
                if (isSecondary) {
                    lerp(properties.getContainerColor(), properties.getContentColor(), 0.08f)
                } else {
                    properties.getContainerColor()
                }
            } else {
                properties.getContainerColor()
            },
        animationSpec = tween(durationMillis = 120),
        label = "booklyButtonContainer",
    )
    val depth = properties.getDepth()
    val offsetY by animateDpAsState(
        targetValue = if (interactionPressed && enabled) depth else 0.dp,
        animationSpec = tween(durationMillis = 90, easing = LinearOutSlowInEasing),
        label = "booklyButtonOffset",
    )
    val cornerRadius = properties.getCornerRadius()
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = properties.getMinHeight() + depth),
    ) {
        if (depth > 0.dp) {
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .padding(top = depth)
                        .clip(shape)
                        .background(properties.getBaseColor()),
            )
        }

        Box(
            modifier =
                Modifier
                    .offset(y = offsetY)
                    .clip(shape)
                    .background(containerColor)
                    .then(
                        if (isSecondary) {
                            Modifier.border(TokenProvider.borderWidths.regular, properties.getBorderColor(), shape)
                        } else {
                            Modifier
                        },
                    )
                    .clickable(
                        enabled = enabled,
                        role = Role.Button,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                    .heightIn(min = properties.getMinHeight())
                    .fillMaxWidth()
                    .padding(
                        horizontal = properties.getHorizontalPadding(),
                        vertical = properties.getVerticalPadding(),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (properties.isLoading()) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = properties.getContentColor(),
                    strokeWidth = 3.dp,
                )
            } else {
                Text(
                    text = properties.label,
                    style =
                        when (properties.size) {
                            ButtonProperties.Size.Large -> TokenProvider.textStyles.button
                            ButtonProperties.Size.Medium -> TokenProvider.textStyles.label
                        },
                    color = properties.getContentColor(),
                )
            }
        }
    }
}
