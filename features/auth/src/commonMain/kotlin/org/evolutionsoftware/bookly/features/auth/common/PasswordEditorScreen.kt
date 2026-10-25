package org.evolutionsoftware.bookly.features.auth.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.SecureTextField
import org.evolutionsoftware.bookly.design.components.TextFieldChangeEffect
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
internal fun PasswordEditorScreen(
    screenTitle: String,
    description: String?,
    submitLabel: String,
    includeCurrentPassword: Boolean,
    currentPasswordLabel: String,
    currentPasswordPlaceholder: String,
    newPasswordLabel: String,
    newPasswordPlaceholder: String,
    confirmPasswordLabel: String,
    confirmPasswordPlaceholder: String,
    errorMessage: StringResource?,
    isLoading: Boolean,
    isSubmitEnabled: Boolean,
    showPasswordStrength: Boolean,
    currentPassword: String,
    newPassword: String,
    confirmPassword: String,
    isNewPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,
    onBack: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNewPasswordVisibilityToggle: () -> Unit,
    onConfirmPasswordVisibilityToggle: () -> Unit,
    onSubmit: () -> Unit,
) {
    val textFieldState =
        if (isLoading) {
            TextFieldProperties.State.Disabled
        } else {
            TextFieldProperties.State.Default
        }

    val currentPasswordState = rememberTextFieldState(currentPassword)
    val newPasswordState = rememberTextFieldState(newPassword)
    val confirmPasswordState = rememberTextFieldState(confirmPassword)
    TextFieldChangeEffect(currentPasswordState, onCurrentPasswordChange)
    TextFieldChangeEffect(newPasswordState, onNewPasswordChange)
    TextFieldChangeEffect(confirmPasswordState, onConfirmPasswordChange)

    AuthScreenScaffold(
        title = screenTitle,
        onBack = onBack,
    ) {
        if (description != null) {
            Text(
                text = description,
                modifier = Modifier.padding(bottom = TokenProvider.spacings.lg),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
            )
        }
        if (includeCurrentPassword) {
            SecureTextField(
                properties =
                    TextFieldProperties(
                        label = currentPasswordLabel,
                        placeholder = currentPasswordPlaceholder,
                        state = textFieldState,
                    ),
                state = currentPasswordState,
                enabled = !isLoading,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        }
        SecureTextField(
            properties =
                TextFieldProperties(
                    label = newPasswordLabel,
                    placeholder = newPasswordPlaceholder,
                    state = textFieldState,
                ),
            state = newPasswordState,
            enabled = !isLoading,
            isRevealed = isNewPasswordVisible,
            suffix = {
                PasswordSuffix(
                    visible = isNewPasswordVisible,
                    onClick = onNewPasswordVisibilityToggle,
                )
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        SecureTextField(
            properties =
                TextFieldProperties(
                    label = confirmPasswordLabel,
                    placeholder = confirmPasswordPlaceholder,
                    state = textFieldState,
                ),
            state = confirmPasswordState,
            enabled = !isLoading,
            isRevealed = isConfirmPasswordVisible,
            suffix = {
                PasswordSuffix(
                    visible = isConfirmPasswordVisible,
                    onClick = onConfirmPasswordVisibilityToggle,
                )
            },
        )
        if (showPasswordStrength) {
            Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapSm))
            PasswordStrengthMeter(
                password = newPassword,
                modifier = Modifier.padding(horizontal = TokenProvider.spacings.xxs),
            )
        }
        errorMessage?.let {
            Text(
                text = stringResource(it),
                modifier = Modifier.padding(top = TokenProvider.spacings.formGapSm),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textDanger,
            )
        }
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))
        Button(
            properties =
                primaryButtonProperties(
                    label = submitLabel,
                    enabled = isSubmitEnabled,
                    loading = isLoading,
                ),
            modifier = Modifier.fillMaxWidth(),
            onClick = onSubmit,
        )
    }
}
