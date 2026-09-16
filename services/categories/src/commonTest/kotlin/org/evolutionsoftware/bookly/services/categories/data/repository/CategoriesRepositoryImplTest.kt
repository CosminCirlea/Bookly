package org.evolutionsoftware.bookly.services.categories.data.repository

import kotlinx.coroutines.test.runTest
import org.evolutionsoftware.bookly.services.categories.data.api.CategoriesRemoteDataSource
import org.evolutionsoftware.bookly.services.categories.data.dto.CategoriesPaginatedResponseDto
import org.evolutionsoftware.bookly.services.categories.data.dto.CategoryDto
import org.evolutionsoftware.bookly.services.categories.data.dto.CategoryPaginationDto
import org.evolutionsoftware.bookly.services.categories.data.dto.CategoryTranslationDto
import org.evolutionsoftware.bookly.services.categories.data.local.CategoriesCache
import org.evolutionsoftware.bookly.services.categories.domain.model.Category
import kotlin.test.Test
import kotlin.test.assertEquals

class CategoriesRepositoryImplTest {
    @Test
    fun `filters are fetched once per session and served from cache afterwards`() =
        runTest {
            val cache = InMemoryCategoriesCache()
            val remote = FakeRemote(categories = listOf(categoryDto(1, "Animals")))
            val repository = CategoriesRepositoryImpl(remote, cache)

            assertEquals(listOf(Category("1", "Animals")), repository.getCategories())
            assertEquals(listOf(Category("1", "Animals")), repository.getCategories())

            assertEquals(1, remote.requests)
            assertEquals(listOf(Category("1", "Animals")), cache.categoriesByLanguage[1])
        }

    @Test
    fun `cached filters are returned when the refresh fails`() =
        runTest {
            val cached = listOf(Category("1", "Animals"))
            val repository =
                CategoriesRepositoryImpl(
                    FakeRemote(fail = true),
                    InMemoryCategoriesCache(mutableMapOf(1 to cached)),
                )

            assertEquals(cached, repository.getCategories())
        }

    @Test
    fun `filters are cached independently for each language`() =
        runTest {
            val cache = InMemoryCategoriesCache()
            val repository =
                CategoriesRepositoryImpl(
                    FakeRemote(
                        categories =
                            listOf(
                                categoryDto(
                                    id = 1,
                                    englishName = "Animals",
                                    romanianName = "Animale",
                                ),
                            ),
                    ),
                    cache,
                )

            assertEquals(listOf(Category("1", "Animals")), repository.getCategories(languageId = 1))
            assertEquals(listOf(Category("1", "Animale")), repository.getCategories(languageId = 2))
            assertEquals(
                mapOf(
                    1 to listOf(Category("1", "Animals")),
                    2 to listOf(Category("1", "Animale")),
                ),
                cache.categoriesByLanguage,
            )
        }
}

private class FakeRemote(
    private val categories: List<CategoryDto> = emptyList(),
    private val fail: Boolean = false,
) : CategoriesRemoteDataSource {
    var requests = 0
        private set

    override suspend fun getCategories(
        languageId: Int,
        limit: Int,
    ): CategoriesPaginatedResponseDto {
        requests++
        if (fail) error("network down")
        return CategoriesPaginatedResponseDto(
            data = categories,
            pagination = CategoryPaginationDto(total = categories.size, page = 1, limit = limit, totalPages = 1),
        )
    }
}

private class InMemoryCategoriesCache(
    val categoriesByLanguage: MutableMap<Int, List<Category>> = mutableMapOf(),
) : CategoriesCache {
    override suspend fun getCategories(languageId: Int): List<Category> =
        categoriesByLanguage[languageId].orEmpty()

    override suspend fun replaceCategories(
        languageId: Int,
        categories: List<Category>,
    ) {
        categoriesByLanguage[languageId] = categories
    }
}

private fun categoryDto(
    id: Int,
    englishName: String,
    romanianName: String = englishName,
) =
    CategoryDto(
        id = id,
        translations =
            listOf(
                CategoryTranslationDto(id = id, name = englishName, languageId = 1),
                CategoryTranslationDto(id = id, name = romanianName, languageId = 2),
            ),
    )
