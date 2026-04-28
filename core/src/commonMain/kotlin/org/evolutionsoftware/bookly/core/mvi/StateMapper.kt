package org.evolutionsoftware.bookly.core.mvi

fun interface StateMapper<Action : UserIntentAction, State : ViewState> {
    operator fun invoke(
        action: Action,
        currentState: State,
    ): State
}
