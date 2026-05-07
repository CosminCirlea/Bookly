package org.evolutionsoftware.bookly.services.favorites.domain.model

data class FavoriteBook(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
)

data class Favorite(
    val id: String,
    val bookId: String,
    val book: FavoriteBook,
)
