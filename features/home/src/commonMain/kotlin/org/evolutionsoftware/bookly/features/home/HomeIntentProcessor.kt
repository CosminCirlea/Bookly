package org.evolutionsoftware.bookly.features.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.services.catalog.domain.model.CatalogRefresh
import org.evolutionsoftware.bookly.services.catalog.domain.usecase.GetBooksUseCase
import org.evolutionsoftware.bookly.services.categories.domain.usecase.GetCategoriesUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.AddFavoriteUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.GetFavoritesUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.RemoveFavoriteUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.GetCurrentProfileUseCase

internal class HomeIntentProcessor(
    private val getBooksUseCase: GetBooksUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : IntentProcessor<HomeIntent, HomeAction> {
    override fun invoke(intent: HomeIntent): Flow<HomeAction> =
        when (intent) {
            HomeIntent.Load -> loadCatalog(CatalogRefresh.Automatic)

            HomeIntent.Refresh -> loadCatalog(CatalogRefresh.Force)

            is HomeIntent.FilterSelected -> flowOf(HomeAction.FilterChanged(intent.categoryId))

            is HomeIntent.SearchChanged -> flowOf(HomeAction.SearchChanged(intent.query))

            is HomeIntent.FavoriteToggled ->
                flow {
                    val profile =
                        try {
                            getCurrentProfileUseCase()
                        } catch (e: Exception) {
                            null
                        }
                    if (profile == null) {
                        emit(HomeAction.FavoriteUpdateReverted(intent.bookId, !intent.makeFavorite))
                        return@flow
                    }
                    emit(HomeAction.FavoriteUpdated(intent.bookId, intent.makeFavorite))
                    val result =
                        if (intent.makeFavorite) {
                            addFavoriteUseCase(intent.bookId, profile.id)
                        } else {
                            removeFavoriteUseCase(profile.id, intent.bookId)
                        }
                    if (result is Result.Error) {
                        emit(HomeAction.FavoriteUpdateReverted(intent.bookId, !intent.makeFavorite))
                    }
                }
        }

    private fun loadCatalog(refresh: CatalogRefresh): Flow<HomeAction> =
        flow {
            // Serve whatever is on disk before touching the network. After the first
            // successful load this means the books and their filter options are on
            // screen immediately, with no loading state at all.
            val cached =
                try {
                    getBooksUseCase(CatalogRefresh.CacheOnly)
                } catch (e: Exception) {
                    emptyList()
                }

            if (cached.isNotEmpty()) {
                emit(HomeAction.BooksLoaded(cached))
            } else {
                emit(HomeAction.LoadingStarted)
            }

            when (val categories = getCategoriesUseCase()) {
                is Result.Success -> emit(HomeAction.CategoriesLoaded(categories.data))
                is Result.Error -> Unit
            }

            // Then revalidate. The repository decides whether that costs a request.
            val books =
                try {
                    getBooksUseCase(refresh)
                } catch (e: Exception) {
                    if (cached.isEmpty()) {
                        emit(HomeAction.LoadingFailed(e.message ?: "Failed to load books"))
                        return@flow
                    }
                    // A failed refresh must not disturb books already on screen.
                    cached
                }

            // Only re-emit when the refresh actually changed something, so the grid
            // does not recompose for an identical list.
            if (books != cached) {
                emit(HomeAction.BooksLoaded(books))
            }
            val profile =
                try {
                    getCurrentProfileUseCase()
                } catch (e: Exception) {
                    null
                }
            val favoriteBookIds =
                if (profile != null) {
                    when (val favorites = getFavoritesUseCase(profile.id)) {
                        is Result.Success -> favorites.data.map { it.bookId }.toSet()
                        is Result.Error -> emptySet()
                    }
                } else {
                    emptySet()
                }
            emit(
                HomeAction.ProfileLoaded(
                    profile = profile,
                    favoriteBookIds = favoriteBookIds,
                ),
            )
        }
}
