package org.evolutionsoftware.bookly.features.auth.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
internal fun PasswordEditorScreen(
    screenTitle: String,
    headline: String,
    subtitle: String,
    submitLabel: String,
    includeCurrentPassword: Boolean,
    currentPasswordLabel: String,
    currentPasswordPlaceholder: String,
    newPasswordLabel: String,
    newPasswordPlaceholder: String,
    confirmPasswordLabel: String,
    confirmPasswordPlaceholder: String,
    strengthError: String,
    matchError: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val isSubmitEnabled =
        remember(currentPassword, newPassword, confirmPassword, includeCurrentPassword) {
            (!includeCurrentPassword || currentPassword.length >= 4) &&
                newPassword.length >= 4 &&
                newPassword == confirmPassword
        }

    AuthScreenScaffold(
        title = screenTitle,
        onBack = onBack,
    ) {
        Text(
            text = headline,
            style = TokenProvider.textStyles.title,
            color = TokenProvider.colors.text,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
        Text(
            text = subtitle,
            style = TokenProvider.textStyles.body,
            color = TokenProvider.colors.textMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xl))
        if (includeCurrentPassword) {
            TextField(
                properties =
                    TextFieldProperties(
                        label = currentPasswordLabel,
                        placeholder = currentPasswordPlaceholder,
                    ),
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    error = null
                },
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
        }
        TextField(
            properties =
                TextFieldProperties(
                    label = newPasswordLabel,
                    placeholder = newPasswordPlaceholder,
                ),
            value = newPassword,
            onValueChange = {
                newPassword = it
                error = null
            },
            visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
            suffix = {
                PasswordSuffix(
                    visible = showNewPassword,
                    onClick = { showNewPassword = !showNewPassword },
                )
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
        TextField(
            properties =
                TextFieldProperties(
                    label = confirmPasswordLabel,
                    placeholder = confirmPasswordPlaceholder,
                ),
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                error = null
            },
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            suffix = {
                PasswordSuffix(
                    visible = showConfirmPassword,
                    onClick = { showConfirmPassword = !showConfirmPassword },
                )
            },
        )
        error?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = TokenProvider.spacings.sm),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textDanger,
            )
        }
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xl))
        Button(
            properties = primaryButtonProperties(label = submitLabel, enabled = isSubmitEnabled),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if ((includeCurrentPassword && currentPassword.length < 4) || newPassword.length < 4) {
                    error = strengthError
                } else if (newPassword != confirmPassword) {
                    error = matchError
                } else {
                    onSuccess()
                }
            },
        )
    }
}
