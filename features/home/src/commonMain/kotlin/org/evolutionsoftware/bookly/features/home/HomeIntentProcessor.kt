package org.evolutionsoftware.bookly.features.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.services.catalog.domain.usecase.GetBooksUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.GetCurrentProfileUseCase

internal class HomeIntentProcessor(
    private val getBooksUseCase: GetBooksUseCase,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
) : IntentProcessor<HomeIntent, HomeAction> {
    override fun invoke(intent: HomeIntent): Flow<HomeAction> =
        when (intent) {
            HomeIntent.Load ->
                flow {
                    emit(HomeAction.LoadingStarted)
                    val books =
                        try {
                            getBooksUseCase()
                        } catch (e: Exception) {
                            emit(HomeAction.LoadingFailed(e.message ?: "Failed to load books"))
                            return@flow
                        }
                    val profile =
                        try {
                            getCurrentProfileUseCase()
                        } catch (e: Exception) {
                            null
                        }
                    emit(HomeAction.ContentLoaded(books = books, profile = profile))
                }
            is HomeIntent.FilterSelected -> flowOf(HomeAction.FilterChanged(intent.filter))
        }
}
