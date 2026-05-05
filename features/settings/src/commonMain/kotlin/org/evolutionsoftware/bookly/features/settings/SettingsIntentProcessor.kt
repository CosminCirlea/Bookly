package org.evolutionsoftware.bookly.features.settings

import bookly.features.settings.generated.resources.Res
import bookly.features.settings.generated.resources.settings_contact_us_message
import bookly.features.settings.generated.resources.settings_help_center_message
import bookly.features.settings.generated.resources.settings_invite_friend_message
import bookly.features.settings.generated.resources.settings_language_message
import bookly.features.settings.generated.resources.settings_profile_edit_message
import bookly.features.settings.generated.resources.settings_rate_app_message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.GetCurrentProfileUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.LogoutUseCase

internal class SettingsIntentProcessor(
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
) : IntentProcessor<SettingsIntent, SettingsAction> {
    override fun invoke(intent: SettingsIntent): Flow<SettingsAction> =
        when (intent) {
            SettingsIntent.Load ->
                flow {
                    emit(SettingsAction.LoadingStarted)
                    emit(SettingsAction.ProfileLoaded(getCurrentProfileUseCase()))
                }
            SettingsIntent.JoinClicked -> flowOf(SettingsAction.AuthenticationRequested(SettingsAuthDestination.SignUp))
            SettingsIntent.LoginClicked -> flowOf(SettingsAction.AuthenticationRequested(SettingsAuthDestination.SignIn))
            SettingsIntent.ChangePasswordClicked -> flowOf(SettingsAction.AuthenticationRequested(SettingsAuthDestination.ChangePassword))
            SettingsIntent.ResetPasswordClicked -> flowOf(SettingsAction.AuthenticationRequested(SettingsAuthDestination.ResetPassword))
            SettingsIntent.EditProfileClicked -> flowOf(SettingsAction.MessageRequested(Res.string.settings_profile_edit_message))
            SettingsIntent.HelpCenterClicked -> flowOf(SettingsAction.MessageRequested(Res.string.settings_help_center_message))
            SettingsIntent.ContactUsClicked -> flowOf(SettingsAction.MessageRequested(Res.string.settings_contact_us_message))
            SettingsIntent.InviteFriendClicked -> flowOf(SettingsAction.MessageRequested(Res.string.settings_invite_friend_message))
            SettingsIntent.RateAppClicked -> flowOf(SettingsAction.MessageRequested(Res.string.settings_rate_app_message))
            SettingsIntent.LanguageClicked -> flowOf(SettingsAction.MessageRequested(Res.string.settings_language_message))
            is SettingsIntent.NotificationsToggled -> flowOf(SettingsAction.NotificationsUpdated(intent.enabled))
            is SettingsIntent.SoundToggled -> flowOf(SettingsAction.SoundUpdated(intent.enabled))
            SettingsIntent.SignOutClicked ->
                flow {
                    logoutUseCase()
                    emit(SettingsAction.SignedOut)
                    emit(SettingsAction.ProfileLoaded(null))
                }
        }
}
