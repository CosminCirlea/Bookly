package org.evolutionsoftware.bookly.features.auth.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import bookly.features.auth.generated.resources.auth_sign_in_title
import bookly.features.auth.generated.resources.auth_sign_in_to_register
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
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SignInRoute(
    onBack: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignUp: () -> Unit,
    onAuthenticated: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel = rememberSignInViewModel()
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                SignInSideEffect.NavigateToForgotPassword -> onForgotPassword()
                SignInSideEffect.NavigateToSignUp -> onSignUp()
                is SignInSideEffect.Authenticated -> onAuthenticated(getString(effect.message))
                is SignInSideEffect.ShowMessage -> onShowMessage(getString(effect.message))
            }
        }
    }

    AuthScreenScaffold(
        title = stringResource(Res.string.auth_sign_in_title),
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
                    label = stringResource(Res.string.auth_sign_in_email_label),
                    placeholder = stringResource(Res.string.auth_sign_in_email_placeholder),
                    state = textFieldState,
                ),
            value = viewState.emailOrPhone,
            onValueChange = { viewModel.onUserIntent(SignInIntent.EmailOrPhoneChanged(it)) },
            enabled = !viewState.isLoading,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        TextField(
            properties =
                TextFieldProperties(
                    label = stringResource(Res.string.auth_sign_in_password_label),
                    placeholder = stringResource(Res.string.auth_sign_in_password_placeholder),
                    state = textFieldState,
                ),
            value = viewState.password,
            onValueChange = { viewModel.onUserIntent(SignInIntent.PasswordChanged(it)) },
            enabled = !viewState.isLoading,
            visualTransformation = if (viewState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            suffix = {
                PasswordSuffix(
                    visible = viewState.isPasswordVisible,
                    onClick = { viewModel.onUserIntent(SignInIntent.PasswordVisibilityToggled) },
                )
            },
        )
        Text(
            text = stringResource(Res.string.auth_sign_in_forgot_password),
            modifier =
                Modifier
                    .align(Alignment.End)
                    .padding(top = TokenProvider.spacings.formGapSm)
                    .clickable(enabled = !viewState.isLoading) {
                        viewModel.onUserIntent(SignInIntent.ForgotPasswordClicked)
                    },
            style = TokenProvider.textStyles.body,
            color = TokenProvider.colors.textBrand,
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
                    label = stringResource(Res.string.auth_sign_in_submit),
                    enabled = viewState.isFormValid && !viewState.isLoading,
                ),
            onClick = {
                viewModel.onUserIntent(
                    SignInIntent.Submit(
                        emailOrPhone = viewState.emailOrPhone,
                        password = viewState.password,
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
            text = stringResource(Res.string.auth_sign_in_to_register),
            modifier =
                Modifier.clickable(enabled = !viewState.isLoading) {
                    viewModel.onUserIntent(SignInIntent.SignUpClicked)
                },
            style = TokenProvider.textStyles.bodyStrong,
            color = TokenProvider.colors.textAccent,
            textAlign = TextAlign.Center,
        )
    }
}
