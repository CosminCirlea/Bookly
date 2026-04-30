package org.evolutionsoftware.bookly.features.auth.changepassword

import org.evolutionsoftware.bookly.core.mvi.StateMapper
import org.evolutionsoftware.bookly.features.auth.common.PasswordValidators

internal class ChangePasswordStateMapper : StateMapper<ChangePasswordAction, ChangePasswordViewState> {
    override fun invoke(
        action: ChangePasswordAction,
        currentState: ChangePasswordViewState,
    ): ChangePasswordViewState =
        when (action) {
            is ChangePasswordAction.CurrentPasswordUpdated ->
                currentState.copy(
                    currentPassword = action.value,
                    isFormValid = isFormValid(action.value, currentState.newPassword, currentState.confirmPassword),
                    errorMessage = null,
                )
            is ChangePasswordAction.NewPasswordUpdated ->
                currentState.copy(
                    newPassword = action.value,
                    isFormValid = isFormValid(currentState.currentPassword, action.value, currentState.confirmPassword),
                    errorMessage = null,
                )
            is ChangePasswordAction.ConfirmPasswordUpdated ->
                currentState.copy(
                    confirmPassword = action.value,
                    isFormValid = isFormValid(currentState.currentPassword, currentState.newPassword, action.value),
                    errorMessage = null,
                )
            ChangePasswordAction.NewPasswordVisibilityToggled ->
                currentState.copy(
                    isNewPasswordVisible = !currentState.isNewPasswordVisible,
                    errorMessage = null,
                )
            ChangePasswordAction.ConfirmPasswordVisibilityToggled ->
                currentState.copy(
                    isConfirmPasswordVisible = !currentState.isConfirmPasswordVisible,
                    errorMessage = null,
                )
            ChangePasswordAction.SubmissionStarted ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            is ChangePasswordAction.ValidationFailed ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = action.message,
                )
            ChangePasswordAction.SubmissionSucceeded ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = null,
                )
        }

    private fun isFormValid(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Boolean =
        PasswordValidators.isStrongEnough(currentPassword) &&
            PasswordValidators.isStrongEnough(newPassword) &&
            PasswordValidators.doPasswordsMatch(newPassword, confirmPassword)
}
