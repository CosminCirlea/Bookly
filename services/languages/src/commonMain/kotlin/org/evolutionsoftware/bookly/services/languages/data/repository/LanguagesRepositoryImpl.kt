package org.evolutionsoftware.bookly.services.languages.data.repository

import org.evolutionsoftware.bookly.services.languages.data.api.LanguagesAPI
import org.evolutionsoftware.bookly.services.languages.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.languages.domain.model.Language
import org.evolutionsoftware.bookly.services.languages.domain.repository.LanguagesRepository

class LanguagesRepositoryImpl(
    private val api: LanguagesAPI,
) : LanguagesRepository {

    override suspend fun getLanguages(): List<Language> = withExceptionWrapping {
        api.getLanguages().map { it.toDomain() }
    }
}
