package org.evolutionsoftware.bookly.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolutionsoftware.bookly.core.mvi.MviContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class SettingsViewModel(
    coroutineScope: CoroutineScope,
    intentProcessor: SettingsIntentProcessor,
    stateMapper: SettingsStateMapper,
    effectProducer: SettingsEffectProducer,
) {
    private val mviContainer =
        MviContainer(
            coroutineScope = coroutineScope,
            intentProcessor = intentProcessor,
            stateMapper = stateMapper,
            effectProducer = effectProducer,
            initialState = SettingsViewState(),
            logTag = "SettingsViewModel",
        )

    val sideEffect: Flow<SettingsSideEffect> = mviContainer.sideEffect
    val viewState: StateFlow<SettingsViewState> = mviContainer.state

    fun onUserIntent(intent: SettingsIntent) {
        mviContainer.onUserIntent(intent)
    }
}

private object SettingsKoin : KoinComponent

@Composable
internal fun rememberSettingsViewModel(): SettingsViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        SettingsViewModel(
            coroutineScope = scope,
            intentProcessor = SettingsKoin.get(),
            stateMapper = SettingsKoin.get(),
            effectProducer = SettingsKoin.get(),
        )
    }
}
