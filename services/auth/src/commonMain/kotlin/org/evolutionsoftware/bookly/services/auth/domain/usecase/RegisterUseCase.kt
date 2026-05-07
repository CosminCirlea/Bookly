package org.evolutionsoftware.bookly.services.auth.domain.usecase

import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.core.usecase.utils.withExceptionHandling
import org.evolutionsoftware.bookly.services.auth.domain.exception.AuthServiceException
import org.evolutionsoftware.bookly.services.auth.domain.model.AuthSession
import org.evolutionsoftware.bookly.services.auth.domain.repository.AuthRepository

interface RegisterUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Result<AuthSession, RegisterError>
}

class RegisterUseCaseImpl(
    private val repository: AuthRepository,
) : RegisterUseCase {
    override suspend fun invoke(
        email: String,
        password: String,
    ): Result<AuthSession, RegisterError> =
        withExceptionHandling(
            errorMapper = { exception ->
                when (exception) {
                    is AuthServiceException.ValidationError -> RegisterError.ValidationError(exception.message)
                    is AuthServiceException.Unauthorized -> RegisterError.Unauthorized
                    is AuthServiceException.NetworkError -> RegisterError.NetworkError
                    is AuthServiceException.ServerError -> RegisterError.ServerError(exception.message)
                    else -> RegisterError.Unknown(exception.message ?: "Unknown error")
                }
            },
        ) {
            repository.register(email, password)
            repository.login(email, password)
        }
}

sealed interface RegisterError {
    data class ValidationError(val message: String?) : RegisterError
    data object Unauthorized : RegisterError
    data object NetworkError : RegisterError
    data class ServerError(val message: String?) : RegisterError
    data class Unknown(val message: String) : RegisterError
}
