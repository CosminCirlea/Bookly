package org.evolutionsoftware.bookly.services.languages.domain.repository

import org.evolutionsoftware.bookly.services.languages.domain.model.Language

interface LanguagesRepository {
    suspend fun getLanguages(): List<Language>
}
