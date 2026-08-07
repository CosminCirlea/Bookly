package org.evolutionsoftware.bookly.services.catalog.domain.usecase

import org.evolutionsoftware.bookly.services.catalog.domain.model.BookSummary
import org.evolutionsoftware.bookly.services.catalog.domain.model.CatalogRefresh
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

class GetBooksUseCase(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(refresh: CatalogRefresh = CatalogRefresh.Automatic): List<BookSummary> =
        repository.getBooks(refresh)
}
