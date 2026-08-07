package org.evolutionsoftware.bookly.services.catalog.data.mapper

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.evolutionsoftware.bookly.services.catalog.data.local.BookDetailRow
import org.evolutionsoftware.bookly.services.catalog.data.local.BookRow
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary

private val cacheJson = Json { ignoreUnknownKeys = true }

/** On-disk representation of a page. Kept private to the data layer. */
@Serializable
private data class BookCardPayload(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val imageUrl: String?,
)

// === Cache -> domain ======================================================

internal fun BookRow.toSummary(): BookSummary =
    BookSummary(
        id = id,
        title = title,
        description = description,
        category = category.toBookCategory(),
        emoji = emoji,
        imageUrl = imageUrl,
    )

internal fun BookDetailRow.toDetails(): BookDetails =
    BookDetails(
        id = id,
        title = title,
        category = category.toBookCategory(),
        cards =
            cacheJson
                .decodeFromString<List<BookCardPayload>>(cardsJson)
                .map { payload ->
                    BookCard(
                        id = payload.id,
                        title = payload.title,
                        description = payload.description,
                        emoji = payload.emoji,
                        imageUrl = payload.imageUrl,
                    )
                },
    )

// === Domain -> cache ======================================================

internal fun BookSummary.toRow(revision: String?): BookRow =
    BookRow(
        id = id,
        title = title,
        description = description,
        category = category.name,
        emoji = emoji,
        imageUrl = imageUrl,
        revision = revision,
    )

internal fun BookDetails.toRow(revision: String?): BookDetailRow =
    BookDetailRow(
        id = id,
        title = title,
        category = category.name,
        cardsJson =
            cacheJson.encodeToString(
                cards.map { card ->
                    BookCardPayload(
                        id = card.id,
                        title = card.title,
                        description = card.description,
                        emoji = card.emoji,
                        imageUrl = card.imageUrl,
                    )
                },
            ),
        revision = revision,
    )

private fun String.toBookCategory(): BookCategory =
    BookCategory.entries.firstOrNull { it.name == this } ?: BookCategory.All
