package org.evolutionsoftware.bookly.features.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolutionsoftware.bookly.core.mvi.MviContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class SignUpViewModel(
    coroutineScope: CoroutineScope,
    intentProcessor: SignUpIntentProcessor,
    stateMapper: SignUpStateMapper,
    effectProducer: SignUpEffectProducer,
) {
    private val mviContainer =
        MviContainer(
            coroutineScope = coroutineScope,
            intentProcessor = intentProcessor,
            stateMapper = stateMapper,
            effectProducer = effectProducer,
            initialState = SignUpViewState(),
            logTag = "SignUpViewModel",
        )

    val sideEffect: Flow<SignUpSideEffect> = mviContainer.sideEffect
    val viewState: StateFlow<SignUpViewState> = mviContainer.state

    fun onUserIntent(intent: SignUpIntent) {
        mviContainer.onUserIntent(intent)
    }
}

private object SignUpKoin : KoinComponent

@Composable
internal fun rememberSignUpViewModel(): SignUpViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        SignUpViewModel(
            coroutineScope = scope,
            intentProcessor = SignUpKoin.get(),
            stateMapper = SignUpKoin.get(),
            effectProducer = SignUpKoin.get(),
        )
    }
}
