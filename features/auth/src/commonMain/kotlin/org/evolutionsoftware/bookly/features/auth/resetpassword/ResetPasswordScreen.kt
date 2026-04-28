package org.evolutionsoftware.bookly.features.auth.resetpassword

import androidx.compose.runtime.Composable
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_password_confirm_label
import bookly.features.auth.generated.resources.auth_password_confirm_placeholder
import bookly.features.auth.generated.resources.auth_password_current_label
import bookly.features.auth.generated.resources.auth_password_current_placeholder
import bookly.features.auth.generated.resources.auth_password_match_error
import bookly.features.auth.generated.resources.auth_password_new_label
import bookly.features.auth.generated.resources.auth_password_new_placeholder
import bookly.features.auth.generated.resources.auth_password_strength_error
import bookly.features.auth.generated.resources.auth_reset_password_headline
import bookly.features.auth.generated.resources.auth_reset_password_submit
import bookly.features.auth.generated.resources.auth_reset_password_subtitle
import bookly.features.auth.generated.resources.auth_reset_password_success_message
import bookly.features.auth.generated.resources.auth_reset_password_title
import org.evolutionsoftware.bookly.features.auth.common.PasswordEditorScreen
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ResetPasswordRoute(
    onBack: () -> Unit,
    onFinished: (String) -> Unit,
) {
    val successMessage = stringResource(Res.string.auth_reset_password_success_message)
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
        strengthError = stringResource(Res.string.auth_password_strength_error),
        matchError = stringResource(Res.string.auth_password_match_error),
        onBack = onBack,
        onSuccess = { onFinished(successMessage) },
    )
}
