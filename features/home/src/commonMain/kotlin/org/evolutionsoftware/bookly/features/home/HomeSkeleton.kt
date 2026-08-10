package org.evolutionsoftware.bookly.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.components.ShimmerBlock
import org.evolutionsoftware.bookly.design.components.rememberShimmerBrush
import org.evolutionsoftware.bookly.design.theme.TokenProvider

/**
 * Loading placeholder for the playroom.
 *
 * Deliberately mirrors [HomeScreen]'s own metrics — same paddings, tile sizes, card
 * shapes and grid spacing — so the real content lands without anything shifting. Only
 * shown when there is nothing cached to display; a warm cache skips straight to books.
 */
@Composable
internal fun HomeSkeleton(modifier: Modifier = Modifier) {
    // One brush for the whole screen, so every placeholder sweeps in step.
    val brush = rememberShimmerBrush()
    val pill = RoundedCornerShape(percent = 50)
    val tile = RoundedCornerShape(TokenProvider.borderRadius.md)

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        // Toolbar: avatar, greeting lines, settings button.
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TokenProvider.spacings.horizontalSpacing,
                        vertical = TokenProvider.spacings.sm,
                    ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
        ) {
            ShimmerBlock(modifier = Modifier.size(44.dp), shape = CircleShape, brush = brush)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xxs + 2.dp),
            ) {
                ShimmerBlock(modifier = Modifier.height(10.dp).width(64.dp), shape = pill, brush = brush)
                ShimmerBlock(modifier = Modifier.height(16.dp).width(128.dp), shape = pill, brush = brush)
            }
            ShimmerBlock(modifier = Modifier.size(44.dp), shape = CircleShape, brush = brush)
        }

        // Search field.
        ShimmerBlock(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = TokenProvider.spacings.horizontalSpacing,
                        end = TokenProvider.spacings.horizontalSpacing,
                        bottom = TokenProvider.spacings.sm,
                    ).height(52.dp),
            shape = tile,
            brush = brush,
        )

        // Filter chips.
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = TokenProvider.spacings.horizontalSpacing,
                        end = TokenProvider.spacings.horizontalSpacing,
                        bottom = TokenProvider.spacings.sm,
                    ),
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
        ) {
            repeat(SKELETON_FILTERS) {
                Column(
                    modifier = Modifier.width(64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs - 2.dp),
                ) {
                    ShimmerBlock(modifier = Modifier.size(56.dp), shape = tile, brush = brush)
                    ShimmerBlock(modifier = Modifier.height(8.dp).width(40.dp), shape = pill, brush = brush)
                }
            }
        }

        // Book grid. A plain Column of rows rather than a lazy grid: the count is fixed
        // and nothing scrolls while loading.
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = TokenProvider.spacings.horizontalSpacing,
                        end = TokenProvider.spacings.horizontalSpacing,
                        top = TokenProvider.spacings.xxs,
                    ),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapMd),
        ) {
            repeat(SKELETON_ROWS) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapMd),
                ) {
                    repeat(2) { column ->
                        SkeletonBookCard(
                            coverShape = skeletonCoverShape(row * 2 + column),
                            brush = brush,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkeletonBookCard(
    coverShape: Shape,
    brush: Brush,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.md),
                    ambientColor = Color(0x0F392E00),
                    spotColor = Color(0x0F392E00),
                ).clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgSurface)
                .padding(TokenProvider.spacings.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ShimmerBlock(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            shape = coverShape,
            brush = brush,
        )
        ShimmerBlock(
            modifier =
                Modifier
                    .padding(
                        top = TokenProvider.spacings.sm,
                        bottom = TokenProvider.spacings.xxs,
                    ).height(12.dp)
                    .width(96.dp),
            shape = RoundedCornerShape(percent = 50),
            brush = brush,
        )
    }
}

private const val SKELETON_FILTERS = 5
private const val SKELETON_ROWS = 2
