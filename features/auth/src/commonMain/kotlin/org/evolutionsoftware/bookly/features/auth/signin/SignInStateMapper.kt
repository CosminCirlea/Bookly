package org.evolutionsoftware.bookly.features.auth.signin

import org.evolutionsoftware.bookly.core.mvi.StateMapper

internal class SignInStateMapper : StateMapper<SignInAction, SignInViewState> {
    override fun invoke(
        action: SignInAction,
        currentState: SignInViewState,
    ): SignInViewState =
        when (action) {
            is SignInAction.EmailOrPhoneUpdated ->
                currentState.copy(
                    emailOrPhone = action.value,
                    isFormValid = SignInValidators.isFormValid(action.value, currentState.password),
                    errorMessage = null,
                )
            is SignInAction.PasswordUpdated ->
                currentState.copy(
                    password = action.value,
                    isFormValid = SignInValidators.isFormValid(currentState.emailOrPhone, action.value),
                    errorMessage = null,
                )
            SignInAction.PasswordVisibilityToggled ->
                currentState.copy(
                    isPasswordVisible = !currentState.isPasswordVisible,
                    errorMessage = null,
                )
            SignInAction.SubmissionStarted ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            is SignInAction.ValidationFailed ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = action.message,
                )
            SignInAction.SubmissionSucceeded ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = null,
                )
            SignInAction.ForgotPasswordNavigationRequested,
            SignInAction.SignUpNavigationRequested -> currentState
        }
}
