package org.evolutionsoftware.bookly.features.auth.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_sign_in_facebook
import bookly.features.auth.generated.resources.auth_sign_in_google
import bookly.features.auth.generated.resources.auth_sign_up_confirm_password_label
import bookly.features.auth.generated.resources.auth_sign_up_confirm_password_placeholder
import bookly.features.auth.generated.resources.auth_sign_up_email_label
import bookly.features.auth.generated.resources.auth_sign_up_email_placeholder
import bookly.features.auth.generated.resources.auth_sign_up_password_label
import bookly.features.auth.generated.resources.auth_sign_up_password_placeholder
import bookly.features.auth.generated.resources.auth_sign_up_submit
import bookly.features.auth.generated.resources.auth_sign_up_title
import bookly.features.auth.generated.resources.auth_sign_up_to_sign_in
import org.evolutionsoftware.bookly.components.ui.PlayroomDivider
import org.evolutionsoftware.bookly.components.ui.PlayroomSocialButton
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.features.auth.common.AuthScreenScaffold
import org.evolutionsoftware.bookly.features.auth.common.PasswordSuffix
import org.evolutionsoftware.bookly.features.auth.common.primaryButtonProperties
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SignUpRoute(
    onBack: () -> Unit,
    onSignIn: () -> Unit,
    onAuthenticated: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel = rememberSignUpViewModel()
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                SignUpSideEffect.NavigateToSignIn -> onSignIn()
                is SignUpSideEffect.Authenticated -> onAuthenticated(getString(effect.message))
                is SignUpSideEffect.ShowMessage -> onShowMessage(getString(effect.message))
            }
        }
    }

    AuthScreenScaffold(
        title = stringResource(Res.string.auth_sign_up_title),
        onBack = onBack,
    ) {
        val textFieldState =
            if (viewState.isLoading) {
                TextFieldProperties.State.Disabled
            } else {
                TextFieldProperties.State.Default
            }
        TextField(
            properties =
                TextFieldProperties(
                    label = stringResource(Res.string.auth_sign_up_email_label),
                    placeholder = stringResource(Res.string.auth_sign_up_email_placeholder),
                    state = textFieldState,
                ),
            value = viewState.emailOrPhone,
            onValueChange = { viewModel.onUserIntent(SignUpIntent.EmailOrPhoneChanged(it)) },
            enabled = !viewState.isLoading,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        TextField(
            properties =
                TextFieldProperties(
                    label = stringResource(Res.string.auth_sign_up_password_label),
                    placeholder = stringResource(Res.string.auth_sign_up_password_placeholder),
                    state = textFieldState,
                ),
            value = viewState.password,
            onValueChange = { viewModel.onUserIntent(SignUpIntent.PasswordChanged(it)) },
            enabled = !viewState.isLoading,
            visualTransformation = if (viewState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            suffix = {
                PasswordSuffix(
                    visible = viewState.isPasswordVisible,
                    onClick = { viewModel.onUserIntent(SignUpIntent.PasswordVisibilityToggled) },
                )
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        TextField(
            properties =
                TextFieldProperties(
                    label = stringResource(Res.string.auth_sign_up_confirm_password_label),
                    placeholder = stringResource(Res.string.auth_sign_up_confirm_password_placeholder),
                    state = textFieldState,
                ),
            value = viewState.confirmPassword,
            onValueChange = { viewModel.onUserIntent(SignUpIntent.ConfirmPasswordChanged(it)) },
            enabled = !viewState.isLoading,
            visualTransformation = if (viewState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            suffix = {
                PasswordSuffix(
                    visible = viewState.isConfirmPasswordVisible,
                    onClick = { viewModel.onUserIntent(SignUpIntent.ConfirmPasswordVisibilityToggled) },
                )
            },
        )
        viewState.errorMessage?.let {
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
                    label = stringResource(Res.string.auth_sign_up_submit),
                    enabled = viewState.isFormValid && !viewState.isLoading,
                ),
            onClick = {
                viewModel.onUserIntent(
                    SignUpIntent.Submit(
                        emailOrPhone = viewState.emailOrPhone,
                        password = viewState.password,
                        confirmPassword = viewState.confirmPassword,
                    ),
                )
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))
        PlayroomDivider()
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapLg))
        PlayroomSocialButton(
            label = stringResource(Res.string.auth_sign_in_google),
            textColor = TokenProvider.colors.socialGoogle,
            icon = Icons.Google,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapSm))
        PlayroomSocialButton(
            label = stringResource(Res.string.auth_sign_in_facebook),
            textColor = TokenProvider.colors.socialFacebook,
            icon = Icons.Facebook,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapLg))
        Text(
            text = stringResource(Res.string.auth_sign_up_to_sign_in),
            modifier =
                Modifier.clickable(enabled = !viewState.isLoading) {
                    viewModel.onUserIntent(SignUpIntent.SignInClicked)
                },
            style = TokenProvider.textStyles.bodyStrong,
            color = TokenProvider.colors.textAccent,
            textAlign = TextAlign.Center,
        )
    }
}
