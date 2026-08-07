package org.evolutionsoftware.bookly.services.catalog.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BooksPaginatedResponseDto
import org.evolutionsoftware.bookly.services.catalog.data.error.requireSuccess

class CatalogAPI(
    private val httpClient: HttpClient,
) : CatalogRemoteDataSource {
    override suspend fun getBooks(limit: Int): BooksPaginatedResponseDto =
        httpClient
            .get(BOOKS_PATH) { parameter("limit", limit) }
            .requireSuccess()
            .body()

    override suspend fun getBookDetails(
        bookId: String,
        languageId: Int,
    ): List<BookDetailDto>? {
        val response = httpClient.get("$BOOKS_PATH/$bookId/languages/$languageId")
        if (response.status == HttpStatusCode.NotFound) return null
        return response.requireSuccess().body()
    }

    private companion object {
        const val BOOKS_PATH = "api/books"
    }
}
