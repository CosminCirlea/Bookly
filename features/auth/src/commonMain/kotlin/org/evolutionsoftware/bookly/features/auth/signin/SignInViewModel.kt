package org.evolutionsoftware.bookly.features.auth.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolutionsoftware.bookly.core.mvi.MviContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class SignInViewModel(
    coroutineScope: CoroutineScope,
    intentProcessor: SignInIntentProcessor,
    stateMapper: SignInStateMapper,
    effectProducer: SignInEffectProducer,
) {
    private val mviContainer =
        MviContainer(
            coroutineScope = coroutineScope,
            intentProcessor = intentProcessor,
            stateMapper = stateMapper,
            effectProducer = effectProducer,
            initialState = SignInViewState(),
            logTag = "SignInViewModel",
        )

    val sideEffect: Flow<SignInSideEffect> = mviContainer.sideEffect
    val viewState: StateFlow<SignInViewState> = mviContainer.state

    fun onUserIntent(intent: SignInIntent) {
        mviContainer.onUserIntent(intent)
    }
}

private object SignInKoin : KoinComponent

@Composable
internal fun rememberSignInViewModel(): SignInViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        SignInViewModel(
            coroutineScope = scope,
            intentProcessor = SignInKoin.get(),
            stateMapper = SignInKoin.get(),
            effectProducer = SignInKoin.get(),
        )
    }
}
