package org.evolutionsoftware.bookly.services.favorites.data.mapper

import org.evolutionsoftware.bookly.services.favorites.data.dto.FavoriteDto
import org.evolutionsoftware.bookly.services.favorites.domain.model.Favorite
import org.evolutionsoftware.bookly.services.favorites.domain.model.FavoriteBook

internal fun FavoriteDto.toDomain(): Favorite {
    val translation = book.bookTranslations.firstOrNull()
    return Favorite(
        id = id.toString(),
        bookId = book.id.toString(),
        book = FavoriteBook(
            id = book.id.toString(),
            title = translation?.title ?: "",
            description = translation?.description ?: "",
            imageUrl = book.photoUrl,
        ),
    )
}
