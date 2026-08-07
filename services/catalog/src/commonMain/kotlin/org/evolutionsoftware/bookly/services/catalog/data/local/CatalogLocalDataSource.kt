package org.evolutionsoftware.bookly.services.catalog.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.evolutionsoftware.bookly.core.logging.Logger

/**
 * SQLDelight-backed [CatalogCache].
 *
 * Deals only in stored rows — translating those into domain models is the mappers'
 * job, which keeps SQL out of the domain.
 */
class CatalogLocalDataSource(
    driverFactory: DatabaseDriverFactory,
) : CatalogCache {
    private val database = CatalogDatabase(driverFactory.createDriver())
    private val queries = database.bookEntityQueries

    override suspend fun getBooks(): List<BookRow> =
        withContext(Dispatchers.IO) {
            queries.selectAllBooks().executeAsList().map { it.toRow() }
        }

    override suspend fun hasBooks(): Boolean =
        withContext(Dispatchers.IO) {
            queries.countBooks().executeAsOne() > 0L
        }

    override suspend fun replaceBooks(books: List<BookRow>): Unit =
        withContext(Dispatchers.IO) {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.transaction {
                queries.deleteAllBooks()
                books.forEach { book ->
                    queries.insertBook(
                        id = book.id,
                        title = book.title,
                        description = book.description,
                        category = book.category,
                        emoji = book.emoji,
                        imageUrl = book.imageUrl,
                        revision = book.revision,
                        updatedAt = now,
                    )
                }
                // Keeps the cache from growing without bound as the catalog changes.
                queries.deleteOrphanedBookDetails()
            }
            logger.d("replaceBooks: cached ${books.size} books")
        }

    override suspend fun getBookRevision(bookId: String): String? =
        withContext(Dispatchers.IO) {
            queries.selectBookRevision(bookId).executeAsOneOrNull()?.revision
        }

    override suspend fun getBookDetails(bookId: String): BookDetailRow? =
        withContext(Dispatchers.IO) {
            queries.selectBookDetailById(bookId).executeAsOneOrNull()?.toRow()
        }

    override suspend fun saveBookDetails(details: BookDetailRow): Unit =
        withContext(Dispatchers.IO) {
            queries.insertBookDetail(
                id = details.id,
                title = details.title,
                category = details.category,
                cardsJson = details.cardsJson,
                revision = details.revision,
                updatedAt = Clock.System.now().toEpochMilliseconds(),
            )
            logger.d("saveBookDetails: cached ${details.id} at revision ${details.revision}")
        }

    private fun BookEntity.toRow(): BookRow =
        BookRow(
            id = id,
            title = title,
            description = description,
            category = category,
            emoji = emoji,
            imageUrl = imageUrl,
            revision = revision,
        )

    private fun BookDetailEntity.toRow(): BookDetailRow =
        BookDetailRow(
            id = id,
            title = title,
            category = category,
            cardsJson = cardsJson,
            revision = revision,
        )

    private companion object {
        val logger = Logger.withTag("CatalogLocalDataSource")
    }
}
