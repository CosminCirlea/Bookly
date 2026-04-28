package org.evolutionsoftware.bookly.core.mvi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.evolutionsoftware.bookly.core.logging.Logger
import org.evolutionsoftware.bookly.core.mvi.plugin.MviPlugin

@Suppress("LongParameterList")
class MviContainer<UiState : ViewState, Effect : SideEffect, Intent : UserIntent, Action : UserIntentAction>(
    private val coroutineScope: CoroutineScope,
    private val intentProcessor: IntentProcessor<Intent, Action>,
    private val stateMapper: StateMapper<Action, UiState>,
    private val effectProducer: EffectProducer<Action, UiState, Effect>,
    private val processDispatcher: CoroutineDispatcher = Dispatchers.Default,
    initialState: UiState,
    private val plugins: List<MviPlugin<Action>> = emptyList(),
    logTag: String = "MviContainer",
) {
    private val logger = Logger.withTag(logTag)

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<UiState> = _state

    private val _sideEffect = Channel<Effect>(Channel.BUFFERED)
    val sideEffect: Flow<Effect> = _sideEffect.receiveAsFlow()

    fun onUserIntent(intent: Intent) {
        coroutineScope.launch(processDispatcher) {
            logger.d("Processing intent: $intent")
            intentProcessor(intent)
                .onEach { action -> plugins.forEach { it.onUserIntentAction(action) } }
                .map { action ->
                    val current = _state.value
                    val nextState = stateMapper(action, current)
                    val effect = effectProducer(action, current)
                    nextState to effect
                }
                .onEach { (nextState, effect) ->
                    withContext(Dispatchers.Main) {
                        _state.value = nextState
                        effect?.let { _sideEffect.send(it) }
                    }
                }
                .catch { throwable -> logger.e("MVI pipeline error", throwable) }
                .launchIn(this)
        }
    }
}
