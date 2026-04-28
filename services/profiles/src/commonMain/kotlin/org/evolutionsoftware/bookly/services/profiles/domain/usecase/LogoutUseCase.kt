package org.evolutionsoftware.bookly.services.profiles.domain.usecase

import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository

class LogoutUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
