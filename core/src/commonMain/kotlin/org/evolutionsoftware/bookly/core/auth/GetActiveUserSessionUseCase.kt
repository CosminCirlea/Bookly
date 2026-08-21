package org.evolutionsoftware.bookly.core.auth

import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSession
import org.evolutionsoftware.bookly.core.network.UserSessionStore

class GetActiveUserSessionUseCase(
    private val authTokenStore: AuthTokenStore,
    private val userSessionStore: UserSessionStore,
) {
    suspend operator fun invoke(): UserSession? {
        if (authTokenStore.read() == null) return null
        return userSessionStore.read()
    }
}
