package org.evolutionsoftware.bookly.features.settings

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile

internal data class SettingsViewState(
    val isLoading: Boolean = true,
    val profile: ParentProfile? = null,
) : ViewState {
    val isAuthenticated: Boolean
        get() = profile != null

    val menuEntries: List<String>
        get() =
            if (isAuthenticated) {
                listOf("Offline library", "Download preferences", "Parent tips")
            } else {
                emptyList()
            }
}

internal sealed interface SettingsSideEffect : SideEffect {
    data object RequireAuthentication : SettingsSideEffect

    data object SignedOut : SettingsSideEffect
}

internal sealed interface SettingsIntent : UserIntent {
    data object Load : SettingsIntent

    data object AuthenticateClicked : SettingsIntent

    data object SignOutClicked : SettingsIntent
}

internal sealed interface SettingsAction : UserIntentAction {
    data object LoadingStarted : SettingsAction

    data class ProfileLoaded(val profile: ParentProfile?) : SettingsAction

    data object AuthenticationRequested : SettingsAction

    data object SignedOut : SettingsAction
}
