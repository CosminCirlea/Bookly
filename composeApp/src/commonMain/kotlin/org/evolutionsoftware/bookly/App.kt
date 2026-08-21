package org.evolutionsoftware.bookly

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import org.evolutionsoftware.bookly.components.ui.BooklyToastHost
import org.evolutionsoftware.bookly.components.ui.BooklyToastKind
import org.evolutionsoftware.bookly.components.ui.rememberBooklyToastState
import org.evolutionsoftware.bookly.debug.DebugButtonsScreen
import org.evolutionsoftware.bookly.debug.DebugColorsScreen
import org.evolutionsoftware.bookly.debug.DebugEmptyScreen
import org.evolutionsoftware.bookly.debug.DebugErrorScreen
import org.evolutionsoftware.bookly.debug.DebugIconButtonsScreen
import org.evolutionsoftware.bookly.debug.DebugLoadingScreen
import org.evolutionsoftware.bookly.debug.DebugMenuScreen
import org.evolutionsoftware.bookly.debug.DebugTextFieldsScreen
import org.evolutionsoftware.bookly.debug.DebugTypographyScreen
import org.evolutionsoftware.bookly.design.theme.bookly.BooklyTheme
import org.evolutionsoftware.bookly.di.AppKoin
import org.evolutionsoftware.bookly.features.auth.AuthDestination
import org.evolutionsoftware.bookly.features.auth.AuthFlow
import org.evolutionsoftware.bookly.features.auth.createprofile.CreateProfileRoute
import org.evolutionsoftware.bookly.features.home.HomeRoute
import org.evolutionsoftware.bookly.features.reader.ReaderRoute
import org.evolutionsoftware.bookly.features.reader.debug.DebugReaderRoute
import org.evolutionsoftware.bookly.features.settings.ContactUsRoute
import org.evolutionsoftware.bookly.features.settings.NotificationsRoute
import org.evolutionsoftware.bookly.features.settings.SettingsAuthDestination
import org.evolutionsoftware.bookly.features.settings.SettingsRoute

@Composable
@Preview
fun App(startWithDebugMenu: Boolean = false) {
    remember { AppKoin.start() }
    var appLanguage by remember {
        mutableStateOf(AppLanguage.fromLanguageTag(AppLocaleController.currentLanguageTag()))
    }

    key(appLanguage) {
        BooklyContent(
            startWithDebugMenu = startWithDebugMenu,
            selectedLanguageTag = appLanguage.languageTag,
            onLanguageSelected = { languageTag ->
                val language = AppLanguage.fromLanguageTag(languageTag)
                AppLocaleController.setLanguageTag(language.languageTag)
                appLanguage = language
            },
        )
    }
}

