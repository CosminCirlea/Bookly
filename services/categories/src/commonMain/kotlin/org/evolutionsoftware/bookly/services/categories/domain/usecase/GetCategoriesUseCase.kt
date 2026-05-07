package org.evolutionsoftware.bookly.services.categories.domain.usecase

import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.core.usecase.utils.withExceptionHandling
import org.evolutionsoftware.bookly.services.categories.domain.exception.CategoriesServiceException
import org.evolutionsoftware.bookly.services.categories.domain.model.Category
import org.evolutionsoftware.bookly.services.categories.domain.repository.CategoriesRepository

interface GetCategoriesUseCase {
    suspend operator fun invoke(): Result<List<Category>, GetCategoriesError>
}

class GetCategoriesUseCaseImpl(
    private val repository: CategoriesRepository,
) : GetCategoriesUseCase {
    override suspend fun invoke(): Result<List<Category>, GetCategoriesError> =
        withExceptionHandling(
            errorMapper = { exception ->
                when (exception) {
                    is CategoriesServiceException.Unauthorized -> GetCategoriesError.Unauthorized
                    is CategoriesServiceException.NetworkError -> GetCategoriesError.NetworkError
                    is CategoriesServiceException.ServerError -> GetCategoriesError.ServerError
                    else -> GetCategoriesError.Unknown(exception.message ?: "Unknown error")
                }
            },
        ) {
            repository.getCategories()
        }
}

sealed interface GetCategoriesError {
    data object Unauthorized : GetCategoriesError
    data object NetworkError : GetCategoriesError
    data object ServerError : GetCategoriesError
    data class Unknown(val message: String) : GetCategoriesError
}
