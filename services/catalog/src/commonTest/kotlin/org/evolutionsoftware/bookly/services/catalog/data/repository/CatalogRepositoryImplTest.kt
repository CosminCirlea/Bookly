package org.evolutionsoftware.bookly.services.catalog.data.repository

import kotlinx.coroutines.test.runTest
import org.evolutionsoftware.bookly.services.catalog.data.api.CatalogRemoteDataSource
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookListItemDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookPageDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookPaginationDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookTranslationDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BooksPaginatedResponseDto
import org.evolutionsoftware.bookly.services.catalog.data.local.BookDetailRow
import org.evolutionsoftware.bookly.services.catalog.data.local.BookRow
import org.evolutionsoftware.bookly.services.catalog.data.local.CatalogCache
import org.evolutionsoftware.bookly.services.catalog.domain.exception.CatalogServiceException
import org.evolutionsoftware.bookly.services.catalog.domain.model.CatalogRefresh
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CatalogRepositoryImplTest {
    // === The list: revalidated once per app session ========================

    @Test
    fun `list is fetched once per session and served from cache afterwards`() =
        runTest {
            val remote = FakeRemote(books = listOf(bookDto(id = 1, revision = 3)))
            val repository = CatalogRepositoryImpl(remote, InMemoryCache())

            repository.getBooks()
            repository.getBooks()
            repository.getBooks()

            assertEquals(1, remote.bookRequests, "the catalog should be revalidated only once per session")
        }

    @Test
    fun `list is fetched when the cache is empty even after a session refresh`() =
        runTest {
            val remote = FakeRemote(books = emptyList())
            val repository = CatalogRepositoryImpl(remote, InMemoryCache())

            repository.getBooks()
            repository.getBooks()

            // Nothing was cached, so the second read has no choice but to try again.
            assertEquals(2, remote.bookRequests)
        }

    @Test
    fun `force always reaches the network`() =
        runTest {
            val remote = FakeRemote(books = listOf(bookDto(id = 1, revision = 1)))
            val repository = CatalogRepositoryImpl(remote, InMemoryCache())

            repository.getBooks()
            repository.getBooks(CatalogRefresh.Force)

            assertEquals(2, remote.bookRequests)
        }

    @Test
    fun `cache only never reaches the network`() =
        runTest {
            val cache = InMemoryCache(books = mutableListOf(bookRow(id = "1", revision = "1")))
            val remote = FakeRemote(books = listOf(bookDto(id = 1, revision = 9)))
            val repository = CatalogRepositoryImpl(remote, cache)

            val books = repository.getBooks(CatalogRefresh.CacheOnly)

            assertEquals(0, remote.bookRequests)
            assertEquals(1, books.size)
        }

    @Test
    fun `list falls back to cache when the network fails`() =
        runTest {
            val cache = InMemoryCache(books = mutableListOf(bookRow(id = "1", revision = "1")))
            val remote = FakeRemote(failBooks = true)
            val repository = CatalogRepositoryImpl(remote, cache)

            val books = repository.getBooks()

            assertEquals(1, books.size, "a failed refresh must still serve the cached catalog")
        }

    @Test
    fun `list surfaces the failure when there is nothing cached`() =
        runTest {
            val repository = CatalogRepositoryImpl(FakeRemote(failBooks = true), InMemoryCache())

            // Transport failures surface as the domain's error type, not the raw cause.
            assertFailsWith<CatalogServiceException> { repository.getBooks() }
        }

    // === Details: downloaded only when the revision changes ================

    @Test
    fun `details are downloaded the first time and then served from cache`() =
        runTest {
            val cache = InMemoryCache(books = mutableListOf(bookRow(id = "1", revision = "7")))
            val remote = FakeRemote(details = listOf(detailDto(id = 1)))
            val repository = CatalogRepositoryImpl(remote, cache)

            val first = repository.getBookDetails("1")
            val second = repository.getBookDetails("1")
            val third = repository.getBookDetails("1")

            assertNotNull(first)
            assertEquals(first, second)
            assertEquals(first, third)
            assertEquals(1, remote.detailRequests, "unchanged pages must never be downloaded twice")
        }

    @Test
    fun `details are re-downloaded when the catalog reports a new revision`() =
        runTest {
            val cache = InMemoryCache(books = mutableListOf(bookRow(id = "1", revision = "7")))
            val remote = FakeRemote(details = listOf(detailDto(id = 1)))
            val repository = CatalogRepositoryImpl(remote, cache)

            repository.getBookDetails("1")
            assertEquals(1, remote.detailRequests)

            // The server publishes new content for this book.
            cache.books[0] = cache.books[0].copy(revision = "8")
            repository.getBookDetails("1")

            assertEquals(2, remote.detailRequests, "a new revision must invalidate the cached pages")
        }

    @Test
    fun `details are kept when the catalog advertises no revision`() =
        runTest {
            val cache = InMemoryCache(books = mutableListOf(bookRow(id = "1", revision = null)))
            val remote = FakeRemote(details = listOf(detailDto(id = 1)))
            val repository = CatalogRepositoryImpl(remote, cache)

            repository.getBookDetails("1")
            repository.getBookDetails("1")

            assertEquals(
                1,
                remote.detailRequests,
                "without a revision there is no evidence of staleness, so no traffic should be spent",
            )
        }

    @Test
    fun `details fall back to cache when the download fails`() =
        runTest {
            val cache =
                InMemoryCache(
                    books = mutableListOf(bookRow(id = "1", revision = "8")),
                    details = mutableMapOf("1" to detailRow(id = "1", revision = "7")),
                )
            val repository = CatalogRepositoryImpl(FakeRemote(failDetails = true), cache)

            // Revision differs, so a download is attempted and fails.
            val details = repository.getBookDetails("1")

            assertNotNull(details, "a failed download must not take the book away from the child")
            assertEquals("Cached", details.title)
        }

    @Test
    fun `details keep the cached copy when the book disappears upstream`() =
        runTest {
            val cache =
                InMemoryCache(
                    books = mutableListOf(bookRow(id = "1", revision = "8")),
                    details = mutableMapOf("1" to detailRow(id = "1", revision = "7")),
                )
            // Remote returns null, i.e. 404.
            val repository = CatalogRepositoryImpl(FakeRemote(details = null), cache)

            val details = repository.getBookDetails("1")

            assertNotNull(details)
            assertEquals("Cached", details.title)
        }

    @Test
    fun `details return null when neither cache nor network has the book`() =
        runTest {
            val repository = CatalogRepositoryImpl(FakeRemote(details = null), InMemoryCache())

            assertNull(repository.getBookDetails("missing"))
        }

    @Test
    fun `replacing the catalog drops pages for books that no longer exist`() =
        runTest {
            val cache =
                InMemoryCache(
                    books = mutableListOf(bookRow(id = "1", revision = "1")),
                    details = mutableMapOf("1" to detailRow(id = "1", revision = "1")),
                )
            val remote = FakeRemote(books = listOf(bookDto(id = 2, revision = 1)))
            val repository = CatalogRepositoryImpl(remote, cache)

            repository.getBooks(CatalogRefresh.Force)

            assertTrue(cache.details.isEmpty(), "orphaned pages should not linger on disk")
        }
}

