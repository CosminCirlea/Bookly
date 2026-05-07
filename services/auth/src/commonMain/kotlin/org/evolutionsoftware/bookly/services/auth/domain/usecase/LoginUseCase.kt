package org.evolutionsoftware.bookly.services.auth.domain.usecase

import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.core.usecase.utils.withExceptionHandling
import org.evolutionsoftware.bookly.services.auth.domain.exception.AuthServiceException
import org.evolutionsoftware.bookly.services.auth.domain.model.AuthSession
import org.evolutionsoftware.bookly.services.auth.domain.repository.AuthRepository

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): Result<AuthSession, LoginError>
}

class LoginUseCaseImpl(
    private val repository: AuthRepository,
) : LoginUseCase {
    override suspend fun invoke(email: String, password: String): Result<AuthSession, LoginError> =
        withExceptionHandling(
            errorMapper = { exception ->
                when (exception) {
                    is AuthServiceException.ValidationError -> LoginError.ValidationError(exception.message)
                    is AuthServiceException.Unauthorized -> LoginError.InvalidCredentials
                    is AuthServiceException.NetworkError -> LoginError.NetworkError
                    is AuthServiceException.ServerError -> LoginError.ServerError(exception.message)
                    else -> LoginError.Unknown(exception.message ?: "Unknown error")
                }
            },
        ) {
            repository.login(email, password)
        }
}

sealed interface LoginError {
    data class ValidationError(val message: String?) : LoginError
    data object InvalidCredentials : LoginError
    data object NetworkError : LoginError
    data class ServerError(val message: String?) : LoginError
    data class Unknown(val message: String) : LoginError
}
