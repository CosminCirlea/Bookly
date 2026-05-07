package org.evolutionsoftware.bookly.services.categories.domain.repository

import org.evolutionsoftware.bookly.services.categories.domain.model.Category

interface CategoriesRepository {
    suspend fun getCategories(languageId: Int = 1): List<Category>
}
