package org.evolutionsoftware.bookly.features.settings

import org.evolutionsoftware.bookly.core.mvi.StateMapper

internal class SettingsStateMapper : StateMapper<SettingsAction, SettingsViewState> {
    override fun invoke(
        action: SettingsAction,
        currentState: SettingsViewState,
    ): SettingsViewState =
        when (action) {
            SettingsAction.LoadingStarted -> currentState.copy(isLoading = true)
            is SettingsAction.ProfileLoaded ->
                currentState.copy(
                    isLoading = false,
                    profile = action.profile,
                )
            is SettingsAction.NotificationsUpdated ->
                currentState.copy(notificationsEnabled = action.enabled)
            is SettingsAction.SoundUpdated ->
                currentState.copy(soundEnabled = action.enabled)
            SettingsAction.AuthenticationRequested -> currentState
            SettingsAction.SignedOut ->
                currentState.copy(
                    isLoading = false,
                    profile = null,
                )
            is SettingsAction.MessageRequested -> currentState
        }
}
