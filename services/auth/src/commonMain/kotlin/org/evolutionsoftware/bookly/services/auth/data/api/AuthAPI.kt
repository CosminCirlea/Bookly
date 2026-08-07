package org.evolutionsoftware.bookly.services.auth.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.evolutionsoftware.bookly.services.auth.data.dto.AuthResponseDto
import org.evolutionsoftware.bookly.services.auth.data.dto.LoginRequestDto
import org.evolutionsoftware.bookly.services.auth.data.dto.RefreshTokenRequestDto
import org.evolutionsoftware.bookly.services.auth.data.dto.RegisterRequestDto
import org.evolutionsoftware.bookly.services.auth.data.repository.requireSuccess

class AuthAPI(
    private val httpClient: HttpClient,
) {
    suspend fun login(request: LoginRequestDto): AuthResponseDto =
        httpClient
            .post(LOGIN_PATH) { setBody(request) }
            .requireSuccess()
            .body()

    suspend fun register(request: RegisterRequestDto) {
        httpClient
            .post(REGISTER_PATH) { setBody(request) }
            .requireSuccess()
    }

    suspend fun refreshToken(request: RefreshTokenRequestDto): AuthResponseDto =
        httpClient
            .post(REFRESH_PATH) { setBody(request) }
            .requireSuccess()
            .body()

    private companion object {
        private const val LOGIN_PATH = "api/auth/login"
        private const val REGISTER_PATH = "api/auth/signup"
        private const val REFRESH_PATH = "api/auth/refresh"
    }
}
