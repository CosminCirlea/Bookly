package org.evolutionsoftware.bookly.services.catalog.domain.model

data class BookSummary(
    val id: String,
    val title: String,
    val description: String,
    val category: BookCategory,
    val emoji: String,
    val imageUrl: String? = null,
)
