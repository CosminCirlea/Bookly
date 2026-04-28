package org.evolutionsoftware.bookly.features.auth.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_sign_in_email_label
import bookly.features.auth.generated.resources.auth_sign_in_email_placeholder
import bookly.features.auth.generated.resources.auth_sign_in_facebook
import bookly.features.auth.generated.resources.auth_sign_in_forgot_password
import bookly.features.auth.generated.resources.auth_sign_in_google
import bookly.features.auth.generated.resources.auth_sign_in_password_label
import bookly.features.auth.generated.resources.auth_sign_in_password_placeholder
import bookly.features.auth.generated.resources.auth_sign_in_submit
import bookly.features.auth.generated.resources.auth_sign_in_success_message
import bookly.features.auth.generated.resources.auth_sign_in_symbol
import bookly.features.auth.generated.resources.auth_sign_in_title
import bookly.features.auth.generated.resources.auth_sign_in_to_register
import bookly.features.auth.generated.resources.auth_sign_in_validation_error
import kotlinx.coroutines.launch
import org.evolutionsoftware.bookly.components.ui.PlayroomDivider
import org.evolutionsoftware.bookly.components.ui.PlayroomSocialButton
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.features.auth.common.AuthIllustration
import org.evolutionsoftware.bookly.features.auth.common.AuthScreenScaffold
import org.evolutionsoftware.bookly.features.auth.common.PasswordSuffix
import org.evolutionsoftware.bookly.features.auth.common.primaryButtonProperties
import org.evolutionsoftware.bookly.features.auth.common.resolveDisplayName
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.LoginUseCase
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.GlobalContext

@Composable
internal fun SignInRoute(
    onBack: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignUp: () -> Unit,
    onAuthenticated: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val loginUseCase = remember { GlobalContext.get().get<LoginUseCase>() }
    val title = stringResource(Res.string.auth_sign_in_title)
    val symbol = stringResource(Res.string.auth_sign_in_symbol)
    val emailLabel = stringResource(Res.string.auth_sign_in_email_label)
    val emailPlaceholder = stringResource(Res.string.auth_sign_in_email_placeholder)
    val passwordLabel = stringResource(Res.string.auth_sign_in_password_label)
    val passwordPlaceholder = stringResource(Res.string.auth_sign_in_password_placeholder)
    val forgotPasswordLabel = stringResource(Res.string.auth_sign_in_forgot_password)
    val submitLabel = stringResource(Res.string.auth_sign_in_submit)
    val validationError = stringResource(Res.string.auth_sign_in_validation_error)
    val successMessage = stringResource(Res.string.auth_sign_in_success_message)
    val googleLabel = stringResource(Res.string.auth_sign_in_google)
    val facebookLabel = stringResource(Res.string.auth_sign_in_facebook)
    val toRegisterLabel = stringResource(Res.string.auth_sign_in_to_register)
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val isSubmitEnabled =
        remember(emailOrPhone, password) {
            resolveDisplayName(emailOrPhone).isNotBlank() && password.length >= 4
        }

    AuthScreenScaffold(
        title = title,
        onBack = onBack,
    ) {
        AuthIllustration(symbol = symbol)
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xl - TokenProvider.spacings.xs))
        TextField(
            properties =
                TextFieldProperties(
                    label = emailLabel,
                    placeholder = emailPlaceholder,
                ),
            value = emailOrPhone,
            onValueChange = {
                emailOrPhone = it
                error = null
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
        TextField(
            properties =
                TextFieldProperties(
                    label = passwordLabel,
                    placeholder = passwordPlaceholder,
                ),
            value = password,
            onValueChange = {
                password = it
                error = null
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            suffix = {
                PasswordSuffix(
                    visible = showPassword,
                    onClick = { showPassword = !showPassword },
                )
            },
        )
        Text(
            text = forgotPasswordLabel,
            modifier =
                Modifier
                    .align(Alignment.End)
                    .padding(top = TokenProvider.spacings.xs)
                    .clickable(onClick = onForgotPassword),
            style = TokenProvider.textStyles.body,
            color = TokenProvider.colors.textBrand,
        )
        error?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = TokenProvider.spacings.sm),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textDanger,
            )
        }
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xl - TokenProvider.spacings.xs))
        Button(
            properties = primaryButtonProperties(label = submitLabel, enabled = isSubmitEnabled),
            onClick = {
                val displayName = resolveDisplayName(emailOrPhone)
                if (displayName.isBlank() || password.length < 4) {
                    error = validationError
                    onShowMessage(validationError)
                } else {
                    scope.launch {
                        loginUseCase(displayName)
                        onAuthenticated(successMessage)
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xl - TokenProvider.spacings.xs))
        PlayroomDivider()
        Spacer(modifier = Modifier.height(TokenProvider.spacings.lg - TokenProvider.spacings.xxs))
        PlayroomSocialButton(
            label = googleLabel,
            accent = TokenProvider.colors.socialGoogle,
            icon = "G",
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
        PlayroomSocialButton(
            label = facebookLabel,
            accent = TokenProvider.colors.socialFacebook,
            icon = "f",
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.lg))
        Text(
            text = toRegisterLabel,
            modifier = Modifier.clickable(onClick = onSignUp),
            style = TokenProvider.textStyles.bodyStrong,
            color = TokenProvider.colors.textAccent,
            textAlign = TextAlign.Center,
        )
    }
}
