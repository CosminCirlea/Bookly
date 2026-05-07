package org.evolutionsoftware.bookly.core.network

interface TokenRefresher {
    suspend fun refresh(currentToken: AuthToken?): AuthToken?
}

data object NoopTokenRefresher : TokenRefresher {
    override suspend fun refresh(currentToken: AuthToken?): AuthToken? = null
}
