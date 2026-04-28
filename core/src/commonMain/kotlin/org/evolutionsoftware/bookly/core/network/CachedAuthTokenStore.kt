package org.evolutionsoftware.bookly.core.network

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CachedAuthTokenStore : AuthTokenStore {
    private val mutex = Mutex()
    private var token: AuthToken? = null

    override suspend fun read(): AuthToken? =
        mutex.withLock { token }

    override suspend fun write(token: AuthToken) {
        mutex.withLock { this.token = token }
    }

    override suspend fun clear() {
        mutex.withLock { token = null }
    }
}
