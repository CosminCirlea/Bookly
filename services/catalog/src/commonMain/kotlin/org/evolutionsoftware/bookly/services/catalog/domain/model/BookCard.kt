package org.evolutionsoftware.bookly.services.catalog.domain.model

data class BookCard(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val imageUrl: String? = null,
    val imageLastUpdated: String? = null,
)
