package org.evolutionsoftware.bookly.features.home

import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class HomeEffectProducer : EffectProducer<HomeAction, HomeViewState, HomeSideEffect> {
    override fun invoke(
        action: HomeAction,
        currentState: HomeViewState,
    ): HomeSideEffect? = null
}
