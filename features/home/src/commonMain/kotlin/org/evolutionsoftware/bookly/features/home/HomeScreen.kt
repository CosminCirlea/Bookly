package org.evolutionsoftware.bookly.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.home.generated.resources.Res
import bookly.features.home.generated.resources.home_favorite_add_aria
import bookly.features.home.generated.resources.home_empty_filter_body
import bookly.features.home.generated.resources.home_empty_filter_title
import bookly.features.home.generated.resources.home_empty_library_body
import bookly.features.home.generated.resources.home_empty_library_title
import bookly.features.home.generated.resources.home_error_body
import bookly.features.home.generated.resources.home_error_retry
import bookly.features.home.generated.resources.home_error_title
import bookly.features.home.generated.resources.home_loading_body
import bookly.features.home.generated.resources.home_loading_title
import bookly.features.home.generated.resources.home_favorite_added
import bookly.features.home.generated.resources.home_favorite_failed
import bookly.features.home.generated.resources.home_favorite_remove_aria
import bookly.features.home.generated.resources.home_favorite_removed
import bookly.features.home.generated.resources.home_filter_sheet_done
import bookly.features.home.generated.resources.home_filter_sheet_reset
import bookly.features.home.generated.resources.home_filter_sheet_subtitle
import bookly.features.home.generated.resources.home_filter_sheet_title
import bookly.features.home.generated.resources.home_filters_label
import bookly.features.home.generated.resources.home_greeting_eyebrow
import bookly.features.home.generated.resources.home_playroom_suffix
import bookly.features.home.generated.resources.home_search_placeholder
import bookly.features.home.generated.resources.home_settings_aria
import bookly.features.home.generated.resources.home_title
import coil3.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import org.evolutionsoftware.bookly.components.ui.BooklySheet
import org.evolutionsoftware.bookly.components.ui.BooklyToastKind
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.Feedback
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.IconButton
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.components.properties.FeedbackAction
import org.evolutionsoftware.bookly.design.components.properties.FeedbackProperties
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeRoute(
    refreshKey: Int,
    onBookSelected: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onShowToast: (String, BooklyToastKind) -> Unit = { _, _ -> },
) {
    val viewModel = rememberHomeViewModel()
    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(refreshKey) {
        viewModel.onUserIntent(HomeIntent.Load)
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is HomeSideEffect.FavoriteToggled ->
                    if (effect.added) {
                        onShowToast(getString(Res.string.home_favorite_added), BooklyToastKind.Success)
                    } else {
                        onShowToast(getString(Res.string.home_favorite_removed), BooklyToastKind.Info)
                    }
                HomeSideEffect.FavoriteUpdateFailed ->
                    onShowToast(getString(Res.string.home_favorite_failed), BooklyToastKind.Error)
            }
        }
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
    var showFilterSheet by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        HomeToolbar(
            profile = state.profile,
            onSettingsClick = onSettingsClick,
        )

        HomeSearchField(
            query = state.searchQuery,
            onQueryChange = { onIntent(HomeIntent.SearchChanged(it)) },
            modifier =
                Modifier.padding(
                    start = TokenProvider.spacings.horizontalSpacing,
                    end = TokenProvider.spacings.horizontalSpacing,
                    bottom = TokenProvider.spacings.sm,
                ),
        )

        HomeFilterRow(
            categories = state.categories,
            selectedCategory = state.selectedCategory,
            onCategorySelected = { onIntent(HomeIntent.FilterSelected(it)) },
            onOpenFilterSheet = { showFilterSheet = true },
        )

        Box(modifier = Modifier.weight(1f)) {
            when {
                state.isLoading -> {
                    Feedback(
                        properties = FeedbackProperties.Loading,
                        title = stringResource(Res.string.home_loading_title),
                        description = stringResource(Res.string.home_loading_body),
                    )
                }

                state.error != null -> {
                    Feedback(
                        properties =
                            FeedbackProperties.Error(
                                mainAction =
                                    FeedbackAction(
                                        text = stringResource(Res.string.home_error_retry),
                                        onClick = { onIntent(HomeIntent.Refresh) },
                                    ),
                            ),
                        title = stringResource(Res.string.home_error_title),
                        description = stringResource(Res.string.home_error_body),
                    )
                }

                state.visibleBooks.isEmpty() -> {
                    Feedback(
                        properties = FeedbackProperties.Empty(),
                        title = stringResource(Res.string.home_empty_filter_title),
                        description = stringResource(Res.string.home_empty_filter_body),
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        contentPadding =
                            PaddingValues(
                                start = TokenProvider.spacings.horizontalSpacing,
                                end = TokenProvider.spacings.horizontalSpacing,
                                top = TokenProvider.spacings.xxs,
                                bottom = TokenProvider.spacings.xl,
                            ),
                        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapMd),
                        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapMd),
                    ) {
                        items(state.visibleBooks) { book ->
                            PlayroomBookCard(
                                book = book,
                                isFavorite = book.id in state.favoriteBookIds,
                                onToggleFavorite = {
                                    onIntent(
                                        HomeIntent.FavoriteToggled(
                                            bookId = book.id,
                                            makeFavorite = book.id !in state.favoriteBookIds,
                                        ),
                                    )
                                },
                                onClick = { onBookSelected(book.id) },
                            )
                        }
                    }
                }
            }
        }
    }

    HomeFilterSheet(
        visible = showFilterSheet,
        categories = state.categories,
        selectedCategory = state.selectedCategory,
        onCategorySelected = {
            onIntent(HomeIntent.FilterSelected(it))
            showFilterSheet = false
        },
        onReset = { onIntent(HomeIntent.FilterSelected(BookCategory.All)) },
        onDismiss = { showFilterSheet = false },
    )
}

