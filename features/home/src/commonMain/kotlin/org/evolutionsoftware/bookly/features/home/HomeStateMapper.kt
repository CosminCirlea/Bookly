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
                currentState.copy(isLoading = true, error = null)
            is HomeAction.ContentLoaded -> {
                val visible = filterBooks(action.books, currentState.selectedFilter)
                currentState.copy(
                    isLoading = false,
                    error = null,
                    profile = action.profile,
                    allBooks = action.books,
                    visibleBooks = visible,
                )
            }
            is HomeAction.LoadingFailed ->
                currentState.copy(
                    isLoading = false,
                    error = action.error,
                )
            is HomeAction.FilterChanged ->
                currentState.copy(
                    selectedFilter = action.filter,
                    visibleBooks = filterBooks(currentState.allBooks, action.filter),
                )
        }
}

private fun filterBooks(
    books: List<BookSummary>,
    filter: String,
): List<BookSummary> =
    if (filter == "All") {
        books
    } else {
        books.filter { it.category.label == filter }
    }
