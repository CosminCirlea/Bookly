package org.evolutionsoftware.bookly.services.catalog.domain.usecase

import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

class GetBookDetailsUseCase(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(
        bookId: String,
        forceRefresh: Boolean = false,
    ): BookDetails? = repository.getBookDetails(bookId, forceRefresh)
}
