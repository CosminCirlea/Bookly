package org.evolutionsoftware.bookly.services.favorites.domain.repository

import org.evolutionsoftware.bookly.services.favorites.domain.model.Favorite

interface FavoritesRepository {
    suspend fun getFavorites(profileId: String, languageId: Int = 1): List<Favorite>
    suspend fun addFavorite(bookId: String, profileId: String)
    suspend fun removeFavorite(profileId: String, bookId: String)
}
