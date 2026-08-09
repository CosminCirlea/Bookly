package org.evolutionsoftware.bookly.features.settings

import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class SettingsEffectProducer :
    EffectProducer<SettingsAction, SettingsViewState, SettingsSideEffect> {
    override fun invoke(
        action: SettingsAction,
        currentState: SettingsViewState,
    ): SettingsSideEffect? =
        when (action) {
            is SettingsAction.AuthenticationRequested -> SettingsSideEffect.RequireAuthentication(action.destination)
            SettingsAction.SignedOut -> SettingsSideEffect.SignedOut
            is SettingsAction.MessageRequested ->
                SettingsSideEffect.ShowMessage(
                    message = action.message,
                    args = action.args,
                    isSuccess = action.isSuccess,
                )
            SettingsAction.NotificationsOpened -> SettingsSideEffect.OpenNotifications
            SettingsAction.ContactUsOpened -> SettingsSideEffect.OpenContactUs
            SettingsAction.EditProfileOpened -> SettingsSideEffect.OpenEditProfile
            else -> null
        }
}
