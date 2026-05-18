package org.evolutionsoftware.bookly.features.reader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.reader.generated.resources.Res
import bookly.features.reader.generated.resources.reader_autoplay_start_aria
import bookly.features.reader.generated.resources.reader_autoplay_stop_aria
import bookly.features.reader.generated.resources.reader_close_book_aria
import bookly.features.reader.generated.resources.reader_empty_symbol
import bookly.features.reader.generated.resources.reader_missing_cached_message
import bookly.features.reader.generated.resources.reader_opening_book
import bookly.features.reader.generated.resources.reader_unavailable_offline
import bookly.features.reader.generated.resources.reader_fox
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.IconButton
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReaderRoute(
    bookId: String,
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit,
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
    val openingBook = stringResource(Res.string.reader_opening_book)
    val closeBookAria = stringResource(Res.string.reader_close_book_aria)
    val autoplayStartAria = stringResource(Res.string.reader_autoplay_start_aria)
    val autoplayStopAria = stringResource(Res.string.reader_autoplay_stop_aria)
    val pagerState =
        rememberPagerState(
            initialPage = state.currentPage,
            pageCount = { maxOf(cards.size, 1) },
        )

    LaunchedEffect(state.isAutoplayEnabled, state.currentPage, cards.size) {
        if (!state.isAutoplayEnabled || cards.isEmpty()) {
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

    LaunchedEffect(pagerState, cards.size) {
        if (cards.isEmpty()) {
            return@LaunchedEffect
        }

        snapshotFlow { pagerState.currentPage }
            .collectLatest { page -> onIntent(ReaderIntent.PageChanged(page)) }
    }

    LaunchedEffect(state.currentPage, cards.size) {
        if (cards.isEmpty() || pagerState.currentPage == state.currentPage) {
            return@LaunchedEffect
        }

        pagerState.animateScrollToPage(state.currentPage.coerceIn(0, cards.lastIndex))
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        ReaderTopBar(
            isAutoplayEnabled = state.isAutoplayEnabled,
            autoplayAriaLabel = if (state.isAutoplayEnabled) autoplayStopAria else autoplayStartAria,
            onAutoplayToggle = { onIntent(ReaderIntent.AutoplayToggled) },
            closeAriaLabel = closeBookAria,
            onBack = onBack,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(
                        start = TokenProvider.spacings.horizontalSpacing,
                        top = TokenProvider.spacings.horizontalSpacing,
                        end = TokenProvider.spacings.horizontalSpacing,
                    ),
        )

        if (cards.isEmpty()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp),
                contentAlignment = Alignment.Center,
            ) {
                ReaderEmptyState(
                    title = state.book?.title ?: openingBook,
                    unavailableMessage = unavailableMessage,
                )
            }
        } else {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val contentWidth = maxWidth.coerceAtMost(420.dp)

                Column(
                    modifier =
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 112.dp)
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
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                ReaderProgressIndicator(
                    total = cards.size,
                    current = state.currentPage.coerceIn(0, cards.lastIndex),
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 48.dp),
                )
            }
        }
    }
}

@Composable
private fun ReaderPage(
    card: BookCard,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        ReaderIllustrationCard(
            card = card,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f),
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = card.title,
            modifier = Modifier.fillMaxWidth(),
            style =
                TokenProvider.textStyles.headline.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 80.sp,
                    lineHeight = 80.sp,
                    letterSpacing = (-2).sp,
                ),
            color = TokenProvider.colors.text,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun ReaderIllustrationCard(
    card: BookCard,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(TokenProvider.borderRadius.xl)

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
        if (card.id == "fox") {
            Image(
                painter = painterResource(Res.drawable.reader_fox),
                contentDescription = card.title,
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(1.25f)
                        .offset(x = (-6).dp),
                contentScale = ContentScale.Crop,
            )
        } else {
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
    }
}

@Composable
private fun ReaderProgressIndicator(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(total) { index ->
            val isActive = index == current
            Box(
                modifier =
                    Modifier
                        .size(if (isActive) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive) {
                                TokenProvider.colors.bgAccentStrong
                            } else {
                                TokenProvider.colors.border.copy(alpha = 0.6f)
                            },
                        ),
            )
        }
    }
}

@Composable
private fun ReaderCloseButton(
    ariaLabel: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ReaderActionButton(
        icon = Icons.Close,
        ariaLabel = ariaLabel,
        onClick = onBack,
        modifier = modifier,
    )
}

@Composable
private fun ReaderTopBar(
    isAutoplayEnabled: Boolean,
    autoplayAriaLabel: String,
    onAutoplayToggle: () -> Unit,
    closeAriaLabel: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReaderActionButton(
            icon = if (isAutoplayEnabled) Icons.Pause else Icons.Play,
            ariaLabel = autoplayAriaLabel,
            onClick = onAutoplayToggle,
        )
        ReaderCloseButton(
            ariaLabel = closeAriaLabel,
            onBack = onBack,
        )
    }
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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ReaderEmptyState(
    title: String,
    unavailableMessage: String,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.DotLottie(Res.readBytes("files/loading_lottie.lottie"))
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE,
    )

    Column(
        modifier =
            modifier
                .widthIn(max = 420.dp)
                .padding(horizontal = TokenProvider.spacings.horizontalSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapLg),
    ) {
        Box(
            modifier =
                Modifier
                    .size(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                ),
                contentDescription = "Loading",
                modifier = Modifier.fillMaxSize(),
            )
        }

        Text(
            text = title,
            style =
                TokenProvider.textStyles.headline.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 48.sp,
                    lineHeight = 48.sp,
                    letterSpacing = (-2).sp,
                ),
            color = TokenProvider.colors.text,
            textAlign = TextAlign.Center,
        )

        Text(
            text = unavailableMessage,
            style = TokenProvider.textStyles.body,
            color = TokenProvider.colors.textMuted,
            textAlign = TextAlign.Center,
        )
    }
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
) {
    ReaderPage(
        card = card,
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
    isAutoplayEnabled: Boolean,
    autoplayAriaLabel: String,
    onAutoplayToggle: () -> Unit,
    closeAriaLabel: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ReaderTopBar(
        isAutoplayEnabled = isAutoplayEnabled,
        autoplayAriaLabel = autoplayAriaLabel,
        onAutoplayToggle = onAutoplayToggle,
        closeAriaLabel = closeAriaLabel,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
internal fun ReaderEmptyStateContent(
    title: String,
    unavailableMessage: String,
    modifier: Modifier = Modifier,
) {
    ReaderEmptyState(
        title = title,
        unavailableMessage = unavailableMessage,
        modifier = modifier,
    )
}