// === Fakes ================================================================

private class FakeRemote(
    private val books: List<BookListItemDto> = emptyList(),
    private val details: List<BookDetailDto>? = emptyList(),
    private val failBooks: Boolean = false,
    private val failDetails: Boolean = false,
) : CatalogRemoteDataSource {
    var bookRequests = 0
        private set
    var detailRequests = 0
        private set

    override suspend fun getBooks(limit: Int): BooksPaginatedResponseDto {
        bookRequests++
        if (failBooks) error("network down")
        return BooksPaginatedResponseDto(
            data = books,
            pagination = BookPaginationDto(total = books.size, page = 1, limit = limit, totalPages = 1),
        )
    }

    override suspend fun getBookDetails(
        bookId: String,
        languageId: Int,
    ): List<BookDetailDto>? {
        detailRequests++
        if (failDetails) error("network down")
        return details
    }
}

private class InMemoryCache(
    val books: MutableList<BookRow> = mutableListOf(),
    val details: MutableMap<String, BookDetailRow> = mutableMapOf(),
) : CatalogCache {
    override suspend fun getBooks(): List<BookRow> = books.toList()

    override suspend fun hasBooks(): Boolean = books.isNotEmpty()

    override suspend fun replaceBooks(books: List<BookRow>) {
        this.books.clear()
        this.books.addAll(books)
        val ids = books.map { it.id }.toSet()
        details.keys.retainAll(ids)
    }

    override suspend fun getBookRevision(bookId: String): String? =
        books.firstOrNull { it.id == bookId }?.revision

    override suspend fun getBookDetails(bookId: String): BookDetailRow? = details[bookId]

    override suspend fun saveBookDetails(details: BookDetailRow) {
        this.details[details.id] = details
    }
}

// === Builders =============================================================

private fun bookDto(
    id: Int,
    revision: Int?,
) = BookListItemDto(
    id = id,
    bookTranslations = listOf(BookTranslationDto(id = id, title = "Book $id", description = "d", languageId = 1)),
    contentVersion = revision,
)

private fun detailDto(id: Int) =
    BookDetailDto(
        id = id,
        title = "Downloaded",
        bookPages = listOf(BookPageDto(id = 1, pageNumber = 1, textContent = "Fox")),
    )

private fun bookRow(
    id: String,
    revision: String?,
) = BookRow(
    id = id,
    title = "Book $id",
    description = "d",
    category = "All",
    emoji = "",
    imageUrl = null,
    revision = revision,
)

private fun detailRow(
    id: String,
    revision: String?,
) = BookDetailRow(
    id = id,
    title = "Cached",
    category = "All",
    cardsJson = "[]",
    revision = revision,
)
