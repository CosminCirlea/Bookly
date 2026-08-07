package org.evolutionsoftware.bookly.components.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.theme.TokenProvider

/**
 * Pill toggle matching the Bookly prototype: action-blue track when on,
 * muted sand track when off, with a white sliding knob.
 */
@Composable
fun BooklyToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val trackColor by animateColorAsState(
        targetValue =
            if (checked) {
                TokenProvider.colors.bgAccent
            } else {
                TokenProvider.colors.textMuted.copy(alpha = 0.25f)
            },
    )
    val knobOffset by animateDpAsState(targetValue = if (checked) 20.dp else 0.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            modifier
                .width(48.dp)
                .height(28.dp)
                .clip(CircleShape)
                .background(trackColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) { onCheckedChange(!checked) }
                .padding(2.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .offset(x = knobOffset)
                    .size(24.dp)
                    .shadow(elevation = 2.dp, shape = CircleShape)
                    .background(TokenProvider.colors.bgSurface, CircleShape),
        )
    }
}
