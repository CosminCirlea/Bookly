package org.evolutionsoftware.bookly.services.favorites.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddFavoriteRequestDto(
    @SerialName("book_id")
    val bookId: Int,
    @SerialName("profile_id")
    val profileId: Int,
)

@Serializable
data class AddFavoriteResponseDto(
    val id: Int,
    @SerialName("profile_id")
    val profileId: Int,
    @SerialName("book_id")
    val bookId: Int,
    @SerialName("created_at")
    val createdAt: String,
)

@Serializable
data class FavoriteDto(
    val id: Int,
    val book: FavoriteBookDto,
)

@Serializable
data class FavoriteBookDto(
    val id: Int,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    @SerialName("bookTranslations")
    val bookTranslations: List<FavoriteBookTranslationDto> = emptyList(),
)

@Serializable
data class FavoriteBookTranslationDto(
    val title: String,
    val description: String? = null,
)
