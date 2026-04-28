package org.evolutionsoftware.bookly.services.catalog.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val emoji: String,
    val imageUrl: String? = null,
    val cards: List<BookCardDto>,
)

@Serializable
data class BookCardDto(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val imageUrl: String? = null,
)
