package org.evolutionsoftware.bookly.design.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

private val SHIMMER_BASE = Color(0xFFFFF0C4)
private val SHIMMER_HIGHLIGHT = Color(0xFFFDF6DD)

/** Width of the travelling highlight band. */
private val SHIMMER_BAND = 360.dp
private const val SHIMMER_DURATION_MS = 1_400

/**
 * The sweeping gradient used by loading skeletons.
 *
 * A single transition drives every block that reads this brush, so a whole skeleton
 * shimmers in step rather than each placeholder animating on its own phase.
 */
@Composable
fun rememberShimmerBrush(): Brush {
    val bandPx = with(LocalDensity.current) { SHIMMER_BAND.toPx() }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offset by transition.animateFloat(
        initialValue = -bandPx,
        targetValue = bandPx,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = SHIMMER_DURATION_MS, easing = LinearEasing),
            ),
        label = "shimmerOffset",
    )

    return Brush.linearGradient(
        colorStops =
            arrayOf(
                0f to SHIMMER_BASE,
                0.4f to SHIMMER_HIGHLIGHT,
                0.8f to SHIMMER_BASE,
            ),
        start = Offset(offset, 0f),
        end = Offset(offset + bandPx, 0f),
    )
}

/**
 * A single placeholder in a loading skeleton. Size it with [modifier] to match the
 * real content it stands in for, so nothing shifts when the content arrives.
 */
@Composable
fun ShimmerBlock(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    brush: Brush = rememberShimmerBrush(),
) {
    Box(
        modifier =
            modifier
                .clip(shape)
                .background(brush),
    )
}
