package org.evolutionsoftware.bookly.services.categories.data.mapper

import org.evolutionsoftware.bookly.services.categories.data.dto.CategoryDto
import org.evolutionsoftware.bookly.services.categories.domain.model.Category

internal fun CategoryDto.toDomain(languageId: Int = DEFAULT_LANGUAGE_ID): Category {
    val name = translations
        .firstOrNull { (it.languageId ?: it.language?.id) == languageId }
        ?.name
        ?: translations.firstOrNull()?.name
        ?: ""
    return Category(id = id.toString(), name = name, imageUrl = photoUrl)
}

internal const val DEFAULT_LANGUAGE_ID = 1
