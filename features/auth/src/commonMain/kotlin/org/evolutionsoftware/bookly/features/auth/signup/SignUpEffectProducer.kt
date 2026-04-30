package org.evolutionsoftware.bookly.features.auth.signup

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_sign_up_success_message
import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class SignUpEffectProducer : EffectProducer<SignUpAction, SignUpViewState, SignUpSideEffect> {
    override fun invoke(
        action: SignUpAction,
        currentState: SignUpViewState,
    ): SignUpSideEffect? =
        when (action) {
            SignUpAction.SignInNavigationRequested -> SignUpSideEffect.NavigateToSignIn
            SignUpAction.SubmissionSucceeded -> SignUpSideEffect.Authenticated(Res.string.auth_sign_up_success_message)
            is SignUpAction.ValidationFailed -> SignUpSideEffect.ShowMessage(action.message)
            else -> null
        }
}
