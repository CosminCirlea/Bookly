package org.evolutionsoftware.bookly.components.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.painterResource

/**
 * Secondary "ghost" button from the Bookly prototype: soft cream fill with
 * dark text, no extruded depth.
 */
@Composable
fun PlayroomGhostButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Icons? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        label = "ghostButtonScale",
    )

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .scale(scale)
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgElevated)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                )
                .heightIn(min = 56.dp)
                .padding(
                    horizontal = TokenProvider.spacings.lg,
                    vertical = TokenProvider.spacings.md,
                ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon.icon),
                contentDescription = null,
                tint = TokenProvider.colors.text,
                modifier =
                    Modifier
                        .padding(end = TokenProvider.spacings.xs)
                        .size(20.dp),
            )
        }
        Text(
            text = label,
            style = TokenProvider.textStyles.title.copy(fontSize = 18.sp),
            color = TokenProvider.colors.text,
        )
    }
}
