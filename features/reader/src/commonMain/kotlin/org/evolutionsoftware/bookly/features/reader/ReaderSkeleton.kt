package org.evolutionsoftware.bookly.features.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.components.ShimmerBlock
import org.evolutionsoftware.bookly.design.components.rememberShimmerBrush
import org.evolutionsoftware.bookly.design.theme.TokenProvider

/**
 * Loading placeholder for the reader.
 *
 * Mirrors the real page: the same 4:5 illustration card sized against available height,
 * a title block where the card's name goes, and a footer of page dots. Sits below the
 * toolbar, which is already showing the book's title by the time this appears.
 */
@Composable
internal fun ReaderSkeleton(
    modifier: Modifier = Modifier,
    pageCount: Int = SKELETON_PAGES,
) {
    val brush = rememberShimmerBrush()
    val pill = RoundedCornerShape(percent = 50)

    Column(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
        ) {
            val contentWidth = maxWidth.coerceAtMost(420.dp)
            // Same sizing rule as the real page, so the card does not resize on arrival.
            val cardWidth =
                minOf(
                    contentWidth,
                    (maxHeight - TITLE_BLOCK_HEIGHT).coerceAtLeast(120.dp) * 4f / 5f,
                )

            Column(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = TokenProvider.spacings.xs)
                        .widthIn(max = contentWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // The card itself stays solid white, as in the reader; only the
                // illustration inside it shimmers.
                Box(
                    modifier =
                        Modifier
                            .width(cardWidth)
                            .aspectRatio(4f / 5f)
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(TokenProvider.borderRadius.xl),
                                ambientColor = Color(0x14392E00),
                                spotColor = Color(0x14392E00),
                            ).clip(RoundedCornerShape(TokenProvider.borderRadius.xl))
                            .background(TokenProvider.colors.bgSurface),
                    contentAlignment = Alignment.Center,
                ) {
                    ShimmerBlock(
                        modifier = Modifier.size(220.dp),
                        shape = CircleShape,
                        brush = brush,
                    )
                }

                ShimmerBlock(
                    modifier =
                        Modifier
                            .padding(top = TokenProvider.spacings.sm)
                            .height(48.dp)
                            .width(180.dp),
                    shape = pill,
                    brush = brush,
                )
            }
        }

        // Footer: page dots and the autoplay control.
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = TokenProvider.spacings.horizontalSpacing,
                        end = TokenProvider.spacings.horizontalSpacing,
                        bottom = TokenProvider.spacings.xl,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(44.dp))
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement =
                    Arrangement.spacedBy(TokenProvider.spacings.xs, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(pageCount) { index ->
                    // Matches the real indicator: active pill first, edge dot tapered.
                    val diameter = if (index == pageCount - 1) 6.dp else 10.dp
                    ShimmerBlock(
                        modifier =
                            Modifier
                                .height(diameter)
                                .width(if (index == 0) 24.dp else diameter),
                        shape = pill,
                        brush = brush,
                    )
                }
            }
            ShimmerBlock(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                brush = brush,
            )
        }
    }
}

private const val SKELETON_PAGES = 5
private val TITLE_BLOCK_HEIGHT = 84.dp
