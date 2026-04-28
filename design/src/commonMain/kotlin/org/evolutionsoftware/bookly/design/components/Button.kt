package org.evolutionsoftware.bookly.design.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
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
    var pressed by remember { mutableStateOf(false) }
    val depth = properties.getDepth()
    val offsetY by animateDpAsState(
        targetValue = if (pressed && enabled) depth else 0.dp,
        animationSpec = tween(durationMillis = 90, easing = LinearOutSlowInEasing),
        label = "booklyButtonOffset",
    )
    val cornerRadius = properties.getCornerRadius()
    val pressModifier =
        if (enabled) {
            Modifier.pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown(requireUnconsumed = false)
                        pressed = true
                        waitForUpOrCancellation()
                        pressed = false
                    }
                }
            }
        } else {
            Modifier
        }

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
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(properties.getBaseColor()),
            )
        }

        Box(
            modifier =
                Modifier
                    .offset(y = offsetY)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(properties.getContainerColor())
                    .clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                    .then(pressModifier)
                    .heightIn(min = properties.getMinHeight())
                    .fillMaxWidth()
                    .padding(
                        horizontal = properties.getHorizontalPadding(),
                        vertical = properties.getVerticalPadding(),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = properties.label,
                style =
                    when (properties.size) {
                        ButtonProperties.Size.Large -> TokenProvider.textStyles.button
                        ButtonProperties.Size.Medium -> TokenProvider.textStyles.title.copy(fontWeight = FontWeight.ExtraBold)
                    },
                color = properties.getContentColor(),
            )
        }
    }
}
