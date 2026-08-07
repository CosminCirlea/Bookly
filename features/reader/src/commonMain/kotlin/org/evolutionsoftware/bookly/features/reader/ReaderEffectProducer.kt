package org.evolutionsoftware.bookly.features.reader

import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class ReaderEffectProducer : EffectProducer<ReaderAction, ReaderViewState, ReaderSideEffect> {
    override fun invoke(
        action: ReaderAction,
        currentState: ReaderViewState,
    ): ReaderSideEffect? =
        when (action) {
            ReaderAction.MissingBook -> ReaderSideEffect.MissingBook
            is ReaderAction.FavoriteUpdated -> ReaderSideEffect.FavoriteToggled(added = action.isFavorite)
            is ReaderAction.FavoriteUpdateReverted -> ReaderSideEffect.FavoriteUpdateFailed
            else -> null
        }
}
