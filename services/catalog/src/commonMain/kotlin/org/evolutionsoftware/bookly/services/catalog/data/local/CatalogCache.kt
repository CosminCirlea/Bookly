package org.evolutionsoftware.bookly.services.catalog.data.local

/**
 * The catalog's local store, expressed without reference to SQLDelight so the caching
 * policy can be exercised against an in-memory implementation.
 */
interface CatalogCache {
    suspend fun getBooks(): List<BookRow>

    suspend fun hasBooks(): Boolean

    /** Replaces the cached catalog and drops detail rows for books that no longer exist. */
    suspend fun replaceBooks(books: List<BookRow>)

    /** The content revision the catalog last reported for this book, if any. */
    suspend fun getBookRevision(bookId: String): String?

    suspend fun getBookDetails(bookId: String): BookDetailRow?

    suspend fun saveBookDetails(details: BookDetailRow)
}

/** A cached catalog entry. */
data class BookRow(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val emoji: String,
    val imageUrl: String?,
    val revision: String?,
)

/** A cached book's pages, serialised. */
data class BookDetailRow(
    val id: String,
    val title: String,
    val category: String,
    val cardsJson: String,
    val revision: String?,
)
