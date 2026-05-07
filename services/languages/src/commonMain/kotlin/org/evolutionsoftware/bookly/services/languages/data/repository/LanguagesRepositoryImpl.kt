package org.evolutionsoftware.bookly.services.languages.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.languages.data.dto.LanguageDto
import org.evolutionsoftware.bookly.services.languages.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.languages.domain.exception.LanguagesServiceException
import org.evolutionsoftware.bookly.services.languages.domain.model.Language
import org.evolutionsoftware.bookly.services.languages.domain.repository.LanguagesRepository

class LanguagesRepositoryImpl(
    private val httpClient: HttpClient,
) : LanguagesRepository {
    override suspend fun getLanguages(): List<Language> {
        val response = httpClient.get(LANGUAGES_PATH)

        if (!response.status.isSuccess()) {
            logger.d("Get languages failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }

        return response.body<List<LanguageDto>>().map { it.toDomain() }
    }

    private fun mapStatusToException(status: HttpStatusCode): LanguagesServiceException =
        when (status) {
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Forbidden,
            -> LanguagesServiceException.Unauthorized()
            else ->
                if (status.value in 500..599) {
                    LanguagesServiceException.ServerError()
                } else {
                    LanguagesServiceException.NetworkError("Request failed with status ${status.value}.")
                }
        }

    private companion object {
        private const val LANGUAGES_PATH = "api/languages"
        private val logger = Logger.withTag("LanguagesRepository")
    }
}
