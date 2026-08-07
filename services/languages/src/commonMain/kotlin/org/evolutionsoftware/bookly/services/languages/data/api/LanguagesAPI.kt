package org.evolutionsoftware.bookly.services.languages.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.evolutionsoftware.bookly.services.languages.data.dto.LanguageDto
import org.evolutionsoftware.bookly.services.languages.data.repository.requireSuccess

class LanguagesAPI(
    private val httpClient: HttpClient,
) {
    suspend fun getLanguages(): List<LanguageDto> =
        httpClient
            .get(LANGUAGES_PATH)
            .requireSuccess()
            .body()

    private companion object {
        private const val LANGUAGES_PATH = "api/languages"
    }
}
