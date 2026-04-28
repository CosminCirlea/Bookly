package org.evolutionsoftware.bookly.services.catalog.domain.model

data class BookDetails(
    val id: String,
    val title: String,
    val category: BookCategory,
    val cards: List<BookCard>,
)
