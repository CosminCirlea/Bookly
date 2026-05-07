package org.evolutionsoftware.bookly.features.auth.signin

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.jetbrains.compose.resources.StringResource

internal data class SignInViewState(
    val emailOrPhone: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessage: StringResource? = null,
) : ViewState

internal sealed interface SignInSideEffect : SideEffect {
    data object NavigateToForgotPassword : SignInSideEffect

    data object NavigateToSignUp : SignInSideEffect

    data class Authenticated(val message: StringResource) : SignInSideEffect

    data class ShowMessage(val message: StringResource) : SignInSideEffect
}

internal sealed interface SignInIntent : UserIntent {
    data class EmailOrPhoneChanged(val value: String) : SignInIntent

    data class PasswordChanged(val value: String) : SignInIntent

    data object PasswordVisibilityToggled : SignInIntent

    data class Submit(
        val emailOrPhone: String,
        val password: String,
    ) : SignInIntent

    data object ForgotPasswordClicked : SignInIntent

    data object SignUpClicked : SignInIntent
}

internal sealed interface SignInAction : UserIntentAction {
    data class EmailOrPhoneUpdated(val value: String) : SignInAction

    data class PasswordUpdated(val value: String) : SignInAction

    data object PasswordVisibilityToggled : SignInAction

    data object SubmissionStarted : SignInAction

    data class ValidationFailed(val message: StringResource) : SignInAction

    data object SubmissionSucceeded : SignInAction

    data object SubmissionFailed : SignInAction

    data object ForgotPasswordNavigationRequested : SignInAction

    data object SignUpNavigationRequested : SignInAction
}
