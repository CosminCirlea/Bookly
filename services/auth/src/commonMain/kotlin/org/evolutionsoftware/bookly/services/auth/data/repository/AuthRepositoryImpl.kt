package org.evolutionsoftware.bookly.services.auth.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.services.auth.data.dto.AuthResponseDto
import org.evolutionsoftware.bookly.services.auth.data.dto.LoginRequestDto
import org.evolutionsoftware.bookly.services.auth.data.dto.RefreshTokenRequestDto
import org.evolutionsoftware.bookly.services.auth.data.dto.RegisterRequestDto
import org.evolutionsoftware.bookly.services.auth.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.auth.domain.exception.AuthServiceException
import org.evolutionsoftware.bookly.services.auth.domain.model.AuthSession
import org.evolutionsoftware.bookly.services.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
) : AuthRepository {
    override suspend fun login(email: String, password: String): AuthSession {
        val response =
            httpClient.post(LOGIN_PATH) {
                setBody(LoginRequestDto(email = email, password = password))
            }

        if (!response.status.isSuccess()) {
            logger.d("Login failed with status ${response.status.value}")
            throw mapStatusToException(response.status)
        }

        return response.body<AuthResponseDto>().toDomain()
    }

    override suspend fun register(email: String, password: String) {
        val response =
            httpClient.post(REGISTER_PATH) {
                setBody(RegisterRequestDto(email = email, password = password))
            }

        if (!response.status.isSuccess()) {
            val body = runCatching { response.bodyAsText() }.getOrNull()
            logger.d("Register failed with status ${response.status.value}: $body")
            throw mapStatusToException(response.status)
        }
    }

    override suspend fun logout() {
        // No server-side logout endpoint; tokens are cleared locally by the use case.
    }

    override suspend fun refreshToken(refreshToken: String): AuthSession {
        val response =
            httpClient.post(REFRESH_PATH) {
                setBody(RefreshTokenRequestDto(refreshToken = refreshToken))
            }

        if (!response.status.isSuccess()) {
            throw mapStatusToException(response.status)
        }

        return response.body<AuthResponseDto>().toDomain()
    }

    private fun mapStatusToException(status: HttpStatusCode): AuthServiceException =
        when (status) {
            HttpStatusCode.BadRequest ->
                AuthServiceException.ValidationError("Invalid request data.")
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Forbidden,
            -> AuthServiceException.Unauthorized()
            HttpStatusCode.NotFound -> AuthServiceException.NotFound()
            else ->
                if (status.value in 500..599) {
                    AuthServiceException.ServerError()
                } else {
                    AuthServiceException.NetworkError("Request failed with status ${status.value}.")
                }
        }

    private companion object {
        private const val LOGIN_PATH = "api/auth/login"
        private const val REGISTER_PATH = "api/auth/signup"
        private const val REFRESH_PATH = "api/auth/refresh"
        private val logger = Logger.withTag("AuthRepository")
    }
}
