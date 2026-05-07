package org.evolutionsoftware.bookly.features.auth.createprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolutionsoftware.bookly.core.mvi.MviContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class CreateProfileViewModel(
    coroutineScope: CoroutineScope,
    intentProcessor: CreateProfileIntentProcessor,
    stateMapper: CreateProfileStateMapper,
    effectProducer: CreateProfileEffectProducer,
) {
    private val mviContainer =
        MviContainer(
            coroutineScope = coroutineScope,
            intentProcessor = intentProcessor,
            stateMapper = stateMapper,
            effectProducer = effectProducer,
            initialState = CreateProfileViewState(),
            logTag = "CreateProfileViewModel",
        )

    val sideEffect: Flow<CreateProfileSideEffect> = mviContainer.sideEffect
    val viewState: StateFlow<CreateProfileViewState> = mviContainer.state

    fun onUserIntent(intent: CreateProfileIntent) {
        mviContainer.onUserIntent(intent)
    }
}

private object CreateProfileKoin : KoinComponent

@Composable
internal fun rememberCreateProfileViewModel(): CreateProfileViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        CreateProfileViewModel(
            coroutineScope = scope,
            intentProcessor = CreateProfileKoin.get(),
            stateMapper = CreateProfileKoin.get(),
            effectProducer = CreateProfileKoin.get(),
        )
    }
}
