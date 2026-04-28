package org.evolutionsoftware.bookly.services.catalog.data.mapper

import org.evolutionsoftware.bookly.services.catalog.data.dto.BookCardDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDto
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary

internal fun BookDto.toSummary(): BookSummary =
    BookSummary(
        id = id,
        title = title,
        description = description,
        category = category.toBookCategory(),
        emoji = emoji,
        imageUrl = imageUrl,
    )

internal fun BookDto.toDetails(): BookDetails =
    BookDetails(
        id = id,
        title = title,
        category = category.toBookCategory(),
        cards = cards.map(BookCardDto::toDomain),
    )

private fun BookCardDto.toDomain(): BookCard =
    BookCard(
        id = id,
        title = title,
        description = description,
        emoji = emoji,
        imageUrl = imageUrl,
    )

private fun String.toBookCategory(): BookCategory =
    BookCategory.entries.firstOrNull { it.name == this } ?: BookCategory.All
