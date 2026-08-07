package org.evolutionsoftware.bookly.services.catalog.data.api

import org.evolutionsoftware.bookly.services.catalog.data.dto.BookDetailDto
import org.evolutionsoftware.bookly.services.catalog.data.dto.BooksPaginatedResponseDto

/**
 * The catalog's network surface. Abstracted from the Ktor client so the caching
 * policy can be tested without a transport.
 */
interface CatalogRemoteDataSource {
    suspend fun getBooks(limit: Int = DEFAULT_PAGE_SIZE): BooksPaginatedResponseDto

    /** @return null when the book does not exist upstream. */
    suspend fun getBookDetails(
        bookId: String,
        languageId: Int,
    ): List<BookDetailDto>?

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }
}
