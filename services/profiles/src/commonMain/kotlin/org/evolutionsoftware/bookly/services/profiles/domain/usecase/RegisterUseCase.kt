package org.evolutionsoftware.bookly.services.profiles.domain.usecase

import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository

class RegisterUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(displayName: String): ParentProfile = repository.register(displayName)
}
