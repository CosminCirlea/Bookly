package org.evolutionsoftware.bookly.services.catalog.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BooksPaginatedResponseDto(
    val data: List<BookListItemDto>,
    val pagination: BookPaginationDto,
)

@Serializable
data class BookPaginationDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
)

@Serializable
data class BookListItemDto(
    val id: Int,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    val duration: Int? = null,
    val status: Boolean = true,
    @SerialName("bookCategories")
    val bookCategories: List<BookCategoryItemDto> = emptyList(),
    @SerialName("bookTranslations")
    val bookTranslations: List<BookTranslationDto> = emptyList(),
    /**
     * Server-reported content version. Whenever this changes, the cached pages for
     * this book are stale and must be re-downloaded; while it stays the same the
     * reader is served entirely from disk.
     */
    @SerialName("content_version")
    val contentVersion: Int? = null,
    /**
     * Fallback revision marker for backends that report a timestamp rather than a
     * version counter. Only consulted when [contentVersion] is absent.
     */
    @SerialName("updated_at")
    val updatedAt: String? = null,
) {
    /**
     * Opaque revision for this book's content, or null when the backend reports
     * neither marker — in which case cached pages are kept as-is.
     */
    val revision: String?
        get() = contentVersion?.toString() ?: updatedAt
}

@Serializable
data class BookTranslationDto(
    val id: Int,
    val title: String,
    val description: String? = null,
    @SerialName("language_id")
    val languageId: Int,
)

@Serializable
data class BookCategoryItemDto(
    @SerialName("category")
    val category: BookCategoryDetailDto? = null,
)

@Serializable
data class BookCategoryDetailDto(
    val id: Int,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    @SerialName("categoryTranslations")
    val translations: List<CategoryTranslationRefDto> = emptyList(),
)

@Serializable
data class CategoryTranslationRefDto(
    val id: Int,
    val name: String,
    @SerialName("language_id")
    val languageId: Int? = null,
)

@Serializable
data class BookDetailDto(
    val id: Int,
    val title: String,
    val description: String? = null,
    val book: BookPhotoDto? = null,
    @SerialName("bookPages")
    val bookPages: List<BookPageDto> = emptyList(),
)

@Serializable
data class BookPhotoDto(
    @SerialName("photo_url")
    val photoUrl: String? = null,
)

@Serializable
data class BookPageDto(
    val id: Int,
    @SerialName("page_number")
    val pageNumber: Int,
    @SerialName("text_content")
    val textContent: String,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    @SerialName("audio_url")
    val audioUrl: String? = null,
)
