package org.evolutionsoftware.bookly.features.settings

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
            SettingsIntent.AuthenticateClicked -> flowOf(SettingsAction.AuthenticationRequested)
            SettingsIntent.SignOutClicked ->
                flow {
                    logoutUseCase()
                    emit(SettingsAction.SignedOut)
                    emit(SettingsAction.ProfileLoaded(null))
                }
        }
}
