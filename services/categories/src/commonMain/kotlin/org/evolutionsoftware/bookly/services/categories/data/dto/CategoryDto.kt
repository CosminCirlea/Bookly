package org.evolutionsoftware.bookly.services.categories.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val status: Boolean = true,
    @SerialName("categoryTranslations")
    val translations: List<CategoryTranslationDto> = emptyList(),
)

@Serializable
data class CategoryTranslationDto(
    val id: Int,
    val name: String,
    @SerialName("language_id")
    val languageId: Int,
)

@Serializable
data class CategoriesPaginatedResponseDto(
    val data: List<CategoryDto>,
    val pagination: CategoryPaginationDto,
)

@Serializable
data class CategoryPaginationDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
)
