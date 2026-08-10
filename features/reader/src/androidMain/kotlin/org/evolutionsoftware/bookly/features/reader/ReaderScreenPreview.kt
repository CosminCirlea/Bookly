package org.evolutionsoftware.bookly.features.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.design.theme.bookly.BooklyTheme
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails

private val previewBook =
    BookDetails(
        id = "forest-animals",
        title = "Forest Animals",
        category = BookCategory.Animals,
        cards =
            listOf(
                BookCard(
                    id = "1",
                    title = "Fox",
                    description = "A clever fox",
                    emoji = "🦊",
                    imageUrl = null,
                ),
                BookCard(
                    id = "2",
                    title = "Bear",
                    description = "A big bear",
                    emoji = "🐻",
                    imageUrl = null,
                ),
                BookCard(
                    id = "3",
                    title = "Deer",
                    description = "A gentle deer",
                    emoji = "🦌",
                    imageUrl = null,
                ),
            ),
    )

private val previewStateWithContent =
    ReaderViewState(
        book = previewBook,
        currentPage = 0,
        isAutoplayEnabled = false,
    )

private val previewStateLoading =
    ReaderViewState(
        book = null,
        currentPage = 0,
        isAutoplayEnabled = false,
    )

private val previewStateAutoplay =
    ReaderViewState(
        book = previewBook,
        currentPage = 1,
        isAutoplayEnabled = true,
    )

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReaderScreenPreview() {
    BooklyTheme {
        ReaderScreenContent(
            state = previewStateWithContent,
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReaderSkeletonPreview() {
    BooklyTheme {
        ReaderScreenContent(
            state = previewStateLoading,
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReaderScreenAutoplayPreview() {
    BooklyTheme {
        ReaderScreenContent(
            state = previewStateAutoplay,
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReaderPagePreview() {
    BooklyTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(TokenProvider.colors.bgBase),
        ) {
            ReaderPageContent(
                card = previewBook.cards.first(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReaderProgressIndicatorPreview() {
    BooklyTheme {
        Box(
            modifier =
                Modifier
                    .background(TokenProvider.colors.bgBase),
        ) {
            ReaderProgressIndicatorContent(
                total = 5,
                current = 2,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReaderTopBarPreview() {
    BooklyTheme {
        Box(
            modifier =
                Modifier
                    .background(TokenProvider.colors.bgBase),
        ) {
            ReaderTopBarContent(
                title = "Forest Animals",
                closeAriaLabel = "Close",
                onBack = {},
                isFavorite = true,
                onFavoriteToggle = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReaderEmptyStatePreview() {
    BooklyTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(TokenProvider.colors.bgBase),
        ) {
            ReaderEmptyStateContent(
                title = "Forest Animals",
                unavailableMessage = "This book is not available offline",
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Reader - shimmer skeleton")
@Composable
private fun ReaderSkeletonOnlyPreview() {
    BooklyTheme {
        ReaderSkeletonContent()
    }
}
