package org.evolutionsoftware.bookly.services.categories.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.evolutionsoftware.bookly.services.categories.data.dto.CategoriesPaginatedResponseDto
import org.evolutionsoftware.bookly.services.categories.data.error.requireSuccess

class CategoriesAPI(
    private val httpClient: HttpClient,
) {
    suspend fun getCategories(languageId: Int, limit: Int = 100): CategoriesPaginatedResponseDto =
        httpClient
            .get(CATEGORIES_PATH) {
                parameter("language_id", languageId)
                parameter("limit", limit)
            }
            .requireSuccess()
            .body()

    private companion object {
        private const val CATEGORIES_PATH = "api/categories"
    }
}
