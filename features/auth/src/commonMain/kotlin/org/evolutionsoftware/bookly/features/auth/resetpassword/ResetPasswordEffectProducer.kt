package org.evolutionsoftware.bookly.features.auth.resetpassword

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_reset_password_success_message
import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class ResetPasswordEffectProducer :
    EffectProducer<ResetPasswordAction, ResetPasswordViewState, ResetPasswordSideEffect> {
    override fun invoke(
        action: ResetPasswordAction,
        currentState: ResetPasswordViewState,
    ): ResetPasswordSideEffect? =
        when (action) {
            ResetPasswordAction.SubmissionSucceeded -> ResetPasswordSideEffect.Finished(Res.string.auth_reset_password_success_message)
            is ResetPasswordAction.ValidationFailed -> ResetPasswordSideEffect.ShowMessage(action.message)
            else -> null
        }
}
