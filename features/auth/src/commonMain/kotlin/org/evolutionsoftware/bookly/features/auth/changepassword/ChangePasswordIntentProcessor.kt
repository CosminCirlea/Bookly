package org.evolutionsoftware.bookly.features.auth.changepassword

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_password_match_error
import bookly.features.auth.generated.resources.auth_password_strength_error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.features.auth.common.PasswordValidators

internal class ChangePasswordIntentProcessor : IntentProcessor<ChangePasswordIntent, ChangePasswordAction> {
    override fun invoke(intent: ChangePasswordIntent): Flow<ChangePasswordAction> =
        when (intent) {
            ChangePasswordIntent.ConfirmPasswordVisibilityToggled -> flowOf(ChangePasswordAction.ConfirmPasswordVisibilityToggled)
            ChangePasswordIntent.NewPasswordVisibilityToggled -> flowOf(ChangePasswordAction.NewPasswordVisibilityToggled)
            is ChangePasswordIntent.ConfirmPasswordChanged -> flowOf(ChangePasswordAction.ConfirmPasswordUpdated(intent.value))
            is ChangePasswordIntent.CurrentPasswordChanged -> flowOf(ChangePasswordAction.CurrentPasswordUpdated(intent.value))
            is ChangePasswordIntent.NewPasswordChanged -> flowOf(ChangePasswordAction.NewPasswordUpdated(intent.value))
            is ChangePasswordIntent.Submit ->
                flow {
                    if (!PasswordValidators.isStrongEnough(intent.currentPassword) || !PasswordValidators.isStrongEnough(intent.newPassword)) {
                        emit(ChangePasswordAction.ValidationFailed(Res.string.auth_password_strength_error))
                        return@flow
                    }
                    if (!PasswordValidators.doPasswordsMatch(intent.newPassword, intent.confirmPassword)) {
                        emit(ChangePasswordAction.ValidationFailed(Res.string.auth_password_match_error))
                        return@flow
                    }

                    emit(ChangePasswordAction.SubmissionStarted)
                    emit(ChangePasswordAction.SubmissionSucceeded)
                }
        }
}
