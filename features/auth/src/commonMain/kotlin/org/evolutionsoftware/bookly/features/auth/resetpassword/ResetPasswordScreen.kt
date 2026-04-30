package org.evolutionsoftware.bookly.features.auth.resetpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_password_confirm_label
import bookly.features.auth.generated.resources.auth_password_confirm_placeholder
import bookly.features.auth.generated.resources.auth_password_current_label
import bookly.features.auth.generated.resources.auth_password_current_placeholder
import bookly.features.auth.generated.resources.auth_password_new_label
import bookly.features.auth.generated.resources.auth_password_new_placeholder
import bookly.features.auth.generated.resources.auth_reset_password_headline
import bookly.features.auth.generated.resources.auth_reset_password_submit
import bookly.features.auth.generated.resources.auth_reset_password_subtitle
import bookly.features.auth.generated.resources.auth_reset_password_title
import kotlinx.coroutines.flow.collectLatest
import org.evolutionsoftware.bookly.features.auth.common.PasswordEditorScreen
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ResetPasswordRoute(
    onBack: () -> Unit,
    onFinished: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel = rememberResetPasswordViewModel()
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is ResetPasswordSideEffect.Finished -> onFinished(getString(effect.message))
                is ResetPasswordSideEffect.ShowMessage -> onShowMessage(getString(effect.message))
            }
        }
    }

    PasswordEditorScreen(
        screenTitle = stringResource(Res.string.auth_reset_password_title),
        headline = stringResource(Res.string.auth_reset_password_headline),
        subtitle = stringResource(Res.string.auth_reset_password_subtitle),
        submitLabel = stringResource(Res.string.auth_reset_password_submit),
        includeCurrentPassword = false,
        currentPasswordLabel = stringResource(Res.string.auth_password_current_label),
        currentPasswordPlaceholder = stringResource(Res.string.auth_password_current_placeholder),
        newPasswordLabel = stringResource(Res.string.auth_password_new_label),
        newPasswordPlaceholder = stringResource(Res.string.auth_password_new_placeholder),
        confirmPasswordLabel = stringResource(Res.string.auth_password_confirm_label),
        confirmPasswordPlaceholder = stringResource(Res.string.auth_password_confirm_placeholder),
        errorMessage = viewState.errorMessage,
        isLoading = viewState.isLoading,
        isSubmitEnabled = viewState.isFormValid && !viewState.isLoading,
        currentPassword = "",
        newPassword = viewState.newPassword,
        confirmPassword = viewState.confirmPassword,
        isNewPasswordVisible = viewState.isNewPasswordVisible,
        isConfirmPasswordVisible = viewState.isConfirmPasswordVisible,
        onBack = onBack,
        onCurrentPasswordChange = {},
        onNewPasswordChange = { viewModel.onUserIntent(ResetPasswordIntent.NewPasswordChanged(it)) },
        onConfirmPasswordChange = { viewModel.onUserIntent(ResetPasswordIntent.ConfirmPasswordChanged(it)) },
        onNewPasswordVisibilityToggle = { viewModel.onUserIntent(ResetPasswordIntent.NewPasswordVisibilityToggled) },
        onConfirmPasswordVisibilityToggle = { viewModel.onUserIntent(ResetPasswordIntent.ConfirmPasswordVisibilityToggled) },
        onSubmit = {
            viewModel.onUserIntent(
                ResetPasswordIntent.Submit(
                    newPassword = viewState.newPassword,
                    confirmPassword = viewState.confirmPassword,
                ),
            )
        },
    )
}
