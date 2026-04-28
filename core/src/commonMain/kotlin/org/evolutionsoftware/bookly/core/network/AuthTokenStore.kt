package org.evolutionsoftware.bookly.core.network

interface AuthTokenStore {
    suspend fun read(): AuthToken?

    suspend fun write(token: AuthToken)

    suspend fun clear()
}
