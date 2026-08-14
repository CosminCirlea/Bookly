package org.evolutionsoftware.bookly.services.catalog.data.api

import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BooksPaginatedResponseDto

/**
 * The catalog's network surface. Abstracted from the Ktor client so the caching
 * policy can be tested without a transport.
 */
interface CatalogRemoteDataSource {
    suspend fun getBooks(
        limit: Int = DEFAULT_PAGE_SIZE,
        page: Int = 1,
    ): BooksPaginatedResponseDto

    suspend fun getBookLastUpdated(bookId: String): String? {
        var page = 1
        while (true) {
            val response = getBooks(page = page)
            response.data
                .firstOrNull { it.id.toString() == bookId }
                ?.let { return it.cacheLastUpdated }

            if (page >= response.pagination.totalPages) return null
            page++
        }
    }

    /** @return null when the book does not exist upstream. */
    suspend fun getBookDetails(
        bookId: String,
        languageId: Int,
    ): List<BookDetailDto>?

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }
}
