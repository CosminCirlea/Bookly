package org.evolutionsoftware.bookly.features.auth.createprofile

import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class CreateProfileEffectProducer :
    EffectProducer<CreateProfileAction, CreateProfileViewState, CreateProfileSideEffect> {
    override fun invoke(
        action: CreateProfileAction,
        currentState: CreateProfileViewState,
    ): CreateProfileSideEffect? =
        when (action) {
            CreateProfileAction.SubmissionSucceeded -> CreateProfileSideEffect.ProfileCreated
            CreateProfileAction.Skipped -> CreateProfileSideEffect.Skipped
            is CreateProfileAction.SubmissionFailed ->
                CreateProfileSideEffect.ShowError("An error occurred, please try again")
            else -> null
        }
}
