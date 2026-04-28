package org.evolutionsoftware.bookly.features.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolutionsoftware.bookly.core.mvi.MviContainer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class ReaderViewModel(
    coroutineScope: CoroutineScope,
    intentProcessor: ReaderIntentProcessor,
    stateMapper: ReaderStateMapper,
    effectProducer: ReaderEffectProducer,
) {
    private val mviContainer =
        MviContainer(
            coroutineScope = coroutineScope,
            intentProcessor = intentProcessor,
            stateMapper = stateMapper,
            effectProducer = effectProducer,
            initialState = ReaderViewState(),
            logTag = "ReaderViewModel",
        )

    val sideEffect: Flow<ReaderSideEffect> = mviContainer.sideEffect
    val viewState: StateFlow<ReaderViewState> = mviContainer.state

    fun onUserIntent(intent: ReaderIntent) {
        mviContainer.onUserIntent(intent)
    }
}

private object ReaderKoin : KoinComponent

@Composable
internal fun rememberReaderViewModel(): ReaderViewModel {
    val scope = rememberCoroutineScope()
    return remember {
        ReaderViewModel(
            coroutineScope = scope,
            intentProcessor = ReaderKoin.get(),
            stateMapper = ReaderKoin.get(),
            effectProducer = ReaderKoin.get(),
        )
    }
}
