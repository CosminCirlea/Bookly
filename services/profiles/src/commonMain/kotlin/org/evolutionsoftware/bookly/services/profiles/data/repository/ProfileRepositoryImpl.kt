package org.evolutionsoftware.bookly.services.profiles.data.repository

import org.evolutionsoftware.bookly.services.profiles.data.error.withExceptionWrapping
import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSession
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.evolutionsoftware.bookly.services.profiles.data.api.ProfilesAPI
import org.evolutionsoftware.bookly.services.profiles.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ProfilesAPI,
    private val authTokenStore: AuthTokenStore,
    private val userSessionStore: UserSessionStore,
) : ProfileRepository {

    override suspend fun getCurrentProfile(): ParentProfile? {
        val session = userSessionStore.read() ?: return null
        return runCatching { fetchProfile(session.userId) }
            .getOrNull()
            ?: session.toDomain()
    }

    override suspend fun login(displayName: String): ParentProfile {
        val session = userSessionStore.read()
        if (session != null) {
            runCatching { fetchProfile(session.userId) }
                .getOrNull()
                ?.let { return it }
        }
        return ParentProfile(id = displayName.lowercase(), displayName = displayName)
    }

    override suspend fun register(displayName: String): ParentProfile =
        ParentProfile(id = displayName.lowercase(), displayName = displayName)

    override suspend fun logout() {
        authTokenStore.clear()
        userSessionStore.clear()
    }

    override suspend fun createProfile(
        name: String,
        dateOfBirth: String,
        gender: Boolean,
    ): ParentProfile = withExceptionWrapping {
        api.createProfile(name, dateOfBirth, gender).toDomain()
    }

    private suspend fun fetchProfile(userId: String): ParentProfile? = withExceptionWrapping {
        api.getProfilesByUserId(userId).firstOrNull()?.toDomain()
    }

    private fun UserSession.toDomain(): ParentProfile =
        ParentProfile(id = userId, displayName = displayName)
}
