package org.evolutionsoftware.bookly.features.settings

import bookly.features.settings.generated.resources.Res
import bookly.features.settings.generated.resources.settings_help_center_message
import bookly.features.settings.generated.resources.settings_invite_copied
import bookly.features.settings.generated.resources.settings_language_changed
import bookly.features.settings.generated.resources.settings_rate_thanks
import bookly.features.settings.generated.resources.settings_sound_message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.auth.CheckSessionUseCase
import org.evolutionsoftware.bookly.core.auth.SessionState
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.GetCurrentProfileUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.LogoutUseCase

internal class SettingsIntentProcessor(
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val checkSessionUseCase: CheckSessionUseCase,
) : IntentProcessor<SettingsIntent, SettingsAction> {
    override fun invoke(intent: SettingsIntent): Flow<SettingsAction> =
        when (intent) {
            SettingsIntent.Load ->
                flow {
                    val isSignedIn = checkSessionUseCase() == SessionState.SignedIn
                    emit(SettingsAction.SessionChecked(isSignedIn))
                    if (isSignedIn) {
                        emit(SettingsAction.LoadingStarted)
                        emit(SettingsAction.ProfileLoaded(getCurrentProfileUseCase()))
                    } else {
                        emit(SettingsAction.ProfileLoaded(null))
                    }
                }
            SettingsIntent.JoinClicked -> flowOf(SettingsAction.AuthenticationRequested(SettingsAuthDestination.SignUp))
            SettingsIntent.LoginClicked -> flowOf(SettingsAction.AuthenticationRequested(SettingsAuthDestination.SignIn))
            SettingsIntent.FacebookContinueClicked -> flowOf(SettingsAction.OnboardingRequested)
            SettingsIntent.ChangePasswordClicked -> flowOf(SettingsAction.AuthenticationRequested(SettingsAuthDestination.ChangePassword))
            SettingsIntent.ResetPasswordClicked -> flowOf(SettingsAction.AuthenticationRequested(SettingsAuthDestination.ResetPassword))
            SettingsIntent.EditProfileClicked -> flowOf(SettingsAction.EditProfileOpened)
            SettingsIntent.NotificationsClicked -> flowOf(SettingsAction.NotificationsOpened)
            SettingsIntent.SoundClicked -> flowOf(SettingsAction.MessageRequested(Res.string.settings_sound_message))
            SettingsIntent.HelpCenterClicked -> flowOf(SettingsAction.MessageRequested(Res.string.settings_help_center_message))
            SettingsIntent.ContactUsClicked -> flowOf(SettingsAction.ContactUsOpened)
            is SettingsIntent.RateSubmitted ->
                flowOf(
                    SettingsAction.MessageRequested(
                        message = Res.string.settings_rate_thanks,
                        args = listOf(intent.stars.toString()),
                        isSuccess = true,
                    ),
                )
            SettingsIntent.InviteLinkCopied ->
                flowOf(
                    SettingsAction.MessageRequested(
                        message = Res.string.settings_invite_copied,
                        isSuccess = true,
                    ),
                )
            is SettingsIntent.LanguageSelected ->
                flow {
                    emit(SettingsAction.LanguageUpdated(intent.language))
                    emit(
                        SettingsAction.MessageRequested(
                            message = Res.string.settings_language_changed,
                            args = listOf(intent.language),
                            isSuccess = true,
                        ),
                    )
                }
            SettingsIntent.SignOutClicked ->
                flow {
                    logoutUseCase()
                    emit(SettingsAction.SignedOut)
                    emit(SettingsAction.ProfileLoaded(null))
                }
        }
}
