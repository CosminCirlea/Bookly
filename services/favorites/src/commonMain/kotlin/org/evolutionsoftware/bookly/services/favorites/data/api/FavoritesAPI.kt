package org.evolutionsoftware.bookly.services.favorites.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import org.evolutionsoftware.bookly.services.favorites.data.dto.AddFavoriteRequestDto
import org.evolutionsoftware.bookly.services.favorites.data.dto.FavoriteDto
import org.evolutionsoftware.bookly.services.favorites.data.error.requireSuccess

class FavoritesAPI(
    private val httpClient: HttpClient,
) {
    suspend fun getFavorites(profileId: String, languageId: Int): List<FavoriteDto> =
        httpClient
            .get("$FAVORITES_PATH/profiles/$profileId/languages/$languageId")
            .requireSuccess()
            .body()

    suspend fun addFavorite(request: AddFavoriteRequestDto) {
        val response = httpClient.post(FAVORITES_PATH) { setBody(request) }
        if (response.status == HttpStatusCode.Conflict) return
        response.requireSuccess()
    }

    suspend fun removeFavorite(profileId: String, bookId: String) {
        val response = httpClient.delete("$FAVORITES_PATH/profiles/$profileId/books/$bookId")
        if (response.status == HttpStatusCode.NotFound) return
        response.requireSuccess()
    }

    private companion object {
        private const val FAVORITES_PATH = "api/favorites"
    }
}
