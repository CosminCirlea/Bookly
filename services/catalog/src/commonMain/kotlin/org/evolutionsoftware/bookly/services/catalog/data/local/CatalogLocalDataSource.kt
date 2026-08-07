package org.evolutionsoftware.bookly.services.catalog.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCard
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookCategory
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary

class CatalogLocalDataSource(
    driverFactory: DatabaseDriverFactory,
) {
    private val database = CatalogDatabase(driverFactory.createDriver())
    private val queries = database.bookEntityQueries
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getBooks(): List<BookSummary> = withContext(Dispatchers.IO) {
        val entities = queries.selectAllBooks().executeAsList()
        logger.d("LocalDataSource.getBooks: found ${entities.size} entities in DB")
        entities.map { it.toBookSummary() }
    }

    suspend fun saveBooks(books: List<BookSummary>): Unit = withContext(Dispatchers.IO) {
        logger.d("LocalDataSource.saveBooks: saving ${books.size} books")
        val now = Clock.System.now().toEpochMilliseconds()
        queries.transaction {
            queries.deleteAllBooks()
            books.forEach { book ->
                queries.insertBook(
                    id = book.id,
                    title = book.title,
                    description = book.description,
                    category = book.category.name,
                    emoji = book.emoji,
                    imageUrl = book.imageUrl,
                    updatedAt = now,
                )
            }
        }
        val savedCount = queries.selectAllBooks().executeAsList().size
        logger.d("LocalDataSource.saveBooks: verified $savedCount books in DB after save")
    }

    suspend fun getBookDetails(bookId: String): BookDetails? = withContext(Dispatchers.IO) {
        queries.selectBookDetailById(bookId).executeAsOneOrNull()?.toBookDetails()
    }

    suspend fun saveBookDetails(details: BookDetails): Unit = withContext(Dispatchers.IO) {
        val cardsJson = json.encodeToString(details.cards.map { CardJson(it) })
        queries.insertBookDetail(
            id = details.id,
            title = details.title,
            category = details.category.name,
            cardsJson = cardsJson,
            updatedAt = Clock.System.now().toEpochMilliseconds(),
        )
    }

    companion object {
        private val logger = Logger.withTag("CatalogLocalDataSource")
    }

    private fun BookEntity.toBookSummary(): BookSummary =
        BookSummary(
            id = id,
            title = title,
            description = description,
            category = BookCategory.entries.find { it.name == category } ?: BookCategory.All,
            emoji = emoji,
            imageUrl = imageUrl,
        )

    private fun BookDetailEntity.toBookDetails(): BookDetails {
        val cards = json.decodeFromString<List<CardJson>>(cardsJson).map { it.toBookCard() }
        return BookDetails(
            id = id,
            title = title,
            category = BookCategory.entries.find { it.name == category } ?: BookCategory.All,
            cards = cards,
        )
    }

    @kotlinx.serialization.Serializable
    private data class CardJson(
        val id: String,
        val title: String,
        val description: String,
        val emoji: String,
        val imageUrl: String?,
    ) {
        constructor(card: BookCard) : this(
            id = card.id,
            title = card.title,
            description = card.description,
            emoji = card.emoji,
            imageUrl = card.imageUrl,
        )

        fun toBookCard(): BookCard =
            BookCard(
                id = id,
                title = title,
                description = description,
                emoji = emoji,
                imageUrl = imageUrl,
            )
    }
}
