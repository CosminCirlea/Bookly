package org.evolutionsoftware.bookly.features.auth.changepassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolutionsoftware.bookly.core.mvi.MviContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class ChangePasswordViewModel(
    coroutineScope: CoroutineScope,
    intentProcessor: ChangePasswordIntentProcessor,
    stateMapper: ChangePasswordStateMapper,
    effectProducer: ChangePasswordEffectProducer,
) {
    private val mviContainer =
        MviContainer(
            coroutineScope = coroutineScope,
            intentProcessor = intentProcessor,
            stateMapper = stateMapper,
            effectProducer = effectProducer,
            initialState = ChangePasswordViewState(),
            logTag = "ChangePasswordViewModel",
        )

    val sideEffect: Flow<ChangePasswordSideEffect> = mviContainer.sideEffect
    val viewState: StateFlow<ChangePasswordViewState> = mviContainer.state

    fun onUserIntent(intent: ChangePasswordIntent) {
        mviContainer.onUserIntent(intent)
    }
}

private object ChangePasswordKoin : KoinComponent

@Composable
internal fun rememberChangePasswordViewModel(): ChangePasswordViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        ChangePasswordViewModel(
            coroutineScope = scope,
            intentProcessor = ChangePasswordKoin.get(),
            stateMapper = ChangePasswordKoin.get(),
            effectProducer = ChangePasswordKoin.get(),
        )
    }
}
