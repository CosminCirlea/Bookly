package org.evolutionsoftware.bookly.services.auth.data.repository

import org.evolutionsoftware.bookly.services.auth.data.api.AuthAPI
import org.evolutionsoftware.bookly.services.auth.data.dto.LoginRequestDto
import org.evolutionsoftware.bookly.services.auth.data.dto.RefreshTokenRequestDto
import org.evolutionsoftware.bookly.services.auth.data.dto.RegisterRequestDto
import org.evolutionsoftware.bookly.services.auth.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.auth.domain.model.AuthSession
import org.evolutionsoftware.bookly.services.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthAPI,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthSession = withExceptionWrapping {
        api.login(LoginRequestDto(email = email, password = password)).toDomain()
    }

    override suspend fun register(email: String, password: String): Unit = withExceptionWrapping {
        api.register(RegisterRequestDto(email = email, password = password))
    }

    override suspend fun logout() {
        // No server-side logout endpoint; tokens are cleared locally by the use case.
    }

    override suspend fun refreshToken(refreshToken: String): AuthSession = withExceptionWrapping {
        api.refreshToken(RefreshTokenRequestDto(refreshToken = refreshToken)).toDomain()
    }
}
