package org.evolutionsoftware.bookly.services.catalog.domain.repository

import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary

interface CatalogRepository {
    suspend fun getBooks(forceRefresh: Boolean = false): List<BookSummary>

    suspend fun getBookDetails(
        bookId: String,
        forceRefresh: Boolean = false,
    ): BookDetails?
}
