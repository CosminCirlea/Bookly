package org.evolutionsoftware.bookly.services.categories.data.repository

import org.evolutionsoftware.bookly.services.categories.data.api.CategoriesRemoteDataSource
import org.evolutionsoftware.bookly.services.categories.data.error.withExceptionWrapping
import org.evolutionsoftware.bookly.services.categories.data.local.CategoriesCache
import org.evolutionsoftware.bookly.services.categories.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.categories.domain.model.Category
import org.evolutionsoftware.bookly.services.categories.domain.repository.CategoriesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CategoriesRepositoryImpl(
    private val remote: CategoriesRemoteDataSource,
    private val cache: CategoriesCache,
) : CategoriesRepository {
    private val refreshMutex = Mutex()
    private val revalidatedLanguages = mutableSetOf<Int>()

    override suspend fun getCategories(languageId: Int): List<Category> {
        if (languageId in revalidatedLanguages) return cache.getCategories(languageId)

        return refreshMutex.withLock {
            if (languageId in revalidatedLanguages) return@withLock cache.getCategories(languageId)

            val cached = cache.getCategories(languageId)
            try {
                val categories =
                    withExceptionWrapping {
                        remote
                            .getCategories(languageId)
                            .data
                            .asSequence()
                            .filter { it.status }
                            .map { it.toDomain(languageId) }
                            .filter { it.name.isNotBlank() }
                            .sortedBy { it.name }
                            .toList()
                    }
                cache.replaceCategories(languageId, categories)
                revalidatedLanguages += languageId
                categories
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                cached.ifEmpty { throw e }
            }
        }
    }
}
