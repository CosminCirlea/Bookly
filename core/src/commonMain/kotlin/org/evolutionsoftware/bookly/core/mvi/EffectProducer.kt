package org.evolutionsoftware.bookly.core.mvi

fun interface EffectProducer<Action : UserIntentAction, State : ViewState, Effect : SideEffect> {
    operator fun invoke(
        action: Action,
        currentState: State,
    ): Effect?
}
