package org.evolutionsoftware.bookly.features.auth.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.TextField
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
            TextField(
                properties =
                    TextFieldProperties(
                        label = currentPasswordLabel,
                        placeholder = currentPasswordPlaceholder,
                        state = textFieldState,
                    ),
                value = currentPassword,
                onValueChange = onCurrentPasswordChange,
                enabled = !isLoading,
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        }
        TextField(
            properties =
                TextFieldProperties(
                    label = newPasswordLabel,
                    placeholder = newPasswordPlaceholder,
                    state = textFieldState,
                ),
            value = newPassword,
            onValueChange = onNewPasswordChange,
            enabled = !isLoading,
            visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            suffix = {
                PasswordSuffix(
                    visible = isNewPasswordVisible,
                    onClick = onNewPasswordVisibilityToggle,
                )
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        TextField(
            properties =
                TextFieldProperties(
                    label = confirmPasswordLabel,
                    placeholder = confirmPasswordPlaceholder,
                    state = textFieldState,
                ),
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            enabled = !isLoading,
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
