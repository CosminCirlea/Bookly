package org.evolutionsoftware.bookly.features.auth.signup

import org.evolutionsoftware.bookly.core.mvi.StateMapper

internal class SignUpStateMapper : StateMapper<SignUpAction, SignUpViewState> {
    override fun invoke(
        action: SignUpAction,
        currentState: SignUpViewState,
    ): SignUpViewState =
        when (action) {
            is SignUpAction.DisplayNameUpdated ->
                currentState.copy(
                    displayName = action.value,
                    isFormValid = SignUpValidators.isFormValid(action.value, currentState.emailOrPhone, currentState.password),
                    errorMessage = null,
                )
            is SignUpAction.EmailOrPhoneUpdated ->
                currentState.copy(
                    emailOrPhone = action.value,
                    isFormValid = SignUpValidators.isFormValid(currentState.displayName, action.value, currentState.password),
                    errorMessage = null,
                )
            is SignUpAction.PasswordUpdated ->
                currentState.copy(
                    password = action.value,
                    isFormValid = SignUpValidators.isFormValid(currentState.displayName, currentState.emailOrPhone, action.value),
                    errorMessage = null,
                )
            SignUpAction.PasswordVisibilityToggled ->
                currentState.copy(
                    isPasswordVisible = !currentState.isPasswordVisible,
                    errorMessage = null,
                )
            SignUpAction.SubmissionStarted ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            is SignUpAction.ValidationFailed ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = action.message,
                )
            SignUpAction.SubmissionSucceeded ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = null,
                )
            SignUpAction.SignInNavigationRequested -> currentState
        }
}
