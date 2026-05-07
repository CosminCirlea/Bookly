package org.evolutionsoftware.bookly.services.auth.domain.repository

import org.evolutionsoftware.bookly.services.auth.domain.model.AuthSession

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthSession
    suspend fun register(email: String, password: String)
    suspend fun logout()
    suspend fun refreshToken(refreshToken: String): AuthSession
}
