package org.evolutionsoftware.bookly.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.components.ui.PlayroomSecondaryChip
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.IconButton
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeRoute(
    refreshKey: Int,
    onBookSelected: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val viewModel = rememberHomeViewModel()
    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(refreshKey) {
        viewModel.onUserIntent(HomeIntent.Load)
    }

    HomeScreen(
        state = state,
        onIntent = viewModel::onUserIntent,
        onBookSelected = onBookSelected,
        onSettingsClick = onSettingsClick,
    )
}

@Composable
private fun HomeScreen(
    state: HomeViewState,
    onIntent: (HomeIntent) -> Unit,
    onBookSelected: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val primaryFilters =
        listOf(
            "All" to "All Nature",
            "Animals" to "Animals",
            "Plants" to "Plants",
            "Weather" to "Weather",
        )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    TokenProvider.colors.bgBase,
                                    TokenProvider.colors.bgBase.copy(alpha = 0.92f),
                                ),
                        ),
                ),
    ) {
        LazyVerticalGrid(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = 148.dp),
            columns = GridCells.Fixed(2),
            contentPadding =
                PaddingValues(
                    start = TokenProvider.spacings.md,
                    end = TokenProvider.spacings.md,
                    top = TokenProvider.spacings.lg,
                    bottom = TokenProvider.spacings.xxl,
                ),
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
        ) {
            items(state.visibleBooks) { book ->
                PlayroomBookCard(
                    book = book,
                    onClick = { onBookSelected(book.id) },
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            PlayroomHeader(
                title = "Playroom",
                profile = state.profile,
                onSettingsClick = onSettingsClick,
            )

            LazyRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = TokenProvider.spacings.md),
                contentPadding = PaddingValues(horizontal = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
            ) {
                lazyItems(primaryFilters) { filter ->
                    PlayroomSecondaryChip(
                        label = filter.second,
                        selected = state.selectedFilter == filter.first,
                        onClick = { onIntent(HomeIntent.FilterSelected(filter.first)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayroomHeader(
    title: String,
    profile: ParentProfile?,
    onSettingsClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = TokenProvider.spacings.lg,
                    vertical = TokenProvider.spacings.md,
                ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(TokenProvider.colors.bgAccentSoft)
                        .padding(3.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(TokenProvider.colors.bgAccentStrong),
                    contentAlignment = Alignment.Center,
                ) {
                    androidx.compose.material3.Text(
                        text = profile?.initials ?: "JR",
                        color = TokenProvider.colors.textInverse,
                        style = TokenProvider.textStyles.bodyStrong,
                    )
                }
            }
            androidx.compose.material3.Text(
                text = title,
                style = TokenProvider.textStyles.headline.copy(fontWeight = FontWeight.ExtraBold),
                color = TokenProvider.colors.textAccent,
            )
        }

        IconButton(
            modifier =
                Modifier
                    .align(Alignment.CenterEnd),
            properties =
                IconButtonProperties(
                    icon = Icons.Settings,
                    ariaLabel = "Settings",
                ),
            onClick = onSettingsClick,
            content = {
                androidx.compose.material3.Icon(
                    painter = painterResource(Icons.Settings.icon),
                    contentDescription = "Settings",
                    tint = TokenProvider.colors.borderAccent,
                )
            },
        )
    }
}

@Composable
private fun PlayroomBookCard(
    book: BookSummary,
    onClick: () -> Unit,
) {
    val shape =
        when (book.id) {
            "forest-animals" -> androidx.compose.foundation.shape.RoundedCornerShape(topStart = 56.dp, topEnd = 100.dp, bottomStart = 44.dp, bottomEnd = 100.dp)
            "birds" -> androidx.compose.foundation.shape.RoundedCornerShape(topStart = 84.dp, topEnd = 28.dp, bottomStart = 48.dp, bottomEnd = 90.dp)
            "garden-veggies" -> androidx.compose.foundation.shape.RoundedCornerShape(topStart = 36.dp, topEnd = 104.dp, bottomStart = 98.dp, bottomEnd = 42.dp)
            "bugs-insects" -> androidx.compose.foundation.shape.RoundedCornerShape(topStart = 92.dp, topEnd = 48.dp, bottomStart = 72.dp, bottomEnd = 104.dp)
            "river-life" -> androidx.compose.foundation.shape.RoundedCornerShape(topStart = 84.dp, topEnd = 28.dp, bottomStart = 48.dp, bottomEnd = 90.dp)
            else -> androidx.compose.foundation.shape.RoundedCornerShape(topStart = 36.dp, topEnd = 104.dp, bottomStart = 98.dp, bottomEnd = 42.dp)
        }
    val background =
        when (book.id) {
            "forest-animals" -> Brush.verticalGradient(listOf(Color(0xFF375C53), Color(0xFF0A4978)))
            "birds" -> Brush.verticalGradient(listOf(Color(0xFF6E5325), Color(0xFF33210B)))
            "garden-veggies" -> Brush.verticalGradient(listOf(Color(0xFF4DA84E), Color(0xFF9CCC65)))
            "bugs-insects" -> Brush.verticalGradient(listOf(Color(0xFFFFC48A), Color(0xFFF58E62)))
            "river-life" -> Brush.verticalGradient(listOf(Color(0xFF7DB6D8), Color(0xFF275F8E)))
            else -> Brush.verticalGradient(listOf(Color(0xFF0B3554), Color(0xFF274C72)))
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(208.dp)
                .background(
                    TokenProvider.colors.bgSurface,
                    androidx.compose.foundation.shape.RoundedCornerShape(TokenProvider.borderRadius.xl),
                ).clickable(onClick = onClick)
                .padding(TokenProvider.spacings.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(118.dp)
                    .clip(shape)
                    .background(background),
            contentAlignment = Alignment.Center,
        ) {
            if (book.id == "night-sky") {
                Box(
                    modifier =
                        Modifier
                            .size(50.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color(0xFFFFE06A)),
                )
                Box(
                    modifier =
                        Modifier
                            .padding(
                                start = TokenProvider.spacings.lg,
                                bottom = TokenProvider.spacings.lg,
                            ).size(34.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color(0xFF204562)),
                )
            } else {
                androidx.compose.material3.Text(
                    text = book.emoji,
                    style = TokenProvider.textStyles.headline.copy(fontWeight = FontWeight.Black),
                )
            }
        }
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xs))
        androidx.compose.material3.Text(
            text = book.title,
            style = TokenProvider.textStyles.bodyStrong.copy(fontWeight = FontWeight.ExtraBold),
            color = TokenProvider.colors.text,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}
