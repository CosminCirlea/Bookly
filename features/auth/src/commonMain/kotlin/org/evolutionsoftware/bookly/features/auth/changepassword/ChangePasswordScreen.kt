package org.evolutionsoftware.bookly.features.auth.changepassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_change_password_submit
import bookly.features.auth.generated.resources.auth_change_password_title
import bookly.features.auth.generated.resources.auth_password_confirm_label
import bookly.features.auth.generated.resources.auth_password_confirm_placeholder
import bookly.features.auth.generated.resources.auth_password_current_label
import bookly.features.auth.generated.resources.auth_password_current_placeholder
import bookly.features.auth.generated.resources.auth_password_new_label
import bookly.features.auth.generated.resources.auth_password_new_placeholder
import kotlinx.coroutines.flow.collectLatest
import org.evolutionsoftware.bookly.features.auth.common.PasswordEditorScreen
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ChangePasswordRoute(
    onBack: () -> Unit,
    onFinished: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel = rememberChangePasswordViewModel()
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is ChangePasswordSideEffect.Finished -> onFinished(getString(effect.message))
                is ChangePasswordSideEffect.ShowMessage -> onShowMessage(getString(effect.message))
            }
        }
    }

    PasswordEditorScreen(
        screenTitle = stringResource(Res.string.auth_change_password_title),
        submitLabel = stringResource(Res.string.auth_change_password_submit),
        includeCurrentPassword = true,
        currentPasswordLabel = stringResource(Res.string.auth_password_current_label),
        currentPasswordPlaceholder = stringResource(Res.string.auth_password_current_placeholder),
        newPasswordLabel = stringResource(Res.string.auth_password_new_label),
        newPasswordPlaceholder = stringResource(Res.string.auth_password_new_placeholder),
        confirmPasswordLabel = stringResource(Res.string.auth_password_confirm_label),
        confirmPasswordPlaceholder = stringResource(Res.string.auth_password_confirm_placeholder),
        errorMessage = viewState.errorMessage,
        isLoading = viewState.isLoading,
        isSubmitEnabled = viewState.isFormValid && !viewState.isLoading,
        currentPassword = viewState.currentPassword,
        newPassword = viewState.newPassword,
        confirmPassword = viewState.confirmPassword,
        isNewPasswordVisible = viewState.isNewPasswordVisible,
        isConfirmPasswordVisible = viewState.isConfirmPasswordVisible,
        onBack = onBack,
        onCurrentPasswordChange = { viewModel.onUserIntent(ChangePasswordIntent.CurrentPasswordChanged(it)) },
        onNewPasswordChange = { viewModel.onUserIntent(ChangePasswordIntent.NewPasswordChanged(it)) },
        onConfirmPasswordChange = { viewModel.onUserIntent(ChangePasswordIntent.ConfirmPasswordChanged(it)) },
        onNewPasswordVisibilityToggle = { viewModel.onUserIntent(ChangePasswordIntent.NewPasswordVisibilityToggled) },
        onConfirmPasswordVisibilityToggle = { viewModel.onUserIntent(ChangePasswordIntent.ConfirmPasswordVisibilityToggled) },
        onSubmit = {
            viewModel.onUserIntent(
                ChangePasswordIntent.Submit(
                    currentPassword = viewState.currentPassword,
                    newPassword = viewState.newPassword,
                    confirmPassword = viewState.confirmPassword,
                ),
            )
        },
    )
}
