package org.evolutionsoftware.bookly.services.catalog.data.repository

import org.evolutionsoftware.bookly.services.catalog.data.api.CatalogAPI
import org.evolutionsoftware.bookly.services.catalog.data.local.CatalogLocalDataSource
import org.evolutionsoftware.bookly.services.catalog.data.mapper.DEFAULT_LANGUAGE_ID
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toDetails
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toSummary
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

class CatalogRepositoryImpl(
    private val api: CatalogAPI,
    private val localDataSource: CatalogLocalDataSource,
) : CatalogRepository {

    override suspend fun getBooks(forceRefresh: Boolean): List<BookSummary> {
        val cachedBooks = runCatching { localDataSource.getBooks() }.getOrElse { emptyList() }

        if (cachedBooks.isNotEmpty() && !forceRefresh) {
            return cachedBooks
        }

        return runCatching { fetchBooksFromNetwork() }
            .onSuccess { books -> runCatching { localDataSource.saveBooks(books) } }
            .getOrElse { cachedBooks.ifEmpty { throw it } }
    }

    private suspend fun fetchBooksFromNetwork(): List<BookSummary> = withExceptionWrapping {
        api.getBooks()
            .data
            .map { it.toSummary(DEFAULT_LANGUAGE_ID) }
    }

    override suspend fun getBookDetails(bookId: String, forceRefresh: Boolean): BookDetails? {
        val cachedDetails = runCatching { localDataSource.getBookDetails(bookId) }.getOrNull()

        if (cachedDetails != null && !forceRefresh) {
            return cachedDetails
        }

        return runCatching { fetchBookDetailsFromNetwork(bookId) }
            .onSuccess { details -> details?.let { runCatching { localDataSource.saveBookDetails(it) } } }
            .getOrElse { cachedDetails ?: throw it }
    }

    private suspend fun fetchBookDetailsFromNetwork(bookId: String): BookDetails? = withExceptionWrapping {
        api.getBookDetails(bookId, DEFAULT_LANGUAGE_ID)
            ?.firstOrNull()
            ?.toDetails()
    }
}
