package org.evolutionsoftware.bookly

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.evolutionsoftware.bookly.design.theme.bookly.BooklyTheme
import org.evolutionsoftware.bookly.di.AppKoin
import org.evolutionsoftware.bookly.features.auth.AuthDestination
import org.evolutionsoftware.bookly.features.auth.AuthFlow
import org.evolutionsoftware.bookly.features.auth.createprofile.CreateProfileRoute
import org.evolutionsoftware.bookly.features.home.HomeRoute
import org.evolutionsoftware.bookly.features.reader.ReaderRoute
import org.evolutionsoftware.bookly.features.settings.SettingsAuthDestination
import org.evolutionsoftware.bookly.features.settings.SettingsRoute

@Composable
@Preview
fun App() {
    remember { AppKoin.start() }

    BooklyTheme {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        var refreshKey by remember { mutableIntStateOf(0) }
        var destination by remember { mutableStateOf<AppDestination>(AppDestination.Home) }

        fun showMessage(message: String) {
            refreshKey++
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { _ ->
            when (val screen = destination) {
                AppDestination.Home ->
                    HomeRoute(
                        refreshKey = refreshKey,
                        onBookSelected = { bookId -> destination = AppDestination.Reader(bookId) },
                        onSettingsClick = { destination = AppDestination.Settings },
                    )

                is AppDestination.Reader -> {
                    PlatformBackHandler {
                        destination = AppDestination.Home
                    }
                    ReaderRoute(
                        bookId = screen.bookId,
                        onBack = { destination = AppDestination.Home },
                        onShowMessage = ::showMessage,
                    )
                }

                AppDestination.Settings -> {
                    PlatformBackHandler {
                        destination = AppDestination.Home
                    }
                    SettingsRoute(
                        refreshKey = refreshKey,
                        onClose = { destination = AppDestination.Home },
                        onRequireAuthentication = { authDestination ->
                            destination =
                                AppDestination.Auth(
                                    when (authDestination) {
                                        SettingsAuthDestination.SignIn -> AuthDestination.SignIn
                                        SettingsAuthDestination.SignUp -> AuthDestination.SignUp
                                        SettingsAuthDestination.ChangePassword -> AuthDestination.ChangePassword
                                        SettingsAuthDestination.ResetPassword -> AuthDestination.ResetPassword
                                    },
                                )
                        },
                        onShowMessage = ::showMessage,
                    )
                }

                is AppDestination.Auth -> {
                    AuthFlow(
                        startDestination = screen.destination,
                        onExit = { destination = AppDestination.Settings },
                        onAuthenticated = {
                            destination = AppDestination.Home
                            showMessage(it)
                        },
                        onSignedUp = { destination = AppDestination.CreateProfile },
                        onFinished = {
                            destination = AppDestination.Settings
                            showMessage(it)
                        },
                        onShowMessage = ::showMessage,
                    )
                }

                AppDestination.CreateProfile -> {
                    CreateProfileRoute(
                        onProfileCreated = { destination = AppDestination.Home },
                        onSkip = { destination = AppDestination.Home },
                        onShowMessage = ::showMessage,
                    )
                }
            }
        }
    }
}

private sealed interface AppDestination {
    data object Home : AppDestination

    data class Reader(val bookId: String) : AppDestination

    data object Settings : AppDestination

    data class Auth(val destination: AuthDestination) : AppDestination

    data object CreateProfile : AppDestination
}
