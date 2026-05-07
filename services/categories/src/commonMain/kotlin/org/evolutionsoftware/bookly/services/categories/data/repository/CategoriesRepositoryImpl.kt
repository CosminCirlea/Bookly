package org.evolutionsoftware.bookly.services.categories.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.categories.data.dto.CategoriesPaginatedResponseDto
import org.evolutionsoftware.bookly.services.categories.data.mapper.DEFAULT_LANGUAGE_ID
import org.evolutionsoftware.bookly.services.categories.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.categories.domain.exception.CategoriesServiceException
import org.evolutionsoftware.bookly.services.categories.domain.model.Category
import org.evolutionsoftware.bookly.services.categories.domain.repository.CategoriesRepository

class CategoriesRepositoryImpl(
    private val httpClient: HttpClient,
) : CategoriesRepository {
    override suspend fun getCategories(languageId: Int): List<Category> {
        val response =
            httpClient.get(CATEGORIES_PATH) {
                parameter("language_id", languageId)
                parameter("limit", 100)
            }

        if (!response.status.isSuccess()) {
            logger.d("Get categories failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }

        return response.body<CategoriesPaginatedResponseDto>().data.map { it.toDomain(languageId) }
    }

    private fun mapStatusToException(status: HttpStatusCode): CategoriesServiceException =
        when (status) {
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Forbidden,
            -> CategoriesServiceException.Unauthorized()
            else ->
                if (status.value in 500..599) {
                    CategoriesServiceException.ServerError()
                } else {
                    CategoriesServiceException.NetworkError("Request failed with status ${status.value}.")
                }
        }

    private companion object {
        private const val CATEGORIES_PATH = "api/categories"
        private val logger = Logger.withTag("CategoriesRepository")
    }
}
