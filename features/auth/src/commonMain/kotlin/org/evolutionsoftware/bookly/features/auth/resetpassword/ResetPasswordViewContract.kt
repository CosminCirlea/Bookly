package org.evolutionsoftware.bookly.features.auth.resetpassword

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.jetbrains.compose.resources.StringResource

internal data class ResetPasswordViewState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessage: StringResource? = null,
) : ViewState

internal sealed interface ResetPasswordSideEffect : SideEffect {
    data class Finished(val message: StringResource) : ResetPasswordSideEffect

    data class ShowMessage(val message: StringResource) : ResetPasswordSideEffect
}

internal sealed interface ResetPasswordIntent : UserIntent {
    data class NewPasswordChanged(val value: String) : ResetPasswordIntent

    data class ConfirmPasswordChanged(val value: String) : ResetPasswordIntent

    data object NewPasswordVisibilityToggled : ResetPasswordIntent

    data object ConfirmPasswordVisibilityToggled : ResetPasswordIntent

    data class Submit(
        val newPassword: String,
        val confirmPassword: String,
    ) : ResetPasswordIntent
}

internal sealed interface ResetPasswordAction : UserIntentAction {
    data class NewPasswordUpdated(val value: String) : ResetPasswordAction

    data class ConfirmPasswordUpdated(val value: String) : ResetPasswordAction

    data object NewPasswordVisibilityToggled : ResetPasswordAction

    data object ConfirmPasswordVisibilityToggled : ResetPasswordAction

    data object SubmissionStarted : ResetPasswordAction

    data class ValidationFailed(val message: StringResource) : ResetPasswordAction

    data object SubmissionSucceeded : ResetPasswordAction
}
