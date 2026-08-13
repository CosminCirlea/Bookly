package org.evolutionsoftware.bookly.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.design.theme.bookly.BooklyTheme
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.categories.domain.model.Category
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile

private val previewBooks =
    listOf(
        BookSummary(
            id = "forest-animals",
            title = "Forest Animals",
            description = "Learn about animals in the forest",
            category = BookCategory.Animals,
            emoji = "🦊",
            categoryIds = setOf("1"),
        ),
        BookSummary(
            id = "birds",
            title = "Birds",
            description = "Discover different birds",
            category = BookCategory.Animals,
            emoji = "🐦",
            categoryIds = setOf("1"),
        ),
        BookSummary(
            id = "garden-veggies",
            title = "Garden Veggies",
            description = "Explore vegetables in the garden",
            category = BookCategory.Plants,
            emoji = "🥕",
            categoryIds = setOf("2"),
        ),
        BookSummary(
            id = "bugs-insects",
            title = "Bugs & Insects",
            description = "Meet tiny creatures",
            category = BookCategory.Animals,
            emoji = "🐛",
            categoryIds = setOf("1"),
        ),
    )

private val previewProfile =
    ParentProfile(
        id = "1",
        displayName = "Junior Reader",
    )

private val previewStateWithContent =
    HomeViewState(
        isLoading = false,
        error = null,
        profile = previewProfile,
        allBooks = previewBooks,
        visibleBooks = previewBooks,
        selectedCategoryId = null,
        categories =
            listOf(
                Category(id = "1", name = "Animals"),
                Category(id = "2", name = "Plants"),
            ),
        favoriteBookIds = setOf("forest-animals"),
    )

private val previewStateLoading =
    HomeViewState(
        isLoading = true,
        error = null,
        profile = null,
        allBooks = emptyList(),
        visibleBooks = emptyList(),
        selectedCategoryId = null,
    )

private val previewStateEmpty =
    HomeViewState(
        isLoading = false,
        error = null,
        profile = previewProfile,
        allBooks = emptyList(),
        visibleBooks = emptyList(),
        selectedCategoryId = null,
    )

private val previewStateError =
    HomeViewState(
        isLoading = false,
        error = "Failed to load books. Please check your connection.",
        profile = null,
        allBooks = emptyList(),
        visibleBooks = emptyList(),
        selectedCategoryId = null,
    )

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    BooklyTheme {
        HomeScreenContent(
            state = previewStateWithContent,
            onIntent = {},
            onBookSelected = {},
            onSettingsClick = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenLoadingPreview() {
    BooklyTheme {
        HomeScreenContent(
            state = previewStateLoading,
            onIntent = {},
            onBookSelected = {},
            onSettingsClick = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenEmptyPreview() {
    BooklyTheme {
        HomeScreenContent(
            state = previewStateEmpty,
            onIntent = {},
            onBookSelected = {},
            onSettingsClick = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenErrorPreview() {
    BooklyTheme {
        HomeScreenContent(
            state = previewStateError,
            onIntent = {},
            onBookSelected = {},
            onSettingsClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeLoadingStatePreview() {
    BooklyTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(TokenProvider.colors.bgBase),
        ) {
            HomeLoadingContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeEmptyStatePreview() {
    BooklyTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(TokenProvider.colors.bgBase),
        ) {
            HomeEmptyContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeErrorStatePreview() {
    BooklyTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(TokenProvider.colors.bgBase),
        ) {
            HomeErrorContent(onRetry = {})
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Home - shimmer skeleton")
@Composable
private fun HomeSkeletonPreview() {
    BooklyTheme {
        HomeSkeleton()
    }
}
