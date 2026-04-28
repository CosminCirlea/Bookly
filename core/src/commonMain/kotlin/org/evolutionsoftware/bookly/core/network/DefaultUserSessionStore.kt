package org.evolutionsoftware.bookly.core.network

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DefaultUserSessionStore : UserSessionStore {
    private val mutex = Mutex()
    private var session: UserSession? = null

    override suspend fun read(): UserSession? =
        mutex.withLock { session }

    override suspend fun write(session: UserSession) {
        mutex.withLock { this.session = session }
    }

    override suspend fun clear() {
        mutex.withLock { session = null }
    }
}
