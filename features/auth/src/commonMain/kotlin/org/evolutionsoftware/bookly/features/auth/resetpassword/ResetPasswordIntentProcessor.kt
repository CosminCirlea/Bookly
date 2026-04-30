package org.evolutionsoftware.bookly.features.auth.resetpassword

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_password_match_error
import bookly.features.auth.generated.resources.auth_password_strength_error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.features.auth.common.PasswordValidators

internal class ResetPasswordIntentProcessor : IntentProcessor<ResetPasswordIntent, ResetPasswordAction> {
    override fun invoke(intent: ResetPasswordIntent): Flow<ResetPasswordAction> =
        when (intent) {
            ResetPasswordIntent.ConfirmPasswordVisibilityToggled -> flowOf(ResetPasswordAction.ConfirmPasswordVisibilityToggled)
            ResetPasswordIntent.NewPasswordVisibilityToggled -> flowOf(ResetPasswordAction.NewPasswordVisibilityToggled)
            is ResetPasswordIntent.ConfirmPasswordChanged -> flowOf(ResetPasswordAction.ConfirmPasswordUpdated(intent.value))
            is ResetPasswordIntent.NewPasswordChanged -> flowOf(ResetPasswordAction.NewPasswordUpdated(intent.value))
            is ResetPasswordIntent.Submit ->
                flow {
                    if (!PasswordValidators.isStrongEnough(intent.newPassword)) {
                        emit(ResetPasswordAction.ValidationFailed(Res.string.auth_password_strength_error))
                        return@flow
                    }
                    if (!PasswordValidators.doPasswordsMatch(intent.newPassword, intent.confirmPassword)) {
                        emit(ResetPasswordAction.ValidationFailed(Res.string.auth_password_match_error))
                        return@flow
                    }

                    emit(ResetPasswordAction.SubmissionStarted)
                    emit(ResetPasswordAction.SubmissionSucceeded)
                }
        }
}
