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
     * content revision for the book.
     *
     * @return null when the book exists in neither the cache nor the catalog.
     */
    suspend fun getBookDetails(
        bookId: String,
        refresh: CatalogRefresh = CatalogRefresh.Automatic,
    ): BookDetails?
}
