package org.evolutionsoftware.bookly.services.profiles.data.repository

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.evolutionsoftware.bookly.core.network.AuthToken
import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSession
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.evolutionsoftware.bookly.services.profiles.data.api.ProfilesRemoteDataSource
import org.evolutionsoftware.bookly.services.profiles.data.dto.ProfileDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfileRepositoryImplTest {
    @Test
    fun `missing backend profile does not use the user UUID as a profile ID`() =
        runTest {
            val repository = repository(profiles = emptyList())

            assertNull(repository.getCurrentProfile())
        }

    @Test
    fun `current profile uses the numeric backend profile ID required by favorites`() =
        runTest {
            val repository =
                repository(
                    profiles =
                        listOf(
                            ProfileDto(
                                id = 42,
                                userId = USER_ID,
                                name = "Junior",
                            ),
                        ),
                )

            assertEquals("42", repository.getCurrentProfile()?.id)
        }

    @Test
    fun `profiles list response can omit user ID as documented`() {
        val profile =
            Json.decodeFromString<ProfileDto>(
                """{"id":42,"name":"Junior","date_of_birth":null,"gender":true,"photo_url":null}""",
            )

        assertEquals(42, profile.id)
        assertNull(profile.userId)
    }

    private fun repository(profiles: List<ProfileDto>): ProfileRepositoryImpl =
        ProfileRepositoryImpl(
            api = FakeProfilesRemoteDataSource(profiles),
            authTokenStore = FakeAuthTokenStore,
            userSessionStore = FakeUserSessionStore,
        )

    private class FakeProfilesRemoteDataSource(
        private val profiles: List<ProfileDto>,
    ) : ProfilesRemoteDataSource {
        override suspend fun getProfilesByUserId(userId: String): List<ProfileDto> = profiles

        override suspend fun createProfile(
            name: String,
            dateOfBirth: String,
            gender: Boolean,
        ): ProfileDto = error("unused")
    }

    private object FakeAuthTokenStore : AuthTokenStore {
        override suspend fun read(): AuthToken? = null

        override suspend fun write(token: AuthToken) = Unit

        override suspend fun clear() = Unit
    }

    private object FakeUserSessionStore : UserSessionStore {
        override suspend fun read(): UserSession =
            UserSession(
                userId = USER_ID,
                displayName = "reader@example.com",
            )

        override suspend fun write(session: UserSession) = Unit

        override suspend fun clear() = Unit
    }

    private companion object {
        const val USER_ID = "4fb0f6c2-8d2f-4c7e-a6eb-c7642a9f4c51"
    }
}
