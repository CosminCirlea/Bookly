package org.evolutionsoftware.bookly.features.auth.resetpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolutionsoftware.bookly.core.mvi.MviContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class ResetPasswordViewModel(
    coroutineScope: CoroutineScope,
    intentProcessor: ResetPasswordIntentProcessor,
    stateMapper: ResetPasswordStateMapper,
    effectProducer: ResetPasswordEffectProducer,
) {
    private val mviContainer =
        MviContainer(
            coroutineScope = coroutineScope,
            intentProcessor = intentProcessor,
            stateMapper = stateMapper,
            effectProducer = effectProducer,
            initialState = ResetPasswordViewState(),
            logTag = "ResetPasswordViewModel",
        )

    val sideEffect: Flow<ResetPasswordSideEffect> = mviContainer.sideEffect
    val viewState: StateFlow<ResetPasswordViewState> = mviContainer.state

    fun onUserIntent(intent: ResetPasswordIntent) {
        mviContainer.onUserIntent(intent)
    }
}

private object ResetPasswordKoin : KoinComponent

@Composable
internal fun rememberResetPasswordViewModel(): ResetPasswordViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        ResetPasswordViewModel(
            coroutineScope = scope,
            intentProcessor = ResetPasswordKoin.get(),
            stateMapper = ResetPasswordKoin.get(),
            effectProducer = ResetPasswordKoin.get(),
        )
    }
}
