package org.evolutionsoftware.bookly.core.auth

import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSessionStore

class CheckSessionUseCase(
    private val authTokenStore: AuthTokenStore,
    private val userSessionStore: UserSessionStore,
) {
    suspend operator fun invoke(): SessionState =
        if (authTokenStore.read() != null && userSessionStore.read() != null) {
            SessionState.SignedIn
        } else {
            SessionState.SignedOut
        }
}
