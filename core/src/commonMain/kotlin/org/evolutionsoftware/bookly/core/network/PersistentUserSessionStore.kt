package org.evolutionsoftware.bookly.core.network

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PersistentUserSessionStore : UserSessionStore {
    private val mutex = Mutex()
    private val storage = PersistentKeyValueStorage()
    private var initialized = false
    private var cache: UserSession? = null

    override suspend fun read(): UserSession? =
        mutex.withLock {
            if (!initialized) {
                cache =
                    storage.getString(KEY_SESSION)?.let {
                        runCatching { Json.decodeFromString<UserSession>(it) }.getOrNull()
                    }
                initialized = true
            }
            cache
        }

    override suspend fun write(session: UserSession) {
        mutex.withLock {
            cache = session
            initialized = true
            storage.setString(KEY_SESSION, Json.encodeToString(session))
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            cache = null
            initialized = true
            storage.remove(KEY_SESSION)
        }
    }

    private companion object {
        const val KEY_SESSION = "user_session"
    }
}
