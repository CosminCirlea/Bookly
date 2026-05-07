package org.evolutionsoftware.bookly.services.profiles.domain.repository

import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile

interface ProfileRepository {
    suspend fun getCurrentProfile(): ParentProfile?

    suspend fun login(displayName: String): ParentProfile

    suspend fun register(displayName: String): ParentProfile

    suspend fun logout()

    suspend fun createProfile(
        name: String,
        dateOfBirth: String,
        gender: Boolean,
    ): ParentProfile
}
