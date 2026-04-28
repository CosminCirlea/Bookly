package org.evolutionsoftware.bookly.features.home

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile

internal data class HomeViewState(
    val isLoading: Boolean = true,
    val profile: ParentProfile? = null,
    val allBooks: List<BookSummary> = emptyList(),
    val visibleBooks: List<BookSummary> = emptyList(),
    val selectedFilter: String = "All",
) : ViewState {
    val filters: List<String> =
        listOf("All") + allBooks.map { it.category.label }.distinct()
}

internal sealed interface HomeSideEffect : SideEffect

internal sealed interface HomeIntent : UserIntent {
    data object Load : HomeIntent

    data class FilterSelected(val filter: String) : HomeIntent
}

internal sealed interface HomeAction : UserIntentAction {
    data object LoadingStarted : HomeAction

    data class ContentLoaded(
        val books: List<BookSummary>,
        val profile: ParentProfile?,
    ) : HomeAction

    data class FilterChanged(val filter: String) : HomeAction
}
