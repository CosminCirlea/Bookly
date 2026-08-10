package org.evolutionsoftware.bookly.features.home

import org.evolutionsoftware.bookly.core.mvi.StateMapper
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary

internal class HomeStateMapper : StateMapper<HomeAction, HomeViewState> {
    override fun invoke(
        action: HomeAction,
        currentState: HomeViewState,
    ): HomeViewState =
        when (action) {
            HomeAction.LoadingStarted ->
                // Cache-first: keep showing already-loaded books instead of a spinner;
                // only show the loading state when there is nothing to display yet.
                currentState.copy(isLoading = currentState.allBooks.isEmpty(), error = null)
            is HomeAction.BooksLoaded -> {
                val categories = action.books.toFilterOptions()
                currentState.copy(
                    isLoading = false,
                    error = null,
                    allBooks = action.books,
                    categories = categories,
                    // A category that no longer exists after a refresh would filter
                    // everything out, so fall back to showing all books.
                    selectedCategory =
                        currentState.selectedCategory.takeIf { it in categories } ?: BookCategory.All,
                    visibleBooks =
                        filterBooks(
                            books = action.books,
                            category =
                                currentState.selectedCategory.takeIf { it in categories }
                                    ?: BookCategory.All,
                            query = currentState.searchQuery,
                        ),
                )
            }
            is HomeAction.ProfileLoaded ->
                currentState.copy(
                    profile = action.profile,
                    favoriteBookIds = action.favoriteBookIds,
                )
            is HomeAction.LoadingFailed ->
                currentState.copy(
                    isLoading = false,
                    // Keep showing cached books when a refresh fails; only surface the
                    // error screen when there is no content at all.
                    error = if (currentState.allBooks.isEmpty()) action.error else null,
                )
            is HomeAction.FilterChanged ->
                currentState.copy(
                    selectedCategory = action.category,
                    visibleBooks =
                        filterBooks(
                            books = currentState.allBooks,
                            category = action.category,
                            query = currentState.searchQuery,
                        ),
                )
            is HomeAction.SearchChanged ->
                currentState.copy(
                    searchQuery = action.query,
                    visibleBooks =
                        filterBooks(
                            books = currentState.allBooks,
                            category = currentState.selectedCategory,
                            query = action.query,
                        ),
                )
            is HomeAction.FavoriteUpdated ->
                currentState.copy(
                    favoriteBookIds = currentState.favoriteBookIds.update(action.bookId, action.isFavorite),
                )
            is HomeAction.FavoriteUpdateReverted ->
                currentState.copy(
                    favoriteBookIds = currentState.favoriteBookIds.update(action.bookId, action.isFavorite),
                )
        }
}

private fun Set<String>.update(
    bookId: String,
    isFavorite: Boolean,
): Set<String> = if (isFavorite) this + bookId else this - bookId

private fun filterBooks(
    books: List<BookSummary>,
    category: BookCategory,
    query: String,
): List<BookSummary> =
    books.filter { book ->
        val matchesCategory = category == BookCategory.All || book.category == category
        val matchesQuery = query.isBlank() || book.title.contains(query.trim(), ignoreCase = true)
        matchesCategory && matchesQuery
    }

/**
 * Filter options present in the catalogue: All, followed by the categories the cached
 * books actually belong to. Offering a category with nothing behind it would be a
 * dead end.
 */
private fun List<BookSummary>.toFilterOptions(): List<BookCategory> =
    listOf(BookCategory.All) +
        map { it.category }.distinct().filterNot { it == BookCategory.All }
