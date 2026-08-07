package org.evolutionsoftware.bookly.services.categories.data.repository

import org.evolutionsoftware.bookly.services.categories.data.api.CategoriesAPI
import org.evolutionsoftware.bookly.services.categories.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.categories.domain.model.Category
import org.evolutionsoftware.bookly.services.categories.domain.repository.CategoriesRepository

class CategoriesRepositoryImpl(
    private val api: CategoriesAPI,
) : CategoriesRepository {

    override suspend fun getCategories(languageId: Int): List<Category> = withExceptionWrapping {
        api.getCategories(languageId).data.map { it.toDomain(languageId) }
    }
}
