package org.evolutionsoftware.bookly.services.favorites.domain.usecase

import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.core.usecase.utils.withExceptionHandling
import org.evolutionsoftware.bookly.services.favorites.domain.exception.FavoritesServiceException
import org.evolutionsoftware.bookly.services.favorites.domain.model.Favorite
import org.evolutionsoftware.bookly.services.favorites.domain.repository.FavoritesRepository

interface GetFavoritesUseCase {
    suspend operator fun invoke(
        profileId: String,
        languageId: Int = 1,
    ): Result<List<Favorite>, FavoritesError>
}

class GetFavoritesUseCaseImpl(
    private val repository: FavoritesRepository,
) : GetFavoritesUseCase {
    override suspend fun invoke(
        profileId: String,
        languageId: Int,
    ): Result<List<Favorite>, FavoritesError> =
        withExceptionHandling(
            errorMapper = ::toFavoritesError,
        ) {
            repository.getFavorites(profileId, languageId)
        }
}

internal fun toFavoritesError(exception: Exception): FavoritesError =
    when (exception) {
        is FavoritesServiceException.Unauthorized -> FavoritesError.Unauthorized
        is FavoritesServiceException.NotFound -> FavoritesError.NotFound
        is FavoritesServiceException.NetworkError -> FavoritesError.NetworkError
        is FavoritesServiceException.ServerError -> FavoritesError.ServerError
        else -> FavoritesError.Unknown(exception.message ?: "Unknown error")
    }

sealed interface FavoritesError {
    data object Unauthorized : FavoritesError
    data object NotFound : FavoritesError
    data object NetworkError : FavoritesError
    data object ServerError : FavoritesError
    data class Unknown(val message: String) : FavoritesError
}
