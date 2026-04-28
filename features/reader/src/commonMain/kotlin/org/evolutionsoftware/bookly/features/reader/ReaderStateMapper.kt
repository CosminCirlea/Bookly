package org.evolutionsoftware.bookly.features.reader

import org.evolutionsoftware.bookly.core.mvi.StateMapper

internal class ReaderStateMapper : StateMapper<ReaderAction, ReaderViewState> {
    override fun invoke(
        action: ReaderAction,
        currentState: ReaderViewState,
    ): ReaderViewState =
        when (action) {
            ReaderAction.LoadingStarted -> currentState.copy(isLoading = true)
            is ReaderAction.ContentLoaded ->
                currentState.copy(
                    isLoading = false,
                    book = action.book,
                )
            ReaderAction.MissingBook ->
                currentState.copy(
                    isLoading = false,
                    book = null,
                )
        }
}
