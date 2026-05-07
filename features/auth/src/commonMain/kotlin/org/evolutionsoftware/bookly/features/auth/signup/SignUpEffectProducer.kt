package org.evolutionsoftware.bookly.features.auth.signup

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_error_general
import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class SignUpEffectProducer : EffectProducer<SignUpAction, SignUpViewState, SignUpSideEffect> {
    override fun invoke(
        action: SignUpAction,
        currentState: SignUpViewState,
    ): SignUpSideEffect? =
        when (action) {
            SignUpAction.SignInNavigationRequested -> SignUpSideEffect.NavigateToSignIn
            SignUpAction.SubmissionSucceeded -> SignUpSideEffect.ReadyForProfileCreation
            SignUpAction.SubmissionFailed -> SignUpSideEffect.ShowMessage(Res.string.auth_error_general)
            is SignUpAction.ValidationFailed -> SignUpSideEffect.ShowMessage(action.message)
            else -> null
        }
}
