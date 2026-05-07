package org.evolutionsoftware.bookly.services.favorites.domain.usecase

import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.core.usecase.utils.withExceptionHandling
import org.evolutionsoftware.bookly.services.favorites.domain.repository.FavoritesRepository

interface RemoveFavoriteUseCase {
    suspend operator fun invoke(profileId: String, bookId: String): Result<Unit, FavoritesError>
}

class RemoveFavoriteUseCaseImpl(
    private val repository: FavoritesRepository,
) : RemoveFavoriteUseCase {
    override suspend fun invoke(profileId: String, bookId: String): Result<Unit, FavoritesError> =
        withExceptionHandling(errorMapper = ::toFavoritesError) {
            repository.removeFavorite(profileId, bookId)
        }
}
