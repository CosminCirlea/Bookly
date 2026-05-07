package org.evolutionsoftware.bookly.services.categories.domain.model

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
)
