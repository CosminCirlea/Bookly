package org.evolutionsoftware.bookly.features.reader

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.services.catalog.domain.usecase.GetBookDetailsUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.AddFavoriteUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.GetFavoritesUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.RemoveFavoriteUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.GetCurrentProfileUseCase

internal class ReaderIntentProcessor(
    private val getBookDetailsUseCase: GetBookDetailsUseCase,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : IntentProcessor<ReaderIntent, ReaderAction> {
    override fun invoke(intent: ReaderIntent): Flow<ReaderAction> =
        when (intent) {
            is ReaderIntent.Load ->
                flow {
                    emit(ReaderAction.LoadingStarted)
                    val book = getBookDetailsUseCase(intent.bookId)
                    emit(if (book != null) ReaderAction.ContentLoaded(book) else ReaderAction.MissingBook)
                    if (book != null) {
                        val profile =
                            try {
                                getCurrentProfileUseCase()
                            } catch (e: Exception) {
                                null
                            }
                        if (profile != null) {
                            when (val favorites = getFavoritesUseCase(profile.id)) {
                                is Result.Success ->
                                    emit(
                                        ReaderAction.FavoriteStatusLoaded(
                                            isFavorite = favorites.data.any { it.bookId == book.id },
                                        ),
                                    )
                                is Result.Error -> Unit
                            }
                        }
                    }
                }
            is ReaderIntent.AutoplayToggled -> flowOf(ReaderAction.AutoplayUpdated(intent.isEnabled))
            is ReaderIntent.PageChanged -> flowOf(ReaderAction.CurrentPageUpdated(intent.page))
            is ReaderIntent.AutoplayAdvanceRequested ->
                // The last card ends the run rather than looping back to the beginning.
                if (intent.currentPage >= intent.totalPages - 1) {
                    flowOf(ReaderAction.AutoplayUpdated(isEnabled = false))
                } else {
                    flowOf(ReaderAction.CurrentPageUpdated(intent.currentPage + 1))
                }
            is ReaderIntent.FavoriteToggled ->
                flow {
                    emit(ReaderAction.FavoriteUpdated(intent.makeFavorite))
                    val profile =
                        try {
                            getCurrentProfileUseCase()
                        } catch (e: Exception) {
                            null
                        } ?: return@flow
                    val result =
                        if (intent.makeFavorite) {
                            addFavoriteUseCase(intent.bookId, profile.id)
                        } else {
                            removeFavoriteUseCase(profile.id, intent.bookId)
                        }
                    if (result is Result.Error) {
                        emit(ReaderAction.FavoriteUpdateReverted(!intent.makeFavorite))
                    }
                }
        }
}
