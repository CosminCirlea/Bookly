package org.evolutionsoftware.bookly.services.catalog.data.mapper

import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookListItemDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookPageDto
import org.evolutionsoftware.bookly.services.catalog.data.local.BookRow
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary

internal fun BookListItemDto.toSummary(languageId: Int = DEFAULT_LANGUAGE_ID): BookSummary {
    val translation =
        bookTranslations
            .firstOrNull { it.languageId == languageId }
            ?: bookTranslations.firstOrNull()
    val categories = bookCategories.mapNotNull { it.category }
    val categoryName =
        categories
            .firstOrNull()
            ?.translations
            ?.let { translations ->
                translations.firstOrNull { (it.languageId ?: it.language?.id) == languageId }
                    ?: translations.firstOrNull()
            }?.name

    return BookSummary(
        id = id.toString(),
        title = translation?.title ?: "",
        description = translation?.description ?: "",
        category = BookCategory.fromName(categoryName),
        emoji = "",
        imageUrl = photoUrl,
        categoryIds = categories.map { it.id.toString() }.toSet(),
        lastUpdated = cacheLastUpdated,
    )
}

/**
 * Maps a catalog entry straight to the row that will be cached, carrying the
 * server's last-updated timestamp so [org.evolutionsoftware.bookly.services.catalog.data.repository.CatalogRepositoryImpl]
 * can later tell whether the book's pages need re-downloading.
 */
internal fun BookListItemDto.toRow(languageId: Int = DEFAULT_LANGUAGE_ID): BookRow {
    val summary = toSummary(languageId)
    return BookRow(
        id = summary.id,
        title = summary.title,
        description = summary.description,
        category = summary.category.name,
        categoryIds = summary.categoryIds,
        emoji = summary.emoji,
        imageUrl = summary.imageUrl,
        lastUpdated = summary.lastUpdated,
    )
}

internal fun BookDetailDto.toDetails(
    bookId: String,
    lastUpdated: String?,
): BookDetails =
    BookDetails(
        id = bookId,
        title = title,
        category = BookCategory.All,
        cards =
            bookPages
                .sortedBy { it.pageNumber }
                .mapNotNull { page ->
                    page.photoUrl
                        ?.takeIf { it.isNotBlank() }
                        ?.let { imageUrl -> page.toCard(imageUrl, lastUpdated) }
                },
        lastUpdated = lastUpdated,
    )

private fun BookPageDto.toCard(
    imageUrl: String,
    lastUpdated: String?,
): BookCard =
    BookCard(
        id = id.toString(),
        title = textContent,
        description = textContent,
        emoji = "",
        imageUrl = imageUrl,
        imageLastUpdated = lastUpdated,
    )

internal const val DEFAULT_LANGUAGE_ID = 1
