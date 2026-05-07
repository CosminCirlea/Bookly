package org.evolutionsoftware.bookly.services.profiles.domain.usecase

import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.core.usecase.utils.withExceptionHandling
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository

class CreateProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(
        name: String,
        dateOfBirth: String,
        gender: Boolean,
    ): Result<ParentProfile, CreateProfileError> =
        withExceptionHandling(
            errorMapper = { exception ->
                CreateProfileError.Unknown(exception.message ?: "Unknown error")
            },
        ) {
            repository.createProfile(name, dateOfBirth, gender)
        }
}

sealed interface CreateProfileError {
    data object ValidationError : CreateProfileError
    data object NetworkError : CreateProfileError
    data class Unknown(val message: String) : CreateProfileError
}
