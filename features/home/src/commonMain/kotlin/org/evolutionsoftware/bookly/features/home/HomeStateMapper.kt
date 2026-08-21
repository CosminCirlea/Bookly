package org.evolutionsoftware.bookly.features.home

import org.evolutionsoftware.bookly.core.mvi.StateMapper
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
                currentState.copy(
                    isLoading = false,
                    error = null,
                    allBooks = action.books,
                    visibleBooks =
                        filterBooks(
                            books = action.books,
                            categoryId = currentState.selectedCategoryId,
                            query = currentState.searchQuery,
                        ),
                )
            }
            is HomeAction.CategoriesLoaded -> {
                val categoryIds = action.categories.map { it.id }.toSet()
                val selectedCategoryId = currentState.selectedCategoryId?.takeIf { it in categoryIds }
                currentState.copy(
                    categories = action.categories,
                    selectedCategoryId = selectedCategoryId,
                    visibleBooks =
                        filterBooks(
                            books = currentState.allBooks,
                            categoryId = selectedCategoryId,
                            query = currentState.searchQuery,
                        ),
                )
            }
            is HomeAction.SessionLoaded ->
                currentState.copy(
                        accountDisplayName = action.displayName,
                        profile = currentState.profile.takeIf { action.displayName != null },
                        favoriteBookIds = currentState.favoriteBookIds.takeIf { action.displayName != null }.orEmpty(),
                )
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
                    selectedCategoryId = action.categoryId,
                    visibleBooks =
                        filterBooks(
                            books = currentState.allBooks,
                            categoryId = action.categoryId,
                            query = currentState.searchQuery,
                        ),
                )
            is HomeAction.SearchChanged ->
                currentState.copy(
                    searchQuery = action.query,
                    visibleBooks =
                        filterBooks(
                            books = currentState.allBooks,
                            categoryId = currentState.selectedCategoryId,
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
    categoryId: String?,
    query: String,
): List<BookSummary> =
    books.filter { book ->
        val matchesCategory = categoryId == null || categoryId in book.categoryIds
        val matchesQuery = query.isBlank() || book.title.contains(query.trim(), ignoreCase = true)
        matchesCategory && matchesQuery
    }
