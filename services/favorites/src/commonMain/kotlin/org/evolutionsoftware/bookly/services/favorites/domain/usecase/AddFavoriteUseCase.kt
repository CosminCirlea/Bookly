package org.evolutionsoftware.bookly.services.favorites.domain.usecase

import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.core.usecase.utils.withExceptionHandling
import org.evolutionsoftware.bookly.services.favorites.domain.repository.FavoritesRepository

interface AddFavoriteUseCase {
    suspend operator fun invoke(bookId: String, profileId: String): Result<Unit, FavoritesError>
}

class AddFavoriteUseCaseImpl(
    private val repository: FavoritesRepository,
) : AddFavoriteUseCase {
    override suspend fun invoke(bookId: String, profileId: String): Result<Unit, FavoritesError> =
        withExceptionHandling(errorMapper = ::toFavoritesError) {
            repository.addFavorite(bookId, profileId)
        }
}
