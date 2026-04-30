package org.evolutionsoftware.bookly.features.auth.resetpassword

import org.evolutionsoftware.bookly.core.mvi.StateMapper
import org.evolutionsoftware.bookly.features.auth.common.PasswordValidators

internal class ResetPasswordStateMapper : StateMapper<ResetPasswordAction, ResetPasswordViewState> {
    override fun invoke(
        action: ResetPasswordAction,
        currentState: ResetPasswordViewState,
    ): ResetPasswordViewState =
        when (action) {
            is ResetPasswordAction.NewPasswordUpdated ->
                currentState.copy(
                    newPassword = action.value,
                    isFormValid = isFormValid(action.value, currentState.confirmPassword),
                    errorMessage = null,
                )
            is ResetPasswordAction.ConfirmPasswordUpdated ->
                currentState.copy(
                    confirmPassword = action.value,
                    isFormValid = isFormValid(currentState.newPassword, action.value),
                    errorMessage = null,
                )
            ResetPasswordAction.NewPasswordVisibilityToggled ->
                currentState.copy(
                    isNewPasswordVisible = !currentState.isNewPasswordVisible,
                    errorMessage = null,
                )
            ResetPasswordAction.ConfirmPasswordVisibilityToggled ->
                currentState.copy(
                    isConfirmPasswordVisible = !currentState.isConfirmPasswordVisible,
                    errorMessage = null,
                )
            ResetPasswordAction.SubmissionStarted ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            is ResetPasswordAction.ValidationFailed ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = action.message,
                )
            ResetPasswordAction.SubmissionSucceeded ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = null,
                )
        }

    private fun isFormValid(
        newPassword: String,
        confirmPassword: String,
    ): Boolean =
        PasswordValidators.isStrongEnough(newPassword) &&
            PasswordValidators.doPasswordsMatch(newPassword, confirmPassword)
}
