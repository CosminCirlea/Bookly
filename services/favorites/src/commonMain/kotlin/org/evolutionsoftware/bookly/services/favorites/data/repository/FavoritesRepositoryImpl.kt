package org.evolutionsoftware.bookly.services.favorites.data.repository

import org.evolutionsoftware.bookly.services.favorites.data.api.FavoritesAPI
import org.evolutionsoftware.bookly.services.favorites.data.dto.AddFavoriteRequestDto
import org.evolutionsoftware.bookly.services.favorites.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.favorites.domain.model.Favorite
import org.evolutionsoftware.bookly.services.favorites.domain.repository.FavoritesRepository

class FavoritesRepositoryImpl(
    private val api: FavoritesAPI,
) : FavoritesRepository {

    override suspend fun getFavorites(profileId: String, languageId: Int): List<Favorite> =
        withExceptionWrapping {
            api.getFavorites(profileId, languageId).map { it.toDomain() }
        }

    override suspend fun addFavorite(bookId: String, profileId: String): Unit = withExceptionWrapping {
        api.addFavorite(AddFavoriteRequestDto(bookId = bookId.toInt(), profileId = profileId.toInt()))
    }

    override suspend fun removeFavorite(profileId: String, bookId: String): Unit = withExceptionWrapping {
        api.removeFavorite(profileId, bookId)
    }
}
