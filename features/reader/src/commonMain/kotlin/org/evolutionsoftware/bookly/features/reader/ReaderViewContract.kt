package org.evolutionsoftware.bookly.features.reader

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails

internal data class ReaderViewState(
    val isLoading: Boolean = true,
    val book: BookDetails? = null,
    val currentPage: Int = 0,
    val isAutoplayEnabled: Boolean = false,
) : ViewState

internal sealed interface ReaderSideEffect : SideEffect {
    data object MissingBook : ReaderSideEffect
}

internal sealed interface ReaderIntent : UserIntent {
    data class Load(val bookId: String) : ReaderIntent

    data object AutoplayToggled : ReaderIntent

    data class PageChanged(val page: Int) : ReaderIntent

    data class AutoplayAdvanceRequested(
        val currentPage: Int,
        val totalPages: Int,
    ) : ReaderIntent
}

internal sealed interface ReaderAction : UserIntentAction {
    data object LoadingStarted : ReaderAction

    data class ContentLoaded(val book: BookDetails) : ReaderAction

    data object MissingBook : ReaderAction

    data class AutoplayUpdated(val isEnabled: Boolean) : ReaderAction

    data class CurrentPageUpdated(val page: Int) : ReaderAction
}
