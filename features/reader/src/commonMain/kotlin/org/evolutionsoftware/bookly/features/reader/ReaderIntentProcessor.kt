package org.evolutionsoftware.bookly.features.reader

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.services.catalog.domain.usecase.GetBookDetailsUseCase

internal class ReaderIntentProcessor(
    private val getBookDetailsUseCase: GetBookDetailsUseCase,
) : IntentProcessor<ReaderIntent, ReaderAction> {
    override fun invoke(intent: ReaderIntent): Flow<ReaderAction> =
        when (intent) {
            is ReaderIntent.Load ->
                flow {
                    emit(ReaderAction.LoadingStarted)
                    val book = getBookDetailsUseCase(intent.bookId)
                    emit(if (book != null) ReaderAction.ContentLoaded(book) else ReaderAction.MissingBook)
                }
        }
}
