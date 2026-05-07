package org.evolutionsoftware.bookly.services.auth.domain.refresher

import org.evolutionsoftware.bookly.core.network.AuthToken
import org.evolutionsoftware.bookly.core.network.TokenRefresher
import org.evolutionsoftware.bookly.services.auth.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthTokenRefresher : TokenRefresher, KoinComponent {
    private val repository: AuthRepository by inject()

    override suspend fun refresh(currentToken: AuthToken?): AuthToken? {
        val refreshToken = currentToken?.refreshToken ?: return null
        return try {
            val session = repository.refreshToken(refreshToken)
            AuthToken(
                accessToken = session.accessToken,
                refreshToken = session.refreshToken,
            )
        } catch (e: Exception) {
            null
        }
    }
}
