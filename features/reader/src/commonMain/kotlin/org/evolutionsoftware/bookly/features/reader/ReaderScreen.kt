package org.evolutionsoftware.bookly.features.reader

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.reader.generated.resources.Res
import bookly.features.reader.generated.resources.reader_autoplay_badge
import bookly.features.reader.generated.resources.reader_autoplay_start_aria
import bookly.features.reader.generated.resources.reader_autoplay_stop_aria
import bookly.features.reader.generated.resources.reader_close_book_aria
import bookly.features.reader.generated.resources.reader_favorite_add_aria
import bookly.features.reader.generated.resources.reader_favorite_added
import bookly.features.reader.generated.resources.reader_favorite_failed
import bookly.features.reader.generated.resources.reader_favorite_remove_aria
import bookly.features.reader.generated.resources.reader_favorite_removed
import bookly.features.reader.generated.resources.reader_missing_cached_message
import bookly.features.reader.generated.resources.reader_no_page_images_body
import bookly.features.reader.generated.resources.reader_no_page_images_title
import bookly.features.reader.generated.resources.reader_opening_book
import bookly.features.reader.generated.resources.reader_unavailable_offline
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.evolutionsoftware.bookly.components.ui.BooklyToastKind
import org.evolutionsoftware.bookly.components.ui.versionedImageUrl
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.Feedback
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.IconButton
import org.evolutionsoftware.bookly.design.components.properties.FeedbackProperties
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReaderRoute(
    bookId: String,
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
    onShowToast: (String, BooklyToastKind) -> Unit = { message, _ -> onShowMessage(message) },
) {
    val viewModel = rememberReaderViewModel()
    val state by viewModel.viewState.collectAsState()
    val missingCachedMessage = stringResource(Res.string.reader_missing_cached_message)

    LaunchedEffect(bookId) {
        viewModel.onUserIntent(ReaderIntent.Load(bookId))
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                ReaderSideEffect.MissingBook -> onShowMessage(missingCachedMessage)
                is ReaderSideEffect.FavoriteToggled ->
                    if (effect.added) {
                        onShowToast(getString(Res.string.reader_favorite_added), BooklyToastKind.Success)
                    } else {
                        onShowToast(getString(Res.string.reader_favorite_removed), BooklyToastKind.Info)
                    }
                ReaderSideEffect.FavoriteUpdateFailed ->
                    onShowToast(getString(Res.string.reader_favorite_failed), BooklyToastKind.Error)
            }
        }
    }

    ReaderScreen(
        state = state,
        onIntent = viewModel::onUserIntent,
        onBack = onBack,
    )
}

