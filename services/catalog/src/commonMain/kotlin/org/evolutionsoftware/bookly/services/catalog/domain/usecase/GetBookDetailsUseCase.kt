package org.evolutionsoftware.bookly.services.catalog.domain.usecase

import org.evolutionsoftware.bookly.services.catalog.domain.model.BookDetails
import org.evolutionsoftware.bookly.services.catalog.domain.model.CatalogRefresh
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository

class GetBookDetailsUseCase(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(
        bookId: String,
        refresh: CatalogRefresh = CatalogRefresh.Automatic,
    ): BookDetails? = repository.getBookDetails(bookId, refresh)
}
