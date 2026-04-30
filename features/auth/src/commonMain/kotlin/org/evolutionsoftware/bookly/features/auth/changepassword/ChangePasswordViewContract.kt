package org.evolutionsoftware.bookly.features.auth.changepassword

import org.evolutionsoftware.bookly.core.mvi.SideEffect
import org.evolutionsoftware.bookly.core.mvi.UserIntent
import org.evolutionsoftware.bookly.core.mvi.UserIntentAction
import org.evolutionsoftware.bookly.core.mvi.ViewState
import org.jetbrains.compose.resources.StringResource

internal data class ChangePasswordViewState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessage: StringResource? = null,
) : ViewState

internal sealed interface ChangePasswordSideEffect : SideEffect {
    data class Finished(val message: StringResource) : ChangePasswordSideEffect

    data class ShowMessage(val message: StringResource) : ChangePasswordSideEffect
}

internal sealed interface ChangePasswordIntent : UserIntent {
    data class CurrentPasswordChanged(val value: String) : ChangePasswordIntent

    data class NewPasswordChanged(val value: String) : ChangePasswordIntent

    data class ConfirmPasswordChanged(val value: String) : ChangePasswordIntent

    data object NewPasswordVisibilityToggled : ChangePasswordIntent

    data object ConfirmPasswordVisibilityToggled : ChangePasswordIntent

    data class Submit(
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String,
    ) : ChangePasswordIntent
}

internal sealed interface ChangePasswordAction : UserIntentAction {
    data class CurrentPasswordUpdated(val value: String) : ChangePasswordAction

    data class NewPasswordUpdated(val value: String) : ChangePasswordAction

    data class ConfirmPasswordUpdated(val value: String) : ChangePasswordAction

    data object NewPasswordVisibilityToggled : ChangePasswordAction

    data object ConfirmPasswordVisibilityToggled : ChangePasswordAction

    data object SubmissionStarted : ChangePasswordAction

    data class ValidationFailed(val message: StringResource) : ChangePasswordAction

    data object SubmissionSucceeded : ChangePasswordAction
}