// === Toolbar ==============================================================

@Composable
private fun HomeToolbar(
    profile: ParentProfile?,
    onSettingsClick: () -> Unit,
) {
    val settingsButton: @Composable () -> Unit = {
        IconButton(
            properties =
                IconButtonProperties(
                    icon = Icons.Settings,
                    ariaLabel = stringResource(Res.string.home_settings_aria),
                ),
            onClick = onSettingsClick,
        )
    }

    if (profile != null) {
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
            Box(
                modifier =
                    Modifier
                        .size(44.dp)
                        .shadow(elevation = 3.dp, shape = CircleShape, spotColor = Color(0xFFFABD00))
                        .clip(CircleShape)
                        .background(TokenProvider.colors.borderAccent)
                        .border(2.dp, TokenProvider.colors.bgSurface, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = profile.initials,
                    color = TokenProvider.colors.text,
                    style = TokenProvider.textStyles.bodyStrong,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.home_greeting_eyebrow).uppercase(),
                    style =
                        TokenProvider.textStyles.eyebrow.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.6.sp,
                        ),
                    color = TokenProvider.colors.textMuted,
                )
                Text(
                    text = profile.displayName + stringResource(Res.string.home_playroom_suffix),
                    style = TokenProvider.textStyles.title,
                    color = TokenProvider.colors.borderAccent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            settingsButton()
        }
    } else {
        // Signed out the toolbar is just a centred title plus the settings action, so
        // it uses the shared component and inherits its metrics.
        Header(
            properties = HeaderProperties(title = stringResource(Res.string.home_title)),
            trailingContent = settingsButton,
        )
    }
}

// === Search ===============================================================

@Composable
private fun HomeSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = TokenProvider.colors.bgElevated,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.md),
                ),
        textStyle =
            TokenProvider.textStyles.input.copy(
                color = TokenProvider.colors.text,
            ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier =
                    Modifier.padding(
                        horizontal = TokenProvider.spacings.md,
                        vertical = TokenProvider.spacings.sm + TokenProvider.spacings.xxs / 2,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
            ) {
                Icon(
                    painter = painterResource(Icons.Search.icon),
                    contentDescription = null,
                    tint = TokenProvider.colors.textMuted,
                    modifier = Modifier.size(22.dp),
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.home_search_placeholder),
                            style = TokenProvider.textStyles.input,
                            color = TokenProvider.colors.textMuted.copy(alpha = 0.7f),
                        )
                    }
                    innerTextField()
                }
            }
        },
    )
}

