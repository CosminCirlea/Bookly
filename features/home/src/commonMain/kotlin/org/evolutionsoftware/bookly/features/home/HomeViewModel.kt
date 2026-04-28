package org.evolutionsoftware.bookly.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolutionsoftware.bookly.core.mvi.MviContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class HomeViewModel(
    coroutineScope: CoroutineScope,
    intentProcessor: HomeIntentProcessor,
    stateMapper: HomeStateMapper,
    effectProducer: HomeEffectProducer,
) {
    private val mviContainer =
        MviContainer(
            coroutineScope = coroutineScope,
            intentProcessor = intentProcessor,
            stateMapper = stateMapper,
            effectProducer = effectProducer,
            initialState = HomeViewState(),
            logTag = "HomeViewModel",
        )

    val sideEffect: Flow<HomeSideEffect> = mviContainer.sideEffect
    val viewState: StateFlow<HomeViewState> = mviContainer.state

    fun onUserIntent(intent: HomeIntent) {
        mviContainer.onUserIntent(intent)
    }
}

private object HomeKoin : KoinComponent

@Composable
internal fun rememberHomeViewModel(): HomeViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        HomeViewModel(
            coroutineScope = scope,
            intentProcessor = HomeKoin.get(),
            stateMapper = HomeKoin.get(),
            effectProducer = HomeKoin.get(),
        )
    }
}
