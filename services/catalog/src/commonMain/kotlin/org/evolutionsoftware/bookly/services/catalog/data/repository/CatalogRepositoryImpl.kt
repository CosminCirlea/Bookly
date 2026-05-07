package org.evolutionsoftware.bookly.services.catalog.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BooksPaginatedResponseDto
import org.evolutionsoftware.bookly.services.catalog.data.mapper.DEFAULT_LANGUAGE_ID
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toDetails
import org.evolutionsoftware.bookly.services.catalog.data.mapper.toSummary
import org.evolutionsoftware.bookly.services.catalog.domain.exception.CatalogServiceException
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

class CatalogRepositoryImpl(
    private val httpClient: HttpClient,
) : CatalogRepository {
    override suspend fun getBooks(forceRefresh: Boolean): List<BookSummary> {
        val response =
            httpClient.get(BOOKS_PATH) {
                parameter("limit", 100)
            }

        if (!response.status.isSuccess()) {
            logger.d("Get books failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }

        return response
            .body<BooksPaginatedResponseDto>()
            .data
            .map { it.toSummary(DEFAULT_LANGUAGE_ID) }
    }

    override suspend fun getBookDetails(
        bookId: String,
        forceRefresh: Boolean,
    ): BookDetails? {
        val response = httpClient.get("$BOOKS_PATH/$bookId/languages/$DEFAULT_LANGUAGE_ID")

        if (!response.status.isSuccess()) {
            if (response.status == HttpStatusCode.NotFound) return null
            logger.d("Get book details failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }

        return response.body<BookDetailDto>().toDetails()
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