// === Filters ==============================================================

/**
 * Per-category tile styling from the prototype: a soft tint that only fills in
 * once selected, plus the accent the glyph takes on when active.
 */
private data class CategoryStyle(
    val icon: Icons?,
    val glyph: String?,
    val tileColor: Color,
    val accent: Color,
)

private fun categoryStyle(category: BookCategory): CategoryStyle =
    when (category) {
        BookCategory.All -> CategoryStyle(Icons.CategoryAll, null, Color(0xFFFFE0B2), Color(0xFF874E00))
        BookCategory.Animals -> CategoryStyle(Icons.CategoryAnimals, null, Color(0xFFFFE0B2), Color(0xFF874E00))
        BookCategory.Plants -> CategoryStyle(Icons.CategoryPlants, null, Color(0xFFDCEDC8), Color(0xFF1B5E20))
        BookCategory.Weather -> CategoryStyle(Icons.CategoryWeather, null, Color(0xFFB3E5FC), Color(0xFF005E9F))
        BookCategory.Colors -> CategoryStyle(Icons.CategoryColors, null, Color(0xFFF8BBD0), Color(0xFFAD1457))
        BookCategory.Shapes -> CategoryStyle(Icons.CategoryShapes, null, Color(0xFFBBDEFB), Color(0xFF005E9F))
        BookCategory.Food -> CategoryStyle(Icons.CategoryFood, null, Color(0xFFFFCDD2), Color(0xFFB71C1C))
        // The prototype uses the Material "123" symbol, which renders as the digits themselves.
        BookCategory.Numbers -> CategoryStyle(null, "123", Color(0xFFFFE0B2), Color(0xFFE65100))
        BookCategory.Birds -> CategoryStyle(Icons.CategoryBirds, null, Color(0xFFB3E5FC), Color(0xFF005E9F))
    }

