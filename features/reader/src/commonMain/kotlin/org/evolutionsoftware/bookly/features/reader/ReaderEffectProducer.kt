package org.evolutionsoftware.bookly.features.reader

import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class ReaderEffectProducer : EffectProducer<ReaderAction, ReaderViewState, ReaderSideEffect> {
    override fun invoke(
        action: ReaderAction,
        currentState: ReaderViewState,
    ): ReaderSideEffect? =
        when (action) {
            ReaderAction.MissingBook -> ReaderSideEffect.MissingBook
            else -> null
        }
}
