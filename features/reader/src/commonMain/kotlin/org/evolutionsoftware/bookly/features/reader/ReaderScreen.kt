package org.evolutionsoftware.bookly.features.reader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import bookly.features.reader.generated.resources.reader_empty_symbol
import bookly.features.reader.generated.resources.reader_missing_cached_message
import bookly.features.reader.generated.resources.reader_unavailable_offline
import bookly.features.reader.generated.resources.reader_fox
import kotlinx.coroutines.flow.collectLatest
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
        onBack = onBack,
    )
}

@Composable
private fun ReaderScreen(
    state: ReaderViewState,
    onBack: () -> Unit,
) {
    val cards = state.book?.cards.orEmpty()
    val pagerState = rememberPagerState(pageCount = { maxOf(cards.size, 1) })
    val unavailableMessage = stringResource(Res.string.reader_unavailable_offline)

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        ReaderCloseButton(
            onBack = onBack,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = TokenProvider.spacings.lg, end = TokenProvider.spacings.lg),
        )

        if (cards.isEmpty()) {
            ReaderEmptyState(
                title = state.book?.title ?: "Opening book...",
                unavailableMessage = unavailableMessage,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(top = 112.dp, bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                BoxWithConstraints(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true),
                ) {
                    val contentWidth = maxWidth.coerceAtMost(420.dp)
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = (maxWidth - contentWidth) / 2),
                    ) { page ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            ReaderPage(
                                card = cards[page],
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }

                ReaderProgressIndicator(
                    total = cards.size,
                    current = pagerState.currentPage,
                    modifier = Modifier.padding(top = TokenProvider.spacings.lg),
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
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.lg),
    ) {
        ReaderIllustrationCard(
            card = card,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TokenProvider.spacings.xs)
                    .aspectRatio(4f / 5f),
        )

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
                        .fillMaxSize()
                        .padding(8.dp),
                contentScale = ContentScale.Fit,
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
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        properties =
            IconButtonProperties(
                icon = Icons.Close,
                ariaLabel = "Close book",
                buttonSize = 64.dp,
                iconSize = 18.dp,
            ),
        onClick = onBack,
    )
}

@Composable
private fun ReaderEmptyState(
    title: String,
    unavailableMessage: String,
    modifier: Modifier = Modifier,
) {
    val emptySymbol = stringResource(Res.string.reader_empty_symbol)

    Column(
        modifier =
            modifier
                .widthIn(max = 420.dp)
                .padding(horizontal = TokenProvider.spacings.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.lg),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
                    .clip(RoundedCornerShape(TokenProvider.borderRadius.xl))
                    .background(TokenProvider.colors.bgSurface),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = emptySymbol,
                style =
                    TokenProvider.textStyles.headline.copy(
                        fontSize = 120.sp,
                        lineHeight = 120.sp,
                    ),
            )
        }

        Text(
            text = title,
            style =
                TokenProvider.textStyles.headline.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 64.sp,
                    lineHeight = 64.sp,
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