@Composable
private fun BooklyContent(
    startWithDebugMenu: Boolean,
    selectedLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    BooklyTheme {
        val toastState = rememberBooklyToastState()
        val saveableStateHolder = rememberSaveableStateHolder()
        var refreshKey by remember { mutableIntStateOf(0) }
        val initialDestination = if (startWithDebugMenu) AppDestination.DebugMenu else AppDestination.Home
        var destination by remember { mutableStateOf<AppDestination>(initialDestination) }

        fun showToast(
            message: String,
            kind: BooklyToastKind,
        ) {
            refreshKey++
            toastState.show(message, kind)
        }

        fun showMessage(message: String) {
            showToast(message, BooklyToastKind.Info)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Each destination leaves the composition when we navigate away, which would
            // discard its rememberSaveable state (scroll offsets, form input). Holding it
            // per destination key restores that state when the user comes back.
            saveableStateHolder.SaveableStateProvider(destination.stateKey) {
                when (val screen = destination) {
                    AppDestination.Home ->
                        HomeRoute(
                            refreshKey = refreshKey,
                            onBookSelected = { bookId ->
                                val readerDestination = AppDestination.Reader(bookId)
                                saveableStateHolder.removeState(readerDestination.stateKey)
                                destination = readerDestination
                            },
                            onSettingsClick = { destination = AppDestination.Settings },
                            onShowToast = ::showToast,
                        )

                    is AppDestination.Reader -> {
                        PlatformBackHandler {
                            destination = AppDestination.Home
                        }
                        ReaderRoute(
                            bookId = screen.bookId,
                            onBack = { destination = AppDestination.Home },
                            onShowMessage = ::showMessage,
                            onShowToast = ::showToast,
                        )
                    }

                    AppDestination.Settings -> {
                        PlatformBackHandler {
                            destination = AppDestination.Home
                        }
                        SettingsRoute(
                            refreshKey = refreshKey,
                            selectedLanguageTag = selectedLanguageTag,
                            onClose = { destination = AppDestination.Home },
                            onRequireAuthentication = { authDestination ->
                                destination =
                                    AppDestination.Auth(
                                        when (authDestination) {
                                            SettingsAuthDestination.SignIn -> AuthDestination.SignIn
                                            SettingsAuthDestination.ChangePassword -> AuthDestination.ChangePassword
                                            SettingsAuthDestination.ResetPassword -> AuthDestination.ForgotPassword
                                        },
                                    )
                            },
                            onShowMessage = ::showMessage,
                            onShowToast = ::showToast,
                            onOpenNotifications = { destination = AppDestination.Notifications },
                            onOpenContactUs = { destination = AppDestination.ContactUs },
                            onOpenEditProfile = { destination = AppDestination.CreateProfile(fromSettings = true) },
                            onLanguageSelected = onLanguageSelected,
                        )
                    }

                    AppDestination.Notifications -> {
                        PlatformBackHandler {
                            destination = AppDestination.Settings
                        }
                        NotificationsRoute(
                            onBack = { destination = AppDestination.Settings },
                            onShowToast = ::showToast,
                        )
                    }

                    AppDestination.ContactUs -> {
                        PlatformBackHandler {
                            destination = AppDestination.Settings
                        }
                        ContactUsRoute(
                            onBack = { destination = AppDestination.Settings },
                            onShowToast = ::showToast,
                        )
                    }

                    is AppDestination.Auth -> {
                        AuthFlow(
                            startDestination = screen.destination,
                            onExit = { destination = AppDestination.Settings },
                            onAuthenticated = {
                                destination = AppDestination.Home
                                showToast(it, BooklyToastKind.Success)
                            },
                            onSignedUp = { destination = AppDestination.CreateProfile(fromSettings = false) },
                            onFinished = {
                                destination = AppDestination.Settings
                                showToast(it, BooklyToastKind.Success)
                            },
                            onShowMessage = ::showMessage,
                            onOnboardingDone = { destination = AppDestination.Home },
                        )
                    }

                    is AppDestination.CreateProfile -> {
                        // Editing from Settings returns there; the post-sign-up step goes on to Home.
                        val exitDestination =
                            if (screen.fromSettings) AppDestination.Settings else AppDestination.Home
                        PlatformBackHandler { destination = exitDestination }
                        CreateProfileRoute(
                            onProfileCreated = { destination = exitDestination },
                            onSkip = { destination = exitDestination },
                            onShowMessage = ::showMessage,
                        )
                    }

                    AppDestination.DebugMenu -> {
                        PlatformBackHandler { destination = AppDestination.Home }
                        DebugMenuScreen(
                            onClose = { destination = AppDestination.Home },
                            onNavigateToButtons = { destination = AppDestination.DebugButtons },
                            onNavigateToTextFields = { destination = AppDestination.DebugTextFields },
                            onNavigateToIconButtons = { destination = AppDestination.DebugIconButtons },
                            onNavigateToColors = { destination = AppDestination.DebugColors },
                            onNavigateToTypography = { destination = AppDestination.DebugTypography },
                            onNavigateToReader = { destination = AppDestination.DebugReader },
                            onNavigateToLoading = { destination = AppDestination.DebugLoading },
                            onNavigateToEmpty = { destination = AppDestination.DebugEmpty },
                            onNavigateToError = { destination = AppDestination.DebugError },
                        )
                    }

                    AppDestination.DebugReader -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugReaderRoute(onBack = { destination = AppDestination.DebugMenu })
                    }

                    AppDestination.DebugButtons -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugButtonsScreen(onClose = { destination = AppDestination.DebugMenu })
                    }

                    AppDestination.DebugTextFields -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugTextFieldsScreen(onClose = { destination = AppDestination.DebugMenu })
                    }

                    AppDestination.DebugIconButtons -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugIconButtonsScreen(onClose = { destination = AppDestination.DebugMenu })
                    }

                    AppDestination.DebugColors -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugColorsScreen(onClose = { destination = AppDestination.DebugMenu })
                    }

                    AppDestination.DebugTypography -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugTypographyScreen(onClose = { destination = AppDestination.DebugMenu })
                    }

                    AppDestination.DebugLoading -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugLoadingScreen(onClose = { destination = AppDestination.DebugMenu })
                    }

                    AppDestination.DebugEmpty -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugEmptyScreen(onClose = { destination = AppDestination.DebugMenu })
                    }

                    AppDestination.DebugError -> {
                        PlatformBackHandler { destination = AppDestination.DebugMenu }
                        DebugErrorScreen(onClose = { destination = AppDestination.DebugMenu })
                    }
                }
            }

            BooklyToastHost(
                state = toastState,
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(10f),
            )
        }
    }
}

private sealed interface AppDestination {
    /**
     * Identity under which this destination's saveable state is retained. Screens that
     * show different content per argument get distinct keys, so one book's reading
     * position is never restored into another's.
     */
    val stateKey: String
        get() = toString()

    data object Home : AppDestination

    data class Reader(val bookId: String) : AppDestination

    data object Settings : AppDestination

    data object Notifications : AppDestination

    data object ContactUs : AppDestination

    data class Auth(val destination: AuthDestination) : AppDestination

    data class CreateProfile(val fromSettings: Boolean) : AppDestination

    data object DebugMenu : AppDestination

    data object DebugReader : AppDestination

    data object DebugButtons : AppDestination

    data object DebugTextFields : AppDestination

    data object DebugIconButtons : AppDestination

    data object DebugColors : AppDestination

    data object DebugTypography : AppDestination

    data object DebugLoading : AppDestination

    data object DebugEmpty : AppDestination

    data object DebugError : AppDestination
}
