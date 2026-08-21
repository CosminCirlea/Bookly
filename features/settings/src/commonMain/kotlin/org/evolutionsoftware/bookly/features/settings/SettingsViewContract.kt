package org.evolutionsoftware.bookly.features.settings

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.jetbrains.compose.resources.StringResource

sealed interface SettingsAuthDestination {
    data object SignIn : SettingsAuthDestination

    data object ChangePassword : SettingsAuthDestination

    data object ResetPassword : SettingsAuthDestination
}

internal data class SettingsViewState(
    val isLoading: Boolean = true,
    val isSessionActive: Boolean = false,
    val accountDisplayName: String? = null,
    val profile: ParentProfile? = null,
) : ViewState {
    val isAuthenticated: Boolean
        get() = isSessionActive

    val displayName: String?
        get() = profile?.displayName ?: accountDisplayName
}

internal sealed interface SettingsSideEffect : SideEffect {
    data class RequireAuthentication(val destination: SettingsAuthDestination) : SettingsSideEffect

    data object SignedOut : SettingsSideEffect

    data class ShowMessage(
        val message: StringResource,
        val args: List<String> = emptyList(),
        val isSuccess: Boolean = false,
    ) : SettingsSideEffect

    data object OpenNotifications : SettingsSideEffect

    data object OpenContactUs : SettingsSideEffect

    data object OpenEditProfile : SettingsSideEffect
}

internal sealed interface SettingsIntent : UserIntent {
    data object Load : SettingsIntent

    /** The signed-out banner. Sign-up is reached from the sign-in screen itself. */
    data object LoginClicked : SettingsIntent

    data object SignOutClicked : SettingsIntent

    data object NotificationsClicked : SettingsIntent

    data object SoundClicked : SettingsIntent

    data object EditProfileClicked : SettingsIntent

    data object ChangePasswordClicked : SettingsIntent

    data object ResetPasswordClicked : SettingsIntent

    data object HelpCenterClicked : SettingsIntent

    data object ContactUsClicked : SettingsIntent

    data class RateSubmitted(val stars: Int) : SettingsIntent

    data object InviteLinkCopied : SettingsIntent

    data class LanguageSelected(val displayName: String) : SettingsIntent
}

internal sealed interface SettingsAction : UserIntentAction {
    data class SessionChecked(
        val active: Boolean,
        val displayName: String?,
    ) : SettingsAction

    data object LoadingStarted : SettingsAction

    data class ProfileLoaded(val profile: ParentProfile?) : SettingsAction

    data class AuthenticationRequested(val destination: SettingsAuthDestination) : SettingsAction

    data object SignedOut : SettingsAction

    data class MessageRequested(
        val message: StringResource,
        val args: List<String> = emptyList(),
        val isSuccess: Boolean = false,
    ) : SettingsAction

    data object NotificationsOpened : SettingsAction

    data object ContactUsOpened : SettingsAction

    data object EditProfileOpened : SettingsAction
}
