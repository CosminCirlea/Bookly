package org.evolutionsoftware.bookly.core.auth

import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSessionStore

class ClearSessionUseCase(
    private val authTokenStore: AuthTokenStore,
    private val userSessionStore: UserSessionStore,
) {
    suspend operator fun invoke() {
        authTokenStore.clear()
        userSessionStore.clear()
    }
}