@Composable
private fun HomeFilterRow(
    categories: List<BookCategory>,
    selectedCategory: BookCategory,
    onCategorySelected: (BookCategory) -> Unit,
    onOpenFilterSheet: () -> Unit,
) {
    LazyRow(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = TokenProvider.spacings.sm),
        contentPadding = PaddingValues(horizontal = TokenProvider.spacings.horizontalSpacing),
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
    ) {
        item(key = "filters") {
            FilterTile(
                label = stringResource(Res.string.home_filters_label),
                isActive = false,
                tileColor = TokenProvider.colors.text,
                showBadge = selectedCategory != BookCategory.All,
                onClick = onOpenFilterSheet,
            ) {
                Icon(
                    painter = painterResource(Icons.Tune.icon),
                    contentDescription = null,
                    tint = TokenProvider.colors.textInverse,
                    modifier = Modifier.size(26.dp),
                )
            }
        }
        lazyItems(categories, key = { it.name }) { category ->
            val style = categoryStyle(category)
            val active = category == selectedCategory
            // Muted until selected, then the tile tints and the glyph takes its accent.
            val glyphTint = if (active) style.accent else TokenProvider.colors.textSubtle
            FilterTile(
                label = category.label,
                isActive = active,
                tileColor = if (active) style.tileColor else TokenProvider.colors.bgElevated,
                onClick = { onCategorySelected(category) },
            ) {
                if (style.icon != null) {
                    Icon(
                        painter = painterResource(style.icon.icon),
                        contentDescription = null,
                        tint = glyphTint,
                        modifier = Modifier.size(26.dp),
                    )
                } else if (style.glyph != null) {
                    Text(
                        text = style.glyph,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = glyphTint,
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterTile(
    label: String,
    isActive: Boolean,
    tileColor: Color,
    onClick: () -> Unit,
    showBadge: Boolean = false,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier =
            Modifier
                .width(64.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs - 2.dp),
    ) {
        // A 64dp frame holds the 56dp tile, leaving the 4dp gap the prototype's
        // ring-offset creates between the tile and its highlight ring.
        Box(
            modifier =
                Modifier
                    .size(64.dp)
                    .then(
                        if (isActive) {
                            Modifier.border(
                                width = 2.dp,
                                color = TokenProvider.colors.borderAccent,
                                shape = RoundedCornerShape(TokenProvider.borderRadius.md + 4.dp),
                            )
                        } else {
                            Modifier
                        },
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                        .background(tileColor),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
            if (showBadge) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(TokenProvider.colors.bgAccent)
                            .border(2.dp, TokenProvider.colors.bgBase, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "1",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = TokenProvider.colors.textInverse,
                    )
                }
            }
        }
        Text(
            text = label,
            style =
                TokenProvider.textStyles.eyebrow.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                ),
            color = if (isActive) TokenProvider.colors.text else TokenProvider.colors.textSubtle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun HomeFilterSheet(
    visible: Boolean,
    categories: List<BookCategory>,
    selectedCategory: BookCategory,
    onCategorySelected: (BookCategory) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit,
) {
    BooklySheet(visible = visible, onDismiss = onDismiss) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.home_filter_sheet_title),
                modifier = Modifier.weight(1f),
                style = TokenProvider.textStyles.title.copy(fontSize = 22.sp),
                color = TokenProvider.colors.text,
            )
            Text(
                text = stringResource(Res.string.home_filter_sheet_reset),
                modifier =
                    Modifier.clickable(onClick = onReset),
                style = TokenProvider.textStyles.bodyStrong.copy(fontSize = TokenProvider.fontSizes.caption),
                color = TokenProvider.colors.textBrand,
            )
        }
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xs))
        Text(
            text = stringResource(Res.string.home_filter_sheet_subtitle),
            style = TokenProvider.textStyles.body.copy(fontSize = TokenProvider.fontSizes.caption),
            color = TokenProvider.colors.textMuted,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
        categories.chunked(3).forEach { row ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = TokenProvider.spacings.sm),
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
            ) {
                row.forEach { category ->
                    FilterSheetItem(
                        category = category,
                        isActive = category == selectedCategory,
                        onClick = { onCategorySelected(category) },
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
        Button(
            properties =
                ButtonProperties(
                    label = stringResource(Res.string.home_filter_sheet_done),
                    size = ButtonProperties.Size.Large,
                ),
            onClick = onDismiss,
        )
    }
}

@Composable
private fun FilterSheetItem(
    category: BookCategory,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val style = categoryStyle(category)
    Box(modifier = modifier) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                    .background(
                        if (isActive) TokenProvider.colors.bgElevated else TokenProvider.colors.bgSurface,
                    )
                    .then(
                        if (isActive) {
                            Modifier.border(
                                width = 2.dp,
                                color = TokenProvider.colors.borderAccent,
                                shape = RoundedCornerShape(TokenProvider.borderRadius.md),
                            )
                        } else {
                            Modifier
                        },
                    )
                    .clickable(onClick = onClick)
                    .padding(vertical = TokenProvider.spacings.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                        .background(style.tileColor),
                contentAlignment = Alignment.Center,
            ) {
                // In the sheet every tile shows its full category colour, selected or not.
                if (style.icon != null) {
                    Icon(
                        painter = painterResource(style.icon.icon),
                        contentDescription = null,
                        tint = style.accent,
                        modifier = Modifier.size(24.dp),
                    )
                } else if (style.glyph != null) {
                    Text(
                        text = style.glyph,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = style.accent,
                    )
                }
            }
            Text(
                text = category.label,
                style =
                    TokenProvider.textStyles.eyebrow.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                color = TokenProvider.colors.text,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (isActive) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(TokenProvider.spacings.xs)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(TokenProvider.colors.bgAccent),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Icons.Check.icon),
                    contentDescription = null,
                    tint = TokenProvider.colors.textInverse,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

// === Book card ============================================================

private val organicShapes: List<Shape> =
    listOf(
        RoundedCornerShape(topStart = 56.dp, topEnd = 100.dp, bottomStart = 44.dp, bottomEnd = 100.dp),
        RoundedCornerShape(topStart = 84.dp, topEnd = 28.dp, bottomStart = 48.dp, bottomEnd = 90.dp),
        RoundedCornerShape(topStart = 36.dp, topEnd = 104.dp, bottomStart = 98.dp, bottomEnd = 42.dp),
        RoundedCornerShape(topStart = 92.dp, topEnd = 48.dp, bottomStart = 72.dp, bottomEnd = 104.dp),
    )

private fun organicShapeFor(bookId: String): Shape {
    val index = bookId.hashCode().let { if (it < 0) -it else it } % organicShapes.size
    return organicShapes[index]
}

@Composable
private fun PlayroomBookCard(
    book: BookSummary,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
) {
    val style = categoryStyle(book.category)

    Box {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(TokenProvider.borderRadius.md),
                        ambientColor = Color(0x14392E00),
                        spotColor = Color(0x14392E00),
                    )
                    .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                    .background(TokenProvider.colors.bgSurface)
                    .clickable(onClick = onClick)
                    .padding(TokenProvider.spacings.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(organicShapeFor(book.id))
                        .background(TokenProvider.colors.bgElevated),
            ) {
                if (!book.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = book.imageUrl,
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = book.emoji, fontSize = 56.sp)
                    }
                }
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                brush =
                                    Brush.verticalGradient(
                                        colors =
                                            listOf(
                                                Color.Transparent,
                                                style.accent.copy(alpha = 0.55f),
                                            ),
                                    ),
                            ),
                )
            }
            Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
            Text(
                text = book.title,
                modifier = Modifier.padding(bottom = TokenProvider.spacings.xxs),
                style =
                    TokenProvider.textStyles.bodyStrong.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                color = TokenProvider.colors.text,
                textAlign = TextAlign.Center,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        FavoriteButton(
            isFavorite = isFavorite,
            onClick = onToggleFavorite,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(TokenProvider.spacings.md),
        )
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ariaLabel =
        stringResource(
            if (isFavorite) Res.string.home_favorite_remove_aria else Res.string.home_favorite_add_aria,
        )
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            modifier
                .size(36.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x26392E00),
                    spotColor = Color(0x26392E00),
                )
                .clip(CircleShape)
                .background(TokenProvider.colors.bgSurface.copy(alpha = 0.92f))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(if (isFavorite) Icons.HeartFilled.icon else Icons.Heart.icon),
            contentDescription = ariaLabel,
            tint = if (isFavorite) TokenProvider.colors.favorite else TokenProvider.colors.textMuted,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
internal fun HomeScreenContent(
    state: HomeViewState,
    onIntent: (HomeIntent) -> Unit,
    onBookSelected: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    HomeScreen(
        state = state,
        onIntent = onIntent,
        onBookSelected = onBookSelected,
        onSettingsClick = onSettingsClick,
    )
}

@Composable
internal fun HomeLoadingContent() {
    Feedback(
        properties = FeedbackProperties.Loading,
        title = stringResource(Res.string.home_loading_title),
        description = stringResource(Res.string.home_loading_body),
    )
}

@Composable
internal fun HomeEmptyContent() {
    Feedback(
        properties = FeedbackProperties.Empty(),
        title = stringResource(Res.string.home_empty_library_title),
        description = stringResource(Res.string.home_empty_library_body),
    )
}

@Composable
internal fun HomeErrorContent(onRetry: () -> Unit) {
    Feedback(
        properties =
            FeedbackProperties.Error(
                mainAction =
                    FeedbackAction(
                        text = stringResource(Res.string.home_error_retry),
                        onClick = onRetry,
                    ),
            ),
        title = stringResource(Res.string.home_error_title),
        description = stringResource(Res.string.home_error_body),
    )
}
