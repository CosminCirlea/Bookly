package org.evolutionsoftware.bookly.services.favorites.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.favorites.data.dto.AddFavoriteRequestDto
import org.evolutionsoftware.bookly.services.favorites.data.dto.FavoriteDto
import org.evolutionsoftware.bookly.services.favorites.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.favorites.domain.exception.FavoritesServiceException
import org.evolutionsoftware.bookly.services.favorites.domain.model.Favorite
import org.evolutionsoftware.bookly.services.favorites.domain.repository.FavoritesRepository

class FavoritesRepositoryImpl(
    private val httpClient: HttpClient,
) : FavoritesRepository {
    override suspend fun getFavorites(profileId: String, languageId: Int): List<Favorite> {
        val response = httpClient.get("$FAVORITES_PATH/profiles/$profileId/languages/$languageId")

        if (!response.status.isSuccess()) {
            logger.d("Get favorites failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }

        return response.body<List<FavoriteDto>>().map { it.toDomain() }
    }

    override suspend fun addFavorite(bookId: String, profileId: String) {
        val response =
            httpClient.post(FAVORITES_PATH) {
                setBody(
                    AddFavoriteRequestDto(
                        bookId = bookId.toInt(),
                        profileId = profileId.toInt(),
                    ),
                )
            }

        if (!response.status.isSuccess()) {
            logger.d("Add favorite failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }
    }

    override suspend fun removeFavorite(profileId: String, bookId: String) {
        val response = httpClient.delete("$FAVORITES_PATH/profiles/$profileId/books/$bookId")

        if (!response.status.isSuccess()) {
            logger.d("Remove favorite failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }
    }

    private fun mapStatusToException(status: HttpStatusCode): FavoritesServiceException =
        when (status) {
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Forbidden,
            -> FavoritesServiceException.Unauthorized()
            HttpStatusCode.NotFound -> FavoritesServiceException.NotFound()
            else ->
                if (status.value in 500..599) {
                    FavoritesServiceException.ServerError()
                } else {
                    FavoritesServiceException.NetworkError("Request failed with status ${status.value}.")
                }
        }

    private companion object {
        private const val FAVORITES_PATH = "api/favorites"
        private val logger = Logger.withTag("FavoritesRepository")
    }
}
