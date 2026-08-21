package org.evolutionsoftware.bookly.features.settings

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.evolutionsoftware.bookly.core.auth.GetActiveUserSessionUseCase
import org.evolutionsoftware.bookly.core.network.AuthToken
import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSession
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.GetCurrentProfileUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.LogoutUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SettingsIntentProcessorTest {
    @Test
    fun `load uses the active session and backend profile`() =
        runTest {
            val profile = ParentProfile(id = "42", displayName = "Junior")
            val actions = processor(profile).invoke(SettingsIntent.Load).toList()
            val state = actions.mapToState()

            assertEquals(
                SettingsAction.SessionChecked(
                    active = true,
                    displayName = ACTIVE_SESSION.displayName,
                ),
                actions.first(),
            )
            assertEquals(SettingsAction.ProfileLoaded(profile), actions.last())
            assertTrue(state.isAuthenticated)
            assertEquals(profile.displayName, state.displayName)
        }

    @Test
    fun `active account remains visible when the backend profile is missing`() =
        runTest {
            val actions = processor(profile = null).invoke(SettingsIntent.Load).toList()
            val state = actions.mapToState()

            assertTrue(state.isAuthenticated)
            assertEquals(ACTIVE_SESSION.displayName, state.displayName)
            assertNull(state.profile)
        }

    private fun processor(profile: ParentProfile?): SettingsIntentProcessor {
        val repository = StaticProfileRepository(profile)
        return SettingsIntentProcessor(
            getCurrentProfileUseCase = GetCurrentProfileUseCase(repository),
            logoutUseCase = LogoutUseCase(repository),
            getActiveUserSessionUseCase =
                GetActiveUserSessionUseCase(
                    authTokenStore = StaticAuthTokenStore,
                    userSessionStore = StaticUserSessionStore,
                ),
        )
    }

    private fun List<SettingsAction>.mapToState(): SettingsViewState =
        fold(SettingsViewState()) { state, action ->
            SettingsStateMapper()(action, state)
        }

    private class StaticProfileRepository(
        private val profile: ParentProfile?,
    ) : ProfileRepository {
        override suspend fun getCurrentProfile(): ParentProfile? = profile

        override suspend fun login(displayName: String): ParentProfile = error("unused")

        override suspend fun register(displayName: String): ParentProfile = error("unused")

        override suspend fun logout() = Unit

        override suspend fun createProfile(
            name: String,
            dateOfBirth: String,
            gender: Boolean,
        ): ParentProfile = error("unused")
    }

    private object StaticAuthTokenStore : AuthTokenStore {
        override suspend fun read(): AuthToken =
            AuthToken(
                accessToken = "access-token",
                refreshToken = "refresh-token",
            )

        override suspend fun write(token: AuthToken) = Unit

        override suspend fun clear() = Unit
    }

    private object StaticUserSessionStore : UserSessionStore {
        override suspend fun read(): UserSession = ACTIVE_SESSION

        override suspend fun write(session: UserSession) = Unit

        override suspend fun clear() = Unit
    }

    private companion object {
        val ACTIVE_SESSION =
            UserSession(
                userId = "user-id",
                displayName = "reader@example.com",
            )
    }
}
