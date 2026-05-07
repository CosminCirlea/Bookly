package org.evolutionsoftware.bookly.services.profiles.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSession
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.evolutionsoftware.bookly.services.profiles.data.dto.ProfileDto
import org.evolutionsoftware.bookly.services.profiles.data.mapper.toDomain
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val httpClient: HttpClient,
    private val authTokenStore: AuthTokenStore,
    private val userSessionStore: UserSessionStore,
) : ProfileRepository {
    override suspend fun getCurrentProfile(): ParentProfile? {
        val session = userSessionStore.read() ?: return null

        val response = httpClient.get("$PROFILES_USERS_PATH/${session.userId}")

        if (!response.status.isSuccess()) {
            logger.d("Get profiles failed with status ${response.status.value}")
            return session.toDomain()
        }

        val profiles = response.body<List<ProfileDto>>()
        return profiles.firstOrNull()?.toDomain() ?: session.toDomain()
    }

    override suspend fun login(displayName: String): ParentProfile {
        val session = userSessionStore.read()
        if (session != null) {
            val response = httpClient.get("$PROFILES_USERS_PATH/${session.userId}")
            if (response.status.isSuccess()) {
                val profiles = response.body<List<ProfileDto>>()
                profiles.firstOrNull()?.let { return it.toDomain() }
            }
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
    ): ParentProfile {
        val response =
            httpClient.post(PROFILES_PATH) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("name", name)
                            append("date_of_birth", dateOfBirth)
                            append("gender", gender.toString())
                        },
                    ),
                )
            }

        if (!response.status.isSuccess()) {
            logger.d("Create profile failed with status ${response.status.value}")
            throw Exception("Create profile failed: ${response.status.value}")
        }

        return response.body<ProfileDto>().toDomain()
    }

    private fun UserSession.toDomain(): ParentProfile =
        ParentProfile(id = userId, displayName = displayName)

    private companion object {
        private const val PROFILES_USERS_PATH = "api/profiles/users"
        private const val PROFILES_PATH = "api/profiles"
        private val logger = Logger.withTag("ProfileRepository")
    }
}
