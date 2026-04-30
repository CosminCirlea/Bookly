package org.evolutionsoftware.bookly.features.reader

import org.evolutionsoftware.bookly.core.mvi.StateMapper

internal class ReaderStateMapper : StateMapper<ReaderAction, ReaderViewState> {
    override fun invoke(
        action: ReaderAction,
        currentState: ReaderViewState,
    ): ReaderViewState =
        when (action) {
            ReaderAction.LoadingStarted ->
                currentState.copy(
                    isLoading = true,
                    currentPage = 0,
                    isAutoplayEnabled = false,
                )
            is ReaderAction.ContentLoaded ->
                currentState.copy(
                    isLoading = false,
                    book = action.book,
                    currentPage = 0,
                    isAutoplayEnabled = false,
                )
            ReaderAction.MissingBook ->
                currentState.copy(
                    isLoading = false,
                    book = null,
                    currentPage = 0,
                    isAutoplayEnabled = false,
                )
            is ReaderAction.AutoplayUpdated ->
                currentState.copy(isAutoplayEnabled = !currentState.isAutoplayEnabled)
            is ReaderAction.CurrentPageUpdated ->
                currentState.copy(currentPage = action.page.coerceAtLeast(0))
        }
}
