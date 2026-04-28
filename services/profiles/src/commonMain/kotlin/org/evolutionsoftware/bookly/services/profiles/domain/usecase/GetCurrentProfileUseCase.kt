package org.evolutionsoftware.bookly.services.profiles.domain.usecase

import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository

class GetCurrentProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): ParentProfile? = repository.getCurrentProfile()
}
