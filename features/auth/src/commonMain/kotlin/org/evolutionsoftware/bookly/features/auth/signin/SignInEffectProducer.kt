package org.evolutionsoftware.bookly.features.auth.signin

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_sign_in_success_message
import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class SignInEffectProducer : EffectProducer<SignInAction, SignInViewState, SignInSideEffect> {
    override fun invoke(
        action: SignInAction,
        currentState: SignInViewState,
    ): SignInSideEffect? =
        when (action) {
            SignInAction.ForgotPasswordNavigationRequested -> SignInSideEffect.NavigateToForgotPassword
            SignInAction.SignUpNavigationRequested -> SignInSideEffect.NavigateToSignUp
            SignInAction.SubmissionSucceeded -> SignInSideEffect.Authenticated(Res.string.auth_sign_in_success_message)
            is SignInAction.ValidationFailed -> SignInSideEffect.ShowMessage(action.message)
            else -> null
        }
}
