package org.evolutionsoftware.bookly.services.catalog.domain.repository

import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.model.CatalogRefresh

interface CatalogRepository {
    /**
     * The catalog list. Under [CatalogRefresh.Automatic] this revalidates against the
     * server once per app session and is served from disk for the rest of it.
     *
     * @throws org.evolutionsoftware.bookly.services.catalog.domain.exception.CatalogServiceException
     *   only when there is nothing cached to fall back on.
     */
    suspend fun getBooks(refresh: CatalogRefresh = CatalogRefresh.Automatic): List<BookSummary>

    /**
     * A book's pages. Under [CatalogRefresh.Automatic] these are downloaded once and
     * then served from disk indefinitely, until the catalog list reports a different
     * last-updated value for the book.
     *
     * @param knownLastUpdated the book's last-updated value as already known by the
     *   caller (typically read straight from the catalog list that is showing this
     *   book). When supplied under [CatalogRefresh.Automatic], it is trusted instead
     *   of re-fetching the catalog just to look up the same value, saving a network
     *   round trip.
     * @return null when the book exists in neither the cache nor the catalog.
     */
    suspend fun getBookDetails(
        bookId: String,
        refresh: CatalogRefresh = CatalogRefresh.Automatic,
        knownLastUpdated: String? = null,
    ): BookDetails?
}
