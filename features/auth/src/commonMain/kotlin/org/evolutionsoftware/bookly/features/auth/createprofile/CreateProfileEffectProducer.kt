package org.evolutionsoftware.bookly.features.auth.createprofile

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_error_general
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
                CreateProfileSideEffect.ShowError(Res.string.auth_error_general)
            else -> null
        }
}
