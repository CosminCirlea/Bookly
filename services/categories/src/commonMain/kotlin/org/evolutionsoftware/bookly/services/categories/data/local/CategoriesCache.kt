package org.evolutionsoftware.bookly.services.categories.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.categories.domain.model.Category

interface CategoriesCache {
    suspend fun getCategories(languageId: Int): List<Category>

    suspend fun replaceCategories(
        languageId: Int,
        categories: List<Category>,
    )
}

class CategoriesLocalDataSource(
    driverFactory: DatabaseDriverFactory,
) : CategoriesCache {
    private val database = CategoriesDatabase(driverFactory.createDriver())
    private val queries = database.categoryEntityQueries

    override suspend fun getCategories(languageId: Int): List<Category> =
        withContext(Dispatchers.IO) {
            queries.selectCategories(languageId.toLong()).executeAsList().map {
                Category(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.imageUrl,
                )
            }
        }

    override suspend fun replaceCategories(
        languageId: Int,
        categories: List<Category>,
    ): Unit =
        withContext(Dispatchers.IO) {
            val cachedAt = Clock.System.now().toEpochMilliseconds()
            queries.transaction {
                queries.deleteCategoriesForLanguage(languageId.toLong())
                categories.forEach { category ->
                    queries.insertCategory(
                        id = category.id,
                        languageId = languageId.toLong(),
                        name = category.name,
                        imageUrl = category.imageUrl,
                        cachedAt = cachedAt,
                    )
                }
            }
            logger.d("replaceCategories: cached ${categories.size} filters for language $languageId")
        }

    private companion object {
        val logger = Logger.withTag("CategoriesLocalDataSource")
    }
}
