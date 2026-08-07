package org.evolutionsoftware.bookly.features.auth

import androidx.compose.runtime.Composable
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_forgot_password_sent
import org.evolutionsoftware.bookly.features.auth.changepassword.ChangePasswordRoute
import org.evolutionsoftware.bookly.features.auth.forgotpassword.ForgotPasswordRoute
import org.evolutionsoftware.bookly.features.auth.onboarding.OnboardingRoute
import org.evolutionsoftware.bookly.features.auth.resetpassword.ResetPasswordRoute
import org.evolutionsoftware.bookly.features.auth.signin.SignInRoute
import org.evolutionsoftware.bookly.features.auth.signup.SignUpRoute
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthFlow(
    startDestination: AuthDestination,
    onExit: () -> Unit,
    onAuthenticated: (String) -> Unit,
    onSignedUp: () -> Unit,
    onFinished: (String) -> Unit,
    onShowMessage: (String) -> Unit,
    onOnboardingDone: () -> Unit = onExit,
) {
    val forgotPasswordSentMessage = stringResource(Res.string.auth_forgot_password_sent)
    val navigator = rememberAuthNavigator(
        startDestination = startDestination,
        onExit = onExit,
    )

    PlatformBackHandler {
        navigator.navigateBack()
    }

    when (navigator.currentDestination) {
        AuthDestination.SignIn ->
            SignInRoute(
                onBack = { navigator.navigateBack() },
                onForgotPassword = { navigator.navigateTo(AuthDestination.ForgotPassword) },
                onSignUp = { navigator.navigateTo(AuthDestination.SignUp) },
                onAuthenticated = onAuthenticated,
                onShowMessage = onShowMessage,
                onFacebook = { navigator.navigateTo(AuthDestination.Onboarding) },
            )
        AuthDestination.SignUp ->
            SignUpRoute(
                onBack = { navigator.navigateBack() },
                onSignIn = { navigator.resetTo(AuthDestination.SignIn) },
                onSignedUp = onSignedUp,
                onShowMessage = onShowMessage,
                onFacebook = { navigator.navigateTo(AuthDestination.Onboarding) },
            )
        AuthDestination.ChangePassword ->
            ChangePasswordRoute(
                onBack = { navigator.navigateBack() },
                onFinished = onFinished,
                onShowMessage = onShowMessage,
            )
        AuthDestination.ForgotPassword ->
            ForgotPasswordRoute(
                onBack = { navigator.navigateBack() },
                onLinkSent = {
                    onShowMessage(forgotPasswordSentMessage)
                    navigator.navigateTo(AuthDestination.ResetPassword)
                },
            )
        AuthDestination.ResetPassword ->
            ResetPasswordRoute(
                onBack = { navigator.navigateBack() },
                onFinished = onFinished,
                onShowMessage = onShowMessage,
            )
        AuthDestination.Onboarding ->
            OnboardingRoute(
                onDone = onOnboardingDone,
                onRegister = { navigator.resetTo(AuthDestination.SignUp) },
            )
    }
}
