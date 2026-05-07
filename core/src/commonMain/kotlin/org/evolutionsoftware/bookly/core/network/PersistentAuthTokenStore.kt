package org.evolutionsoftware.bookly.core.network

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PersistentAuthTokenStore : AuthTokenStore {
    private val mutex = Mutex()
    private val storage = PersistentKeyValueStorage()
    private var initialized = false
    private var cache: AuthToken? = null

    override suspend fun read(): AuthToken? =
        mutex.withLock {
            if (!initialized) {
                cache =
                    storage.getString(KEY_TOKEN)?.let {
                        runCatching { Json.decodeFromString<AuthToken>(it) }.getOrNull()
                    }
                initialized = true
            }
            cache
        }

    override suspend fun write(token: AuthToken) {
        mutex.withLock {
            cache = token
            initialized = true
            storage.setString(KEY_TOKEN, Json.encodeToString(token))
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            cache = null
            initialized = true
            storage.remove(KEY_TOKEN)
        }
    }

    private companion object {
        const val KEY_TOKEN = "auth_token"
    }
}
