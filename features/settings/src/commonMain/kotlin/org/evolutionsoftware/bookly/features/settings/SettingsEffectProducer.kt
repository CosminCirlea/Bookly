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
            is SettingsAction.MessageRequested -> SettingsSideEffect.ShowMessage(action.message)
            else -> null
        }
}
