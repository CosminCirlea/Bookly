package org.evolutionsoftware.bookly.features.auth.signup

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_sign_in_facebook
import bookly.features.auth.generated.resources.auth_sign_in_google
import bookly.features.auth.generated.resources.auth_sign_up_confirm_password_label
import bookly.features.auth.generated.resources.auth_sign_up_divider
import bookly.features.auth.generated.resources.auth_sign_up_terms
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
import org.evolutionsoftware.bookly.design.components.ActionText
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.SecureTextField
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.TextFieldChangeEffect
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.features.auth.common.AuthScreenScaffold
import org.evolutionsoftware.bookly.features.auth.common.PasswordStrengthMeter
import org.evolutionsoftware.bookly.features.auth.common.PasswordSuffix
import org.evolutionsoftware.bookly.features.auth.common.primaryButtonProperties
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SignUpRoute(
    onBack: () -> Unit,
    onSignIn: () -> Unit,
    onSignedUp: () -> Unit,
    onShowMessage: (String) -> Unit,
    onFacebook: () -> Unit = {},
) {
    val viewModel = rememberSignUpViewModel()
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                SignUpSideEffect.NavigateToSignIn -> onSignIn()
                SignUpSideEffect.ReadyForProfileCreation -> onSignedUp()
                is SignUpSideEffect.ShowMessage -> onShowMessage(getString(effect.message))
            }
        }
    }

    SignUpContent(
        viewState = viewState,
        onIntent = viewModel::onUserIntent,
        onBack = onBack,
        onFacebook = onFacebook,
    )
}

@Composable
internal fun SignUpContent(
    viewState: SignUpViewState,
    onIntent: (SignUpIntent) -> Unit,
    onBack: () -> Unit,
    onFacebook: () -> Unit = {},
) {
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
        val emailState = rememberTextFieldState(viewState.emailOrPhone)
        val passwordState = rememberTextFieldState(viewState.password)
        val confirmPasswordState = rememberTextFieldState(viewState.confirmPassword)
        TextFieldChangeEffect(emailState) { onIntent(SignUpIntent.EmailOrPhoneChanged(it)) }
        TextFieldChangeEffect(passwordState) { onIntent(SignUpIntent.PasswordChanged(it)) }
        TextFieldChangeEffect(confirmPasswordState) { onIntent(SignUpIntent.ConfirmPasswordChanged(it)) }

        TextField(
            properties =
                TextFieldProperties(
                    label = stringResource(Res.string.auth_sign_up_email_label),
                    placeholder = stringResource(Res.string.auth_sign_up_email_placeholder),
                    state = textFieldState,
                ),
            state = emailState,
            enabled = !viewState.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        SecureTextField(
            properties =
                TextFieldProperties(
                    label = stringResource(Res.string.auth_sign_up_password_label),
                    placeholder = stringResource(Res.string.auth_sign_up_password_placeholder),
                    state = textFieldState,
                ),
            state = passwordState,
            enabled = !viewState.isLoading,
            isRevealed = viewState.isPasswordVisible,
            suffix = {
                PasswordSuffix(
                    visible = viewState.isPasswordVisible,
                    onClick = { onIntent(SignUpIntent.PasswordVisibilityToggled) },
                )
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapSm))
        PasswordStrengthMeter(
            password = viewState.password,
            modifier = Modifier.padding(horizontal = TokenProvider.spacings.xxs),
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
        SecureTextField(
            properties =
                TextFieldProperties(
                    label = stringResource(Res.string.auth_sign_up_confirm_password_label),
                    placeholder = stringResource(Res.string.auth_sign_up_confirm_password_placeholder),
                    state = textFieldState,
                ),
            state = confirmPasswordState,
            enabled = !viewState.isLoading,
            isRevealed = viewState.isConfirmPasswordVisible,
            suffix = {
                PasswordSuffix(
                    visible = viewState.isConfirmPasswordVisible,
                    onClick = { onIntent(SignUpIntent.ConfirmPasswordVisibilityToggled) },
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
                    loading = viewState.isLoading,
                ),
            onClick = {
                onIntent(
                    SignUpIntent.Submit(
                        emailOrPhone = viewState.emailOrPhone,
                        password = viewState.password,
                        confirmPassword = viewState.confirmPassword,
                    ),
                )
            },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))
        PlayroomDivider(label = stringResource(Res.string.auth_sign_up_divider))
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapLg))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md, Alignment.CenterHorizontally),
        ) {
        PlayroomSocialButton(
            label = stringResource(Res.string.auth_sign_in_google),
            icon = Icons.Google,
        )
        PlayroomSocialButton(
            label = stringResource(Res.string.auth_sign_in_facebook),
            icon = Icons.Facebook,
            onClick = onFacebook,
        )
        }
        Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapLg))
        Text(
            text = stringResource(Res.string.auth_sign_up_terms),
            modifier = Modifier.padding(horizontal = TokenProvider.spacings.md),
            style = TokenProvider.textStyles.eyebrow,
            color = TokenProvider.colors.textMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
        ActionText(
            text = stringResource(Res.string.auth_sign_up_to_sign_in),
            onClick = { onIntent(SignUpIntent.SignInClicked) },
            enabled = !viewState.isLoading,
            textAlign = TextAlign.Center,
        )
    }
}
