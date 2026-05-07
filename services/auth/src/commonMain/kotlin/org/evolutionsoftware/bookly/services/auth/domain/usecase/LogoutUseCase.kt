package org.evolutionsoftware.bookly.services.auth.domain.usecase

import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.evolutionsoftware.bookly.services.auth.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository,
    private val authTokenStore: AuthTokenStore,
    private val userSessionStore: UserSessionStore,
) {
    suspend operator fun invoke() {
        repository.logout()
        authTokenStore.clear()
        userSessionStore.clear()
    }
}
