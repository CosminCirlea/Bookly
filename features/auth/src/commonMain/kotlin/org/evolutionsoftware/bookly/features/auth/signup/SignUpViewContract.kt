package org.evolutionsoftware.bookly.features.auth.signup

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.jetbrains.compose.resources.StringResource

internal data class SignUpViewState(
    val emailOrPhone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessage: StringResource? = null,
) : ViewState

internal sealed interface SignUpSideEffect : SideEffect {
    data object NavigateToSignIn : SignUpSideEffect

    data class Authenticated(val message: StringResource) : SignUpSideEffect

    data class ShowMessage(val message: StringResource) : SignUpSideEffect
}

internal sealed interface SignUpIntent : UserIntent {
    data class EmailOrPhoneChanged(val value: String) : SignUpIntent

    data class PasswordChanged(val value: String) : SignUpIntent

    data class ConfirmPasswordChanged(val value: String) : SignUpIntent

    data object PasswordVisibilityToggled : SignUpIntent

    data object ConfirmPasswordVisibilityToggled : SignUpIntent

    data class Submit(
        val emailOrPhone: String,
        val password: String,
        val confirmPassword: String,
    ) : SignUpIntent

    data object SignInClicked : SignUpIntent
}

internal sealed interface SignUpAction : UserIntentAction {
    data class EmailOrPhoneUpdated(val value: String) : SignUpAction

    data class PasswordUpdated(val value: String) : SignUpAction

    data class ConfirmPasswordUpdated(val value: String) : SignUpAction

    data object PasswordVisibilityToggled : SignUpAction

    data object ConfirmPasswordVisibilityToggled : SignUpAction

    data object SubmissionStarted : SignUpAction

    data class ValidationFailed(val message: StringResource) : SignUpAction

    data object SubmissionSucceeded : SignUpAction

    data object SignInNavigationRequested : SignUpAction
}
