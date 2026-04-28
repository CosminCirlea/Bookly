package org.evolutionsoftware.bookly.services.catalog.domain.usecase

import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

class GetBooksUseCase(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): List<BookSummary> =
        repository.getBooks(forceRefresh)
}
