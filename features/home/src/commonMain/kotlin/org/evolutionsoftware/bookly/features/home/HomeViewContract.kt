package org.evolutionsoftware.bookly.features.home

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile

internal data class HomeViewState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val profile: ParentProfile? = null,
    val allBooks: List<BookSummary> = emptyList(),
    val visibleBooks: List<BookSummary> = emptyList(),
    val selectedCategory: BookCategory = BookCategory.All,
    val searchQuery: String = "",
    val favoriteBookIds: Set<String> = emptySet(),
    /**
     * Filter options for the cached catalogue. Held in state rather than derived on
     * read, so they land with the books and are not recomputed on every recomposition.
     */
    val categories: List<BookCategory> = listOf(BookCategory.All),
) : ViewState

internal sealed interface HomeSideEffect : SideEffect {
    data class FavoriteToggled(val added: Boolean) : HomeSideEffect

    data object FavoriteUpdateFailed : HomeSideEffect
}

internal sealed interface HomeIntent : UserIntent {
    /** Normal entry. Revalidates the catalog once per app session, cache otherwise. */
    data object Load : HomeIntent

    /** Explicit user request — retry or pull-to-refresh — which always hits the network. */
    data object Refresh : HomeIntent

    data class FilterSelected(val category: BookCategory) : HomeIntent

    data class SearchChanged(val query: String) : HomeIntent

    data class FavoriteToggled(
        val bookId: String,
        val makeFavorite: Boolean,
    ) : HomeIntent
}

internal sealed interface HomeAction : UserIntentAction {
    data object LoadingStarted : HomeAction

    data class BooksLoaded(
        val books: List<BookSummary>,
    ) : HomeAction

    data class ProfileLoaded(
        val profile: ParentProfile?,
        val favoriteBookIds: Set<String>,
    ) : HomeAction

    data class LoadingFailed(val error: String) : HomeAction

    data class FilterChanged(val category: BookCategory) : HomeAction

    data class SearchChanged(val query: String) : HomeAction

    data class FavoriteUpdated(
        val bookId: String,
        val isFavorite: Boolean,
    ) : HomeAction

    data class FavoriteUpdateReverted(
        val bookId: String,
        val isFavorite: Boolean,
    ) : HomeAction
}
