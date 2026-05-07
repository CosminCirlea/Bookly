package org.evolutionsoftware.bookly.features.settings

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.jetbrains.compose.resources.StringResource

sealed interface SettingsAuthDestination {
    data object SignIn : SettingsAuthDestination

    data object SignUp : SettingsAuthDestination

    data object ChangePassword : SettingsAuthDestination

    data object ResetPassword : SettingsAuthDestination
}

internal data class SettingsViewState(
    val isLoading: Boolean = true,
    val isSessionActive: Boolean = false,
    val profile: ParentProfile? = null,
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
) : ViewState {
    val isAuthenticated: Boolean
        get() = profile != null || isSessionActive
}

internal sealed interface SettingsSideEffect : SideEffect {
    data class RequireAuthentication(val destination: SettingsAuthDestination) : SettingsSideEffect

    data object SignedOut : SettingsSideEffect

    data class ShowMessage(val message: StringResource) : SettingsSideEffect
}

internal sealed interface SettingsIntent : UserIntent {
    data object Load : SettingsIntent

    data object JoinClicked : SettingsIntent

    data object LoginClicked : SettingsIntent

    data object FacebookContinueClicked : SettingsIntent

    data object SignOutClicked : SettingsIntent

    data class NotificationsToggled(val enabled: Boolean) : SettingsIntent

    data class SoundToggled(val enabled: Boolean) : SettingsIntent

    data object EditProfileClicked : SettingsIntent

    data object ChangePasswordClicked : SettingsIntent

    data object ResetPasswordClicked : SettingsIntent

    data object HelpCenterClicked : SettingsIntent

    data object ContactUsClicked : SettingsIntent

    data object InviteFriendClicked : SettingsIntent

    data object RateAppClicked : SettingsIntent

    data object LanguageClicked : SettingsIntent
}

internal sealed interface SettingsAction : UserIntentAction {
    data class SessionChecked(val active: Boolean) : SettingsAction

    data object LoadingStarted : SettingsAction

    data class ProfileLoaded(val profile: ParentProfile?) : SettingsAction

    data class AuthenticationRequested(val destination: SettingsAuthDestination) : SettingsAction

    data object SignedOut : SettingsAction

    data class NotificationsUpdated(val enabled: Boolean) : SettingsAction

    data class SoundUpdated(val enabled: Boolean) : SettingsAction

    data class MessageRequested(val message: StringResource) : SettingsAction
}
