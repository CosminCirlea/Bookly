package org.evolutionsoftware.bookly.core.network

interface UserSessionStore {
    suspend fun read(): UserSession?

    suspend fun write(session: UserSession)

    suspend fun clear()
}
