package org.evolutionsoftware.bookly.services.profiles.data.repository

import org.evolutionsoftware.bookly.core.network.AuthToken
import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSession
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val authTokenStore: AuthTokenStore,
    private val userSessionStore: UserSessionStore,
) : ProfileRepository {
    override suspend fun getCurrentProfile(): ParentProfile? =
        userSessionStore.read()?.toDomain()

    override suspend fun login(displayName: String): ParentProfile = upsertSession(displayName)

    override suspend fun register(displayName: String): ParentProfile = upsertSession(displayName)

    override suspend fun logout() {
        authTokenStore.clear()
        userSessionStore.clear()
    }

    private suspend fun upsertSession(displayName: String): ParentProfile {
        val normalizedName = displayName.trim().ifBlank { "Guest Reader" }
        val userId = normalizedName.lowercase().replace(" ", "-")
        authTokenStore.write(
            AuthToken(
                accessToken = "bookly-$userId-token",
                refreshToken = "refresh-$userId",
            ),
        )
        userSessionStore.write(
            UserSession(
                userId = userId,
                displayName = normalizedName,
            ),
        )
        return ParentProfile(
            id = userId,
            displayName = normalizedName,
        )
    }
}

private fun UserSession.toDomain(): ParentProfile =
    ParentProfile(
        id = userId,
        displayName = displayName,
    )
