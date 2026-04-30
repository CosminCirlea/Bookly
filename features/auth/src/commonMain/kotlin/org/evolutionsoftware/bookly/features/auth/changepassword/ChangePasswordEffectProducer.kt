package org.evolutionsoftware.bookly.features.auth.changepassword

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_change_password_success_message
import org.evolutionsoftware.bookly.core.mvi.EffectProducer

internal class ChangePasswordEffectProducer :
    EffectProducer<ChangePasswordAction, ChangePasswordViewState, ChangePasswordSideEffect> {
    override fun invoke(
        action: ChangePasswordAction,
        currentState: ChangePasswordViewState,
    ): ChangePasswordSideEffect? =
        when (action) {
            ChangePasswordAction.SubmissionSucceeded -> ChangePasswordSideEffect.Finished(Res.string.auth_change_password_success_message)
            is ChangePasswordAction.ValidationFailed -> ChangePasswordSideEffect.ShowMessage(action.message)
            else -> null
        }
}
