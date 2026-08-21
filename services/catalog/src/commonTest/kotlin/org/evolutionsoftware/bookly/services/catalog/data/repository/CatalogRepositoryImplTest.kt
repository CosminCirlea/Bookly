package org.evolutionsoftware.bookly.services.catalog.data.repository

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.evolutionsoftware.bookly.services.catalog.data.api.CatalogRemoteDataSource
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookCategoryDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookCategoryItemDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookListItemDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookPageDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookPaginationDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookTranslationDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BooksPaginatedResponseDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.CategoryLanguageRefDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.CategoryTranslationRefDto
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

    @Test
    fun `list preserves backend category IDs and translated style through cache`() =
        runTest {
            val remote =
                FakeRemote(
                    books =
                        listOf(
                            bookDto(
                                id = 1,
                                revision = 1,
                                categoryId = 42,
                                categoryName = "Animals",
                            ),
                        ),
                )
            val repository = CatalogRepositoryImpl(remote, InMemoryCache())

            val book = repository.getBooks().single()

            assertEquals(setOf("42"), book.categoryIds)
            assertEquals("Animals", book.category.label)
        }

    @Test
    fun `list decodes and caches the backend last updated field`() =
        runTest {
            val response =
                Json.decodeFromString<BooksPaginatedResponseDto>(
                    """
                    {
                      "data": [
                        {
                          "id": 1,
                          "last_updated": "2026-08-14T10:15:30.000Z",
                          "bookCategories": [],
                          "bookTranslations": []
                        }
                      ],
                      "pagination": {"total": 1, "page": 1, "limit": 20, "totalPages": 1}
                    }
                    """.trimIndent(),
                )
            val repository = CatalogRepositoryImpl(FakeRemote(books = response.data), InMemoryCache())

            assertEquals("2026-08-14T10:15:30.000Z", repository.getBooks().single().lastUpdated)
        }

    @Test
    fun `freshness lookup searches every catalog page`() =
        runTest {
            val remote = FakeRemote(books = (1..101).map { bookDto(id = it, revision = it) })

            assertEquals("101", remote.getBookLastUpdated("101"))
            assertEquals(2, remote.bookRequests)
        }

    // === Details: downloaded only when last_updated changes =================

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
    fun `details are cached by book ID rather than translation ID`() =
        runTest {
            val cache = InMemoryCache(books = mutableListOf(bookRow(id = "10", revision = null)))
            val remote = FakeRemote(details = listOf(detailDto(id = 999)))
            val repository = CatalogRepositoryImpl(remote, cache)

            val first = repository.getBookDetails("10")
            val second = repository.getBookDetails("10")

            assertEquals("10", first?.id)
            assertEquals(first, second)
            assertEquals(setOf("10"), cache.details.keys)
            assertEquals(1, remote.detailRequests, "reopening the book must use the cached pages")
        }

    @Test
    fun `details are re-downloaded when the API reports a new last update`() =
        runTest {
            val cache =
                InMemoryCache(
                    books = mutableListOf(bookRow(id = "1", revision = "2026-08-13T10:00:00Z")),
                    details =
                        mutableMapOf(
                            "1" to detailRow(id = "1", revision = "2026-08-13T10:00:00Z"),
                        ),
                )
            val remote =
                FakeRemote(
                    books =
                        listOf(
                            bookDto(
                                id = 1,
                                revision = null,
                                lastUpdated = "2026-08-14T10:00:00Z",
                            ),
                        ),
                    details = listOf(detailDto(id = 1)),
                )
            val repository = CatalogRepositoryImpl(remote, cache)

            val details = repository.getBookDetails("1")

            assertEquals("2026-08-14T10:00:00Z", details?.lastUpdated)
            assertEquals(1, remote.detailRequests, "a new last update must invalidate the cached pages")
            assertEquals("2026-08-14T10:00:00Z", cache.details["1"]?.lastUpdated)
        }

    @Test
    fun `unchanged API last update serves cached details`() =
        runTest {
            val timestamp = "2026-08-14T10:00:00Z"
            val cache =
                InMemoryCache(
                    books = mutableListOf(bookRow(id = "1", revision = timestamp)),
                    details = mutableMapOf("1" to detailRow(id = "1", revision = timestamp)),
                )
            val remote =
                FakeRemote(
                    books = listOf(bookDto(id = 1, revision = null, lastUpdated = timestamp)),
                    details = listOf(detailDto(id = 1)),
                )
            val repository = CatalogRepositoryImpl(remote, cache)

            val details = repository.getBookDetails("1")

            assertEquals("Cached", details?.title)
            assertEquals(0, remote.detailRequests)
        }

    @Test
    fun `refresh drops pages whose API image is null instead of preserving cached images`() =
        runTest {
            val oldTimestamp = "2026-08-13T10:00:00Z"
            val newTimestamp = "2026-08-14T10:00:00Z"
            val cachedCards =
                """
                [
                  {
                    "id":"41",
                    "title":"Cat",
                    "description":"Cat",
                    "emoji":"",
                    "imageUrl":"https://cdn.example/cat.png"
                  }
                ]
                """.trimIndent()
            val cache =
                InMemoryCache(
                    books = mutableListOf(bookRow(id = "10", revision = oldTimestamp)),
                    details =
                        mutableMapOf(
                            "10" to
                                detailRow(
                                    id = "10",
                                    revision = oldTimestamp,
                                    cardsJson = cachedCards,
                                ),
                        ),
                )
            val remote =
                FakeRemote(
                    books =
                        listOf(
                            bookDto(
                                id = 10,
                                revision = null,
                                lastUpdated = newTimestamp,
                            ),
                        ),
                    details =
                        listOf(
                            detailDto(
                                id = 11,
                                pages =
                                    listOf(
                                        BookPageDto(
                                            id = 41,
                                            pageNumber = 1,
                                            textContent = "Cat",
                                            photoUrl = null,
                                        ),
                                        BookPageDto(
                                            id = 44,
                                            pageNumber = 4,
                                            textContent = "Duck",
                                            photoUrl = "https://cdn.example/duck.png",
                                        ),
                                    ),
                            ),
                        ),
                )
            val repository = CatalogRepositoryImpl(remote, cache)

            val refreshed = repository.getBookDetails("10")
            val persisted = repository.getBookDetails("10", CatalogRefresh.CacheOnly)

            assertEquals(listOf("44"), refreshed?.cards?.map { it.id })
            assertEquals("https://cdn.example/duck.png", refreshed?.cards?.single()?.imageUrl)
            assertEquals(newTimestamp, refreshed?.cards?.single()?.imageLastUpdated)
            assertEquals(refreshed, persisted)
        }

    @Test
    fun `details with only null page images cache an empty page list`() =
        runTest {
            val cache = InMemoryCache(books = mutableListOf(bookRow(id = "1", revision = "7")))
            val remote =
                FakeRemote(
                    details =
                        listOf(
                            detailDto(
                                id = 1,
                                pages =
                                    listOf(
                                        BookPageDto(
                                            id = 1,
                                            pageNumber = 1,
                                            textContent = "Fox",
                                            photoUrl = null,
                                        ),
                                    ),
                            ),
                        ),
                )
            val repository = CatalogRepositoryImpl(remote, cache)

            val details = repository.getBookDetails("1")
            val persisted = repository.getBookDetails("1", CatalogRefresh.CacheOnly)

            assertTrue(details?.cards.orEmpty().isEmpty())
            assertTrue(persisted?.cards.orEmpty().isEmpty())
            assertEquals("[]", cache.details["1"]?.cardsJson)
        }

    @Test
    fun `legacy cached pages without images are not returned`() =
        runTest {
            val cache =
                InMemoryCache(
                    books = mutableListOf(bookRow(id = "1", revision = "7")),
                    details =
                        mutableMapOf(
                            "1" to
                                detailRow(
                                    id = "1",
                                    revision = "7",
                                    cardsJson =
                                        """
                                        [
                                          {
                                            "id":"1",
                                            "title":"Fox",
                                            "description":"Fox",
                                            "emoji":"",
                                            "imageUrl":null
                                          },
                                          {
                                            "id":"2",
                                            "title":"Duck",
                                            "description":"Duck",
                                            "emoji":"",
                                            "imageUrl":"https://cdn.example/duck.png"
                                          }
                                        ]
                                        """.trimIndent(),
                                ),
                        ),
                )
            val repository = CatalogRepositoryImpl(FakeRemote(), cache)

            val details = repository.getBookDetails("1", CatalogRefresh.CacheOnly)

            assertEquals(listOf("2"), details?.cards?.map { it.id })
        }

    @Test
    fun `details are kept when the catalog advertises no last update`() =
        runTest {
            val cache = InMemoryCache(books = mutableListOf(bookRow(id = "1", revision = null)))
            val remote = FakeRemote(details = listOf(detailDto(id = 1)))
            val repository = CatalogRepositoryImpl(remote, cache)

            repository.getBookDetails("1")
            repository.getBookDetails("1")

            assertEquals(
                1,
                remote.detailRequests,
                "without a timestamp there is no evidence of staleness, so no traffic should be spent",
            )
        }

    @Test
    fun `details fall back to cache when the freshness check fails`() =
        runTest {
            val cache =
                InMemoryCache(
                    books = mutableListOf(bookRow(id = "1", revision = "8")),
                    details = mutableMapOf("1" to detailRow(id = "1", revision = "7")),
                )
            val remote = FakeRemote(failBooks = true, failDetails = true)
            val repository = CatalogRepositoryImpl(remote, cache)

            val details = repository.getBookDetails("1")

            assertNotNull(details, "a failed download must not take the book away from the child")
            assertEquals("Cached", details.title)
            assertEquals(0, remote.detailRequests, "a failed freshness check should keep cached pages")
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

    override suspend fun getBooks(
        limit: Int,
        page: Int,
    ): BooksPaginatedResponseDto {
        bookRequests++
        if (failBooks) error("network down")
        val totalPages = maxOf(1, (books.size + limit - 1) / limit)
        val pageBooks = books.drop((page - 1) * limit).take(limit)
        return BooksPaginatedResponseDto(
            data = pageBooks,
            pagination =
                BookPaginationDto(
                    total = books.size,
                    page = page,
                    limit = limit,
                    totalPages = totalPages,
                ),
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

    override suspend fun getBookLastUpdated(bookId: String): String? =
        books.firstOrNull { it.id == bookId }?.lastUpdated

    override suspend fun getBookDetails(bookId: String): BookDetailRow? = details[bookId]

    override suspend fun saveBookDetails(details: BookDetailRow) {
        this.details[details.id] = details
    }
}

// === Builders =============================================================

private fun bookDto(
    id: Int,
    revision: Int?,
    lastUpdated: String? = revision?.toString(),
    categoryId: Int? = null,
    categoryName: String? = null,
) = BookListItemDto(
    id = id,
    bookTranslations = listOf(BookTranslationDto(id = id, title = "Book $id", description = "d", languageId = 1)),
    bookCategories =
        if (categoryId != null && categoryName != null) {
            listOf(
                BookCategoryItemDto(
                    category =
                        BookCategoryDetailDto(
                            id = categoryId,
                            translations =
                                listOf(
                                    CategoryTranslationRefDto(
                                        id = categoryId,
                                        name = categoryName,
                                        language = CategoryLanguageRefDto(id = 1),
                                    ),
                                ),
                        ),
                ),
            )
        } else {
            emptyList()
        },
    lastUpdated = lastUpdated,
)

private fun detailDto(
    id: Int,
    pages: List<BookPageDto> =
        listOf(
            BookPageDto(
                id = 1,
                pageNumber = 1,
                textContent = "Fox",
                photoUrl = "https://cdn.example/fox.png",
            ),
        ),
) =
    BookDetailDto(
        id = id,
        title = "Downloaded",
        bookPages = pages,
    )

private fun bookRow(
    id: String,
    revision: String?,
) = BookRow(
    id = id,
    title = "Book $id",
    description = "d",
    category = "All",
    categoryIds = emptySet(),
    emoji = "",
    imageUrl = null,
    lastUpdated = revision,
)

private fun detailRow(
    id: String,
    revision: String?,
    cardsJson: String = "[]",
) = BookDetailRow(
    id = id,
    title = "Cached",
    category = "All",
    cardsJson = cardsJson,
    lastUpdated = revision,
)
