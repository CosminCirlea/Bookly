package org.evolutionsoftware.bookly.services.catalog.data.mapper

import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookListItemDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookPageDto
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary

internal fun BookListItemDto.toSummary(languageId: Int = DEFAULT_LANGUAGE_ID): BookSummary {
    val translation =
        bookTranslations
            .firstOrNull { it.languageId == languageId }
            ?: bookTranslations.firstOrNull()

    return BookSummary(
        id = id.toString(),
        title = translation?.title ?: "",
        description = translation?.description ?: "",
        category = BookCategory.All,
        emoji = "",
        imageUrl = photoUrl,
    )
}

internal fun BookDetailDto.toDetails(): BookDetails =
    BookDetails(
        id = id.toString(),
        title = title,
        category = BookCategory.All,
        cards = bookPages.sortedBy { it.pageNumber }.map { it.toCard() },
    )

private fun BookPageDto.toCard(): BookCard =
    BookCard(
        id = id.toString(),
        title = textContent,
        description = textContent,
        emoji = "",
        imageUrl = photoUrl,
    )

internal const val DEFAULT_LANGUAGE_ID = 1
