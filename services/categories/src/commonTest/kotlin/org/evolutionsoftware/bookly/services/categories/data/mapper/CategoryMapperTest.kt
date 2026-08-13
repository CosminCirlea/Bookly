package org.evolutionsoftware.bookly.services.categories.data.mapper

import org.evolutionsoftware.bookly.services.categories.data.dto.CategoryDto
import org.evolutionsoftware.bookly.services.categories.data.dto.CategoryLanguageDto
import org.evolutionsoftware.bookly.services.categories.data.dto.CategoryTranslationDto
import kotlin.test.Test
import kotlin.test.assertEquals

class CategoryMapperTest {
    @Test
    fun `uses the requested nested language translation`() {
        val category =
            CategoryDto(
                id = 7,
                translations =
                    listOf(
                        CategoryTranslationDto(
                            id = 1,
                            name = "Animale",
                            language = CategoryLanguageDto(id = 2),
                        ),
                        CategoryTranslationDto(
                            id = 2,
                            name = "Animals",
                            language = CategoryLanguageDto(id = 1),
                        ),
                    ),
            )

        assertEquals("Animals", category.toDomain(languageId = 1).name)
    }
}
