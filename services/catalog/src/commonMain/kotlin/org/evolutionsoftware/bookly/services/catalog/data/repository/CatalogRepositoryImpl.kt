package org.evolutionsoftware.bookly.services.catalog.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BooksPaginatedResponseDto
import org.evolutionsoftware.bookly.services.catalog.data.local.CatalogLocalDataSource
import org.evolutionsoftware.bookly.services.catalog.data.mapper.DEFAULT_LANGUAGE_ID
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toDetails
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toSummary
import org.evolutionsoftware.bookly.services.catalog.domain.exception.CatalogServiceException
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

class CatalogRepositoryImpl(
    private val httpClient: HttpClient,
    private val localDataSource: CatalogLocalDataSource,
) : CatalogRepository {
    override suspend fun getBooks(forceRefresh: Boolean): List<BookSummary> {
        logger.d("getBooks called, forceRefresh=$forceRefresh")
        val cachedBooks =
            try {
                val books = withContext(Dispatchers.IO) { localDataSource.getBooks() }
                logger.d("Cache returned ${books.size} books")
                books
            } catch (e: Exception) {
                logger.d("Failed to read from cache: ${e.message}")
                e.printStackTrace()
                emptyList()
            }

        if (cachedBooks.isNotEmpty() && !forceRefresh) {
            logger.d("Returning ${cachedBooks.size} books from cache")
            return cachedBooks
        }

        logger.d("Cache empty or forceRefresh, fetching from network...")
        return try {
            fetchBooksFromNetwork()
        } catch (e: Exception) {
            logger.d("Network failed: ${e.message}")
            if (cachedBooks.isNotEmpty()) {
                logger.d("Returning ${cachedBooks.size} cached books as fallback")
                cachedBooks
            } else {
                logger.d("No cached books available, throwing exception")
                throw e
            }
        }
    }

    private suspend fun fetchBooksFromNetwork(): List<BookSummary> {
        val response =
            httpClient.get(BOOKS_PATH) {
                parameter("limit", 100)
            }

        if (!response.status.isSuccess()) {
            logger.d("Get books failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }

        val books =
            response
                .body<BooksPaginatedResponseDto>()
                .data
                .map { it.toSummary(DEFAULT_LANGUAGE_ID) }

        try {
            withContext(Dispatchers.IO) { localDataSource.saveBooks(books) }
            logger.d("Cached ${books.size} books to local database")
        } catch (e: Exception) {
            logger.d("Failed to cache books: ${e.message}")
        }

        return books
    }

    override suspend fun getBookDetails(
        bookId: String,
        forceRefresh: Boolean,
    ): BookDetails? {
        val cachedDetails =
            try {
                withContext(Dispatchers.IO) { localDataSource.getBookDetails(bookId) }
            } catch (e: Exception) {
                logger.d("Failed to read book details from cache: ${e.message}")
                null
            }

        if (cachedDetails != null && !forceRefresh) {
            logger.d("Returning book details for $bookId from cache")
            return cachedDetails
        }

        return try {
            fetchBookDetailsFromNetwork(bookId)
        } catch (e: Exception) {
            logger.d("Network failed for book details: ${e.message}")
            if (cachedDetails != null) {
                logger.d("Returning cached details for $bookId as fallback")
                cachedDetails
            } else {
                throw e
            }
        }
    }

    private suspend fun fetchBookDetailsFromNetwork(bookId: String): BookDetails? {
        val response = httpClient.get("$BOOKS_PATH/$bookId/languages/$DEFAULT_LANGUAGE_ID")

        if (!response.status.isSuccess()) {
            if (response.status == HttpStatusCode.NotFound) return null
            logger.d("Get book details failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }

        val details = response.body<BookDetailDto>().toDetails()
        try {
            withContext(Dispatchers.IO) { localDataSource.saveBookDetails(details) }
            logger.d("Cached book details for $bookId")
        } catch (e: Exception) {
            logger.d("Failed to cache book details: ${e.message}")
        }
        return details
    }

    private fun mapStatusToException(status: HttpStatusCode): CatalogServiceException =
        when (status) {
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Forbidden,
            -> CatalogServiceException.Unauthorized()
            HttpStatusCode.NotFound -> CatalogServiceException.NotFound()
            else ->
                if (status.value in 500..599) {
                    CatalogServiceException.ServerError()
                } else {
                    CatalogServiceException.NetworkError("Request failed with status ${status.value}.")
                }
        }

    private companion object {
        private const val BOOKS_PATH = "api/books"
        private val logger = Logger.withTag("CatalogRepository")
    }
}
