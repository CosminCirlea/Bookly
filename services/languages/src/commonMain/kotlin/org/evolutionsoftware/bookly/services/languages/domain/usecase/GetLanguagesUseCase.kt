package org.evolutionsoftware.bookly.services.languages.domain.usecase

import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.core.usecase.utils.withExceptionHandling
import org.evolutionsoftware.bookly.services.languages.domain.exception.LanguagesServiceException
import org.evolutionsoftware.bookly.services.languages.domain.model.Language
import org.evolutionsoftware.bookly.services.languages.domain.repository.LanguagesRepository

interface GetLanguagesUseCase {
    suspend operator fun invoke(): Result<List<Language>, GetLanguagesError>
}

class GetLanguagesUseCaseImpl(
    private val repository: LanguagesRepository,
) : GetLanguagesUseCase {
    override suspend fun invoke(): Result<List<Language>, GetLanguagesError> =
        withExceptionHandling(
            errorMapper = { exception ->
                when (exception) {
                    is LanguagesServiceException.Unauthorized -> GetLanguagesError.Unauthorized
                    is LanguagesServiceException.NetworkError -> GetLanguagesError.NetworkError
                    is LanguagesServiceException.ServerError -> GetLanguagesError.ServerError
                    else -> GetLanguagesError.Unknown(exception.message ?: "Unknown error")
                }
            },
        ) {
            repository.getLanguages()
        }
}

sealed interface GetLanguagesError {
    data object Unauthorized : GetLanguagesError
    data object NetworkError : GetLanguagesError
    data object ServerError : GetLanguagesError
    data class Unknown(val message: String) : GetLanguagesError
}