@Composable
private fun ReaderScreen(
    state: ReaderViewState,
    onIntent: (ReaderIntent) -> Unit,
    onBack: () -> Unit,
) {
    val cards = state.book?.cards.orEmpty()
    val unavailableMessage = stringResource(Res.string.reader_unavailable_offline)
    val noPageImagesTitle = stringResource(Res.string.reader_no_page_images_title)
    val noPageImagesBody = stringResource(Res.string.reader_no_page_images_body)
    val openingBook = stringResource(Res.string.reader_opening_book)
    val closeBookAria = stringResource(Res.string.reader_close_book_aria)
    val autoplayStartAria = stringResource(Res.string.reader_autoplay_start_aria)
    val autoplayStopAria = stringResource(Res.string.reader_autoplay_stop_aria)

    // The toolbar is a sibling of the content rather than an overlay, so it keeps the
    // shared metrics and the content no longer needs a hand-tuned top inset to clear it.
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        ReaderTopBar(
            title = state.book?.title.orEmpty(),
            closeAriaLabel = closeBookAria,
            onBack = onBack,
            showFavorite = state.canFavorite,
            isFavorite = state.isFavorite,
            onFavoriteToggle = {
                state.book?.let { book ->
                    onIntent(
                        ReaderIntent.FavoriteToggled(
                            bookId = book.id,
                            makeFavorite = !state.isFavorite,
                        ),
                    )
                }
            },
        )

        if (state.isLoading) {
            // Shimmering the real page layout, rather than a spinner, so the book
            // appears in place instead of replacing a centred graphic.
            ReaderSkeleton(modifier = Modifier.weight(1f))
        } else if (state.book != null && cards.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                ReaderUnavailableState(
                    title = noPageImagesTitle,
                    message = noPageImagesBody,
                    isError = true,
                )
            }
        } else if (cards.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                ReaderUnavailableState(
                    title = state.book?.title ?: openingBook,
                    message = unavailableMessage,
                )
            }
        } else {
            ReaderBookContent(
                state = state,
                cards = cards,
                autoplayStartAria = autoplayStartAria,
                autoplayStopAria = autoplayStopAria,
                onIntent = onIntent,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ReaderBookContent(
    state: ReaderViewState,
    cards: List<BookCard>,
    autoplayStartAria: String,
    autoplayStopAria: String,
    onIntent: (ReaderIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState =
        rememberPagerState(
            initialPage = 0,
            pageCount = { cards.size },
        )

    LaunchedEffect(state.isAutoplayEnabled, state.currentPage, cards.size) {
        if (!state.isAutoplayEnabled) {
            return@LaunchedEffect
        }

        delay(7_000)
        onIntent(
            ReaderIntent.AutoplayAdvanceRequested(
                currentPage = state.currentPage,
                totalPages = cards.size,
            ),
        )
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .collectLatest { page -> onIntent(ReaderIntent.PageChanged(page)) }
    }

    LaunchedEffect(state.currentPage, cards.size) {
        val targetPage = state.currentPage.coerceIn(0, cards.lastIndex)
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    // Content and footer are siblings in a column so a long card title can
    // never overlap the page indicator.
    Column(modifier = modifier) {
        BoxWithConstraints(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
        ) {
            val contentWidth = maxWidth.coerceAtMost(420.dp)
            // Size the illustration against the height left over after the
            // title, so a tall screen scales up and a short one never clips.
            val cardWidth =
                minOf(
                    contentWidth,
                    (maxHeight - READER_TITLE_BLOCK_HEIGHT).coerceAtLeast(120.dp) * 4f / 5f,
                )

            Column(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp)
                        .widthIn(max = contentWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp),
                ) { page ->
                    ReaderPage(
                        card = cards[page],
                        cardWidth = cardWidth,
                        showAutoplayBadge = state.isAutoplayEnabled,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        ReaderFooter(
            total = cards.size,
            current = state.currentPage.coerceIn(0, cards.lastIndex),
            isAutoplayEnabled = state.isAutoplayEnabled,
            autoplayAriaLabel = if (state.isAutoplayEnabled) autoplayStopAria else autoplayStartAria,
            onDotSelected = { page ->
                if (state.isAutoplayEnabled) {
                    onIntent(ReaderIntent.AutoplayToggled(isEnabled = false))
                }
                onIntent(ReaderIntent.PageChanged(page))
            },
            onAutoplayToggle = {
                onIntent(ReaderIntent.AutoplayToggled(isEnabled = !state.isAutoplayEnabled))
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = TokenProvider.spacings.horizontalSpacing,
                        end = TokenProvider.spacings.horizontalSpacing,
                        bottom = TokenProvider.spacings.xl,
                    ),
        )
    }
}

@Composable
private fun ReaderPage(
    card: BookCard,
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    showAutoplayBadge: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        ReaderIllustrationCard(
            card = card,
            showAutoplayBadge = showAutoplayBadge,
            modifier =
                Modifier
                    .width(cardWidth)
                    .aspectRatio(4f / 5f),
        )

        Spacer(modifier = Modifier.size(TokenProvider.spacings.sm))

        Text(
            text = card.title,
            modifier = Modifier.fillMaxWidth(),
            style =
                TokenProvider.textStyles.headline.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = READER_TITLE_FONT_SIZE,
                    lineHeight = READER_TITLE_LINE_HEIGHT,
                    letterSpacing = (-2).sp,
                ),
            color = TokenProvider.colors.text,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ReaderIllustrationCard(
    card: BookCard,
    modifier: Modifier = Modifier,
    showAutoplayBadge: Boolean = false,
) {
    val shape = RoundedCornerShape(TokenProvider.borderRadius.xl)
    val imageUrl = card.imageUrl

    Box(
        modifier =
            modifier
                .shadow(
                    elevation = 24.dp,
                    shape = shape,
                    ambientColor = Color(0x14392E00),
                    spotColor = Color(0x14392E00),
                ).clip(shape)
                .background(TokenProvider.colors.bgSurface),
        contentAlignment = Alignment.Center,
    ) {
        when {
            !imageUrl.isNullOrBlank() -> {
                AsyncImage(
                    model = versionedImageUrl(imageUrl, card.imageLastUpdated),
                    contentDescription = card.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            card.emoji.isNotBlank() -> {
                Box(
                    modifier =
                        Modifier
                            .size(220.dp)
                            .clip(CircleShape)
                            .background(TokenProvider.colors.bgAccentSoft.copy(alpha = 0.45f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = card.emoji,
                        style =
                            TokenProvider.textStyles.headline.copy(
                                fontSize = 120.sp,
                                lineHeight = 120.sp,
                            ),
                    )
                }
            }
            else -> {
                Box(
                    modifier =
                        Modifier
                            .size(220.dp)
                            .clip(CircleShape)
                            .background(TokenProvider.colors.bgAccentSoft.copy(alpha = 0.45f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "📖",
                        style =
                            TokenProvider.textStyles.headline.copy(
                                fontSize = 120.sp,
                                lineHeight = 120.sp,
                            ),
                    )
                }
            }
        }

        if (showAutoplayBadge) {
            Row(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(TokenProvider.spacings.sm)
                        .clip(CircleShape)
                        .background(TokenProvider.colors.bgAccent)
                        .padding(
                            horizontal = TokenProvider.spacings.xs + 2.dp,
                            vertical = TokenProvider.spacings.xxs,
                        ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xxs),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(TokenProvider.colors.textInverse),
                )
                Text(
                    text = stringResource(Res.string.reader_autoplay_badge),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TokenProvider.colors.textInverse,
                )
            }
        }
    }
}

/** Matches the design system's circular icon button, used to balance the footer. */
private val READER_ACTION_BUTTON_SIZE = 44.dp

private val READER_TITLE_FONT_SIZE = 64.sp
private val READER_TITLE_LINE_HEIGHT = 72.sp

/** Vertical space the card name occupies, reserved when sizing the illustration. */
private val READER_TITLE_BLOCK_HEIGHT = 84.dp

@Composable
private fun ReaderFooter(
    total: Int,
    current: Int,
    isAutoplayEnabled: Boolean,
    autoplayAriaLabel: String,
    onDotSelected: (Int) -> Unit,
    onAutoplayToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Balances the trailing action button so the dots stay optically centred.
        Spacer(modifier = Modifier.size(READER_ACTION_BUTTON_SIZE))
        ReaderProgressIndicator(
            total = total,
            current = current,
            onDotSelected = onDotSelected,
            modifier = Modifier.weight(1f),
        )
        ReaderActionButton(
            icon = if (isAutoplayEnabled) Icons.Pause else Icons.Play,
            ariaLabel = autoplayAriaLabel,
            onClick = onAutoplayToggle,
        )
    }
}

@Composable
private fun ReaderProgressIndicator(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
    onDotSelected: ((Int) -> Unit)? = null,
) {
    if (total <= 0) return

    // A window of at most MAX_VISIBLE_DOTS slides to keep the current page centred, so
    // a twenty-page book reads the same as a five-page one. The outermost dot on a side
    // that still has pages beyond it tapers down, hinting there is more in that
    // direction without spelling out how much.
    val windowStart = (current - MAX_VISIBLE_DOTS / 2).coerceIn(0, maxOf(0, total - MAX_VISIBLE_DOTS))
    val windowEnd = minOf(total, windowStart + MAX_VISIBLE_DOTS)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (index in windowStart until windowEnd) {
            val isActive = index == current
            val isTapered =
                (index == windowStart && windowStart > 0) ||
                    (index == windowEnd - 1 && windowEnd < total)

            // Animated so paging glides the window rather than snapping it.
            val diameter by animateDpAsState(
                targetValue = if (isTapered) DOT_SIZE_EDGE else DOT_SIZE,
                label = "readerDotDiameter",
            )
            val width by animateDpAsState(
                targetValue = if (isActive) DOT_WIDTH_ACTIVE else diameter,
                label = "readerDotWidth",
            )
            val interactionSource = remember { MutableInteractionSource() }

            Box(
                modifier =
                    Modifier
                        .height(diameter)
                        .width(width)
                        .clip(CircleShape)
                        .background(
                            if (isActive) {
                                TokenProvider.colors.bgAccentStrong
                            } else {
                                TokenProvider.colors.borderAccent.copy(alpha = 0.5f)
                            },
                        )
                        .then(
                            if (onDotSelected != null) {
                                Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                ) { onDotSelected(index) }
                            } else {
                                Modifier
                            },
                        ),
            )
        }
    }
}

/** Instagram-style indicator: a fixed-width window regardless of page count. */
private const val MAX_VISIBLE_DOTS = 5
private val DOT_SIZE = 10.dp

/** Outermost dot when more pages lie beyond it. */
private val DOT_SIZE_EDGE = 6.dp
private val DOT_WIDTH_ACTIVE = 24.dp

@Composable
private fun ReaderTopBar(
    title: String,
    closeAriaLabel: String,
    onBack: () -> Unit,
    showFavorite: Boolean,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Shares the app toolbar so the leading button lines up with every other screen;
    // only the glyph differs, since closing a book is not the same as going back.
    Header(
        modifier = modifier,
        properties =
            HeaderProperties(
                title = title,
                leadingIcon = Icons.Close,
                leadingAriaLabel = closeAriaLabel,
            ),
        onLeadingClick = onBack,
        trailingContent =
            if (showFavorite) {
                {
                    ReaderFavoriteButton(
                        isFavorite = isFavorite,
                        onClick = onFavoriteToggle,
                    )
                }
            } else {
                null
            },
    )
}

@Composable
private fun ReaderFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ariaLabel =
        stringResource(
            if (isFavorite) Res.string.reader_favorite_remove_aria else Res.string.reader_favorite_add_aria,
        )

    IconButton(
        modifier = modifier,
        properties =
            IconButtonProperties(
                icon = if (isFavorite) Icons.HeartFilled else Icons.Heart,
                ariaLabel = ariaLabel,
            ),
        onClick = onClick,
        content = {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            ambientColor = Color(0x0D392E00),
                            spotColor = Color(0x0D392E00),
                        )
                        .clip(CircleShape)
                        .background(TokenProvider.colors.bgAccentSoft),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(if (isFavorite) Icons.HeartFilled.icon else Icons.Heart.icon),
                    contentDescription = ariaLabel,
                    tint = if (isFavorite) TokenProvider.colors.favorite else TokenProvider.colors.text,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
    )
}

@Composable
private fun ReaderActionButton(
    icon: Icons,
    ariaLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        properties =
            IconButtonProperties(
                icon = icon,
                ariaLabel = ariaLabel,
            ),
        onClick = onClick,
        content = {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            ambientColor = Color(0x0D392E00),
                            spotColor = Color(0x0D392E00),
                        )
                        .clip(CircleShape)
                        .background(TokenProvider.colors.bgAccentSoft),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon.icon),
                    contentDescription = ariaLabel,
                    tint = TokenProvider.colors.textAccent,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
    )
}

@Composable
private fun ReaderUnavailableState(
    title: String,
    message: String,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Feedback(
        properties = if (isError) FeedbackProperties.Error() else FeedbackProperties.Empty(),
        title = title,
        description = message,
        modifier = modifier.widthIn(max = 420.dp),
    )
}

@Composable
internal fun ReaderScreenContent(
    state: ReaderViewState,
    onIntent: (ReaderIntent) -> Unit,
    onBack: () -> Unit,
) {
    ReaderScreen(
        state = state,
        onIntent = onIntent,
        onBack = onBack,
    )
}

@Composable
internal fun ReaderPageContent(
    card: BookCard,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 320.dp,
) {
    ReaderPage(
        card = card,
        cardWidth = cardWidth,
        modifier = modifier,
    )
}

@Composable
internal fun ReaderProgressIndicatorContent(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    ReaderProgressIndicator(
        total = total,
        current = current,
        modifier = modifier,
    )
}

@Composable
internal fun ReaderTopBarContent(
    title: String,
    closeAriaLabel: String,
    onBack: () -> Unit,
    showFavorite: Boolean,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ReaderTopBar(
        title = title,
        closeAriaLabel = closeAriaLabel,
        onBack = onBack,
        showFavorite = showFavorite,
        isFavorite = isFavorite,
        onFavoriteToggle = onFavoriteToggle,
        modifier = modifier,
    )
}

@Composable
internal fun ReaderEmptyStateContent(
    title: String,
    unavailableMessage: String,
    modifier: Modifier = Modifier,
) {
    ReaderUnavailableState(
        title = title,
        message = unavailableMessage,
        modifier = modifier,
    )
}

/** Preview seam for the loading skeleton. */
@Composable
internal fun ReaderSkeletonContent(modifier: Modifier = Modifier) {
    ReaderSkeleton(modifier = modifier)
}
