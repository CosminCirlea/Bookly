package org.evolutionsoftware.bookly.services.languages.data.mapper

import org.evolutionsoftware.bookly.services.languages.data.dto.LanguageDto
import org.evolutionsoftware.bookly.services.languages.domain.model.Language

internal fun LanguageDto.toDomain(): Language =
    Language(id = id.toString(), name = name, countryCode = countryCode)
