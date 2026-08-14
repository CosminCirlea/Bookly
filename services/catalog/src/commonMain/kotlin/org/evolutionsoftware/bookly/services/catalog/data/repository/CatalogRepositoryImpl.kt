package org.evolutionsoftware.bookly.services.catalog.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.catalog.data.api.CatalogRemoteDataSource
import org.evolutionsoftware.bookly.services.catalog.data.error.withExceptionWrapping
import org.evolutionsoftware.bookly.services.catalog.data.local.CatalogCache
import org.evolutionsoftware.bookly.services.catalog.data.mapper.DEFAULT_LANGUAGE_ID
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toDetails
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toRow
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toSummary
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.model.CatalogRefresh
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

/**
 * Offline-first catalog.
 *
 * The cache is the source of truth for everything the UI renders: reads are always
 * answered from disk, and the network is only consulted when it can tell us something
 * we do not already know.
 *
 * - **The list** is revalidated once per app session. Navigating around the app, or
 *   returning to the home screen, costs nothing.
 * - **A book's pages** are downloaded once and then served from disk forever, until
 *   the list reports a different content revision for that book. This is where the
 *   bulk of the payload lives, so it is the saving that matters most.
 */
class CatalogRepositoryImpl(
    private val remote: CatalogRemoteDataSource,
    private val cache: CatalogCache,
) : CatalogRepository {
    private val listRefreshMutex = Mutex()

    /**
     * Whether the catalog has been revalidated during this process lifetime. Resetting
     * on process death is intentional: "once per app open" is exactly the contract.
     */
    private var listRevalidated = false

    override suspend fun getBooks(refresh: CatalogRefresh): List<BookSummary> {
        val shouldRevalidate =
            when (refresh) {
                CatalogRefresh.CacheOnly -> false
                CatalogRefresh.Force -> true
                // An empty cache still has to reach the network, session flag or not.
                CatalogRefresh.Automatic -> !listRevalidated || !cache.hasBooks()
            }

        if (!shouldRevalidate) {
            logger.d("getBooks: served from cache")
            return cachedBooks()
        }

        // Serialised so a burst of concurrent callers produces one request, not several.
        return listRefreshMutex.withLock {
            if (refresh == CatalogRefresh.Automatic && listRevalidated && cache.hasBooks()) {
                return@withLock cachedBooks()
            }
            downloadBooks()
        }
    }

    private suspend fun downloadBooks(): List<BookSummary> {
        val fallback = cachedBooks()
        return try {
            val rows =
                withExceptionWrapping {
                    remote.getBooks().data.map { it.toRow(DEFAULT_LANGUAGE_ID) }
                }
            cache.replaceBooks(rows)
            listRevalidated = true
            logger.d("getBooks: revalidated ${rows.size} books from network")
            // Re-read so callers see exactly what was persisted.
            cachedBooks()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            logger.d("getBooks: refresh failed (${e.message}); falling back to cache")
            fallback.ifEmpty { throw e }
        }
    }

    override suspend fun getBookDetails(
        bookId: String,
        refresh: CatalogRefresh,
    ): BookDetails? {
        val cached = cache.getBookDetails(bookId)
        // The revision the catalog last advertised for this book.
        val advertisedRevision = cache.getBookRevision(bookId)

        val shouldDownload =
            when (refresh) {
                CatalogRefresh.CacheOnly -> false
                CatalogRefresh.Force -> true
                CatalogRefresh.Automatic ->
                    when {
                        cached == null -> true
                        // Backend reports no revision, so we have no basis to call the
                        // cache stale. Keeping it costs no traffic, which is the point.
                        advertisedRevision == null -> false
                        else -> cached.revision != advertisedRevision
                    }
            }

        if (!shouldDownload) {
            logger.d("getBookDetails($bookId): served from cache at revision ${cached?.revision}")
            return cached?.toDetails()
        }

        return try {
            val downloaded =
                withExceptionWrapping {
                    remote.getBookDetails(bookId, DEFAULT_LANGUAGE_ID)
                        ?.firstOrNull()
                        ?.toDetails(bookId)
                }
            if (downloaded != null) {
                cache.saveBookDetails(downloaded.toRow(advertisedRevision))
                logger.d("getBookDetails($bookId): downloaded at revision $advertisedRevision")
            }
            // A book missing upstream should not evict a copy the child can still read.
            downloaded ?: cached?.toDetails()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            logger.d("getBookDetails($bookId): download failed (${e.message}); falling back to cache")
            cached?.toDetails() ?: throw e
        }
    }

    private suspend fun cachedBooks(): List<BookSummary> =
        try {
            cache.getBooks().map { it.toSummary() }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            logger.d("cachedBooks: read failed (${e.message})")
            emptyList()
        }

    private companion object {
        val logger = Logger.withTag("CatalogRepository")
    }
}
