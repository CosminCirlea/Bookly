package org.evolutionsoftware.bookly.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.evolutionsoftware.bookly.design.theme.bookly.BooklyTheme
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile

private val previewProfile =
    ParentProfile(
        id = "preview-profile",
        displayName = "Mia",
    )

// === Settings =============================================================

@Preview(showBackground = true, showSystemUi = true, name = "Settings - guest")
@Composable
private fun SettingsGuestPreview() {
    BooklyTheme {
        SettingsScreenContent(
            state = SettingsViewState(isLoading = false),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Settings - signed in")
@Composable
private fun SettingsSignedInPreview() {
    BooklyTheme {
        SettingsScreenContent(
            state =
                SettingsViewState(
                    isLoading = false,
                    isSessionActive = true,
                    profile = previewProfile,
                ),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Settings - Romanian selected")
@Composable
private fun SettingsLanguagePreview() {
    BooklyTheme {
        SettingsScreenContent(
            state =
                SettingsViewState(
                    isLoading = false,
                    isSessionActive = true,
                    profile = previewProfile,
                ),
            selectedLanguageTag = "ro",
        )
    }
}

// === Notifications ========================================================

@Preview(showBackground = true, showSystemUi = true, name = "Notifications")
@Composable
private fun NotificationsPreview() {
    BooklyTheme {
        NotificationsRoute(
            onBack = {},
            onShowToast = { _, _ -> },
        )
    }
}

// === Contact us ===========================================================

@Preview(showBackground = true, showSystemUi = true, name = "Contact us")
@Composable
private fun ContactUsPreview() {
    BooklyTheme {
        ContactUsRoute(
            onBack = {},
            onShowToast = { _, _ -> },
        )
    }
}
