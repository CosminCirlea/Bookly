package org.evolutionsoftware.bookly.features.auth

import androidx.compose.runtime.Composable
import org.evolutionsoftware.bookly.features.auth.changepassword.ChangePasswordRoute
import org.evolutionsoftware.bookly.features.auth.resetpassword.ResetPasswordRoute
import org.evolutionsoftware.bookly.features.auth.signin.SignInRoute
import org.evolutionsoftware.bookly.features.auth.signup.SignUpRoute

@Composable
fun AuthFlow(
    startDestination: AuthDestination,
    onExit: () -> Unit,
    onAuthenticated: (String) -> Unit,
    onFinished: (String) -> Unit,
    onShowMessage: (String) -> Unit,
) {
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
                onForgotPassword = { navigator.navigateTo(AuthDestination.ResetPassword) },
                onSignUp = { navigator.navigateTo(AuthDestination.SignUp) },
                onAuthenticated = onAuthenticated,
                onShowMessage = onShowMessage,
            )
        AuthDestination.SignUp ->
            SignUpRoute(
                onBack = { navigator.navigateBack() },
                onSignIn = { navigator.resetTo(AuthDestination.SignIn) },
                onAuthenticated = onAuthenticated,
                onShowMessage = onShowMessage,
            )
        AuthDestination.ChangePassword ->
            ChangePasswordRoute(
                onBack = { navigator.navigateBack() },
                onFinished = onFinished,
                onShowMessage = onShowMessage,
            )
        AuthDestination.ResetPassword ->
            ResetPasswordRoute(
                onBack = { navigator.navigateBack() },
                onFinished = onFinished,
                onShowMessage = onShowMessage,
            )
    }
}
