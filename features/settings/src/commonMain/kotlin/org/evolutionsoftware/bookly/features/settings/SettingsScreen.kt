package org.evolutionsoftware.bookly.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bookly.features.settings.generated.resources.Res
import bookly.features.settings.generated.resources.settings_signed_out_message
import kotlinx.coroutines.flow.collectLatest
import org.evolutionsoftware.bookly.components.ui.PlayroomDivider
import org.evolutionsoftware.bookly.components.ui.PlayroomMenuItem
import org.evolutionsoftware.bookly.components.ui.PlayroomSocialButton
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsRoute(
    refreshKey: Int,
    onClose: () -> Unit,
    onRequireAuthentication: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel = rememberSettingsViewModel()
    val state by viewModel.viewState.collectAsState()
    val signedOutMessage = stringResource(Res.string.settings_signed_out_message)

    LaunchedEffect(refreshKey) {
        viewModel.onUserIntent(SettingsIntent.Load)
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                SettingsSideEffect.RequireAuthentication -> onRequireAuthentication()
                SettingsSideEffect.SignedOut -> onShowMessage(signedOutMessage)
            }
        }
    }

    SettingsScreen(
        state = state,
        onIntent = viewModel::onUserIntent,
        onClose = onClose,
        onShowMessage = onShowMessage,
    )
}

@Composable
private fun SettingsScreen(
    state: SettingsViewState,
    onIntent: (SettingsIntent) -> Unit,
    onClose: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val toggles =
        remember {
            mutableStateMapOf(
                "Notifications" to true,
                "Sound & Audio" to true,
            )
        }

    if (!state.isAuthenticated) {
        GuestSettingsScreen(
            onBack = onClose,
            onJoin = { onIntent(SettingsIntent.AuthenticateClicked) },
            onLogin = { onIntent(SettingsIntent.AuthenticateClicked) },
        )
        return
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        Header(
            properties = HeaderProperties(title = "Settings"),
            onBackClick = onClose,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TokenProvider.spacings.lg,
                        vertical = TokenProvider.spacings.md,
                    ),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xl),
        ) {
            SettingsSection(
                title = "ACCOUNT",
                items =
                    listOf(
                        SettingsRowItem("Edit Profile", "☺", TokenProvider.colors.bgInfoSoft) {
                            onShowMessage("Profile editing is ready for the next backend step.")
                        },
                        SettingsRowItem("Change Password", "🔒", TokenProvider.colors.bgWarningSoft) {
                            onIntent(SettingsIntent.AuthenticateClicked)
                        },
                        SettingsRowItem("Reset Password", "↺", TokenProvider.colors.bgDangerSoft) {
                            onIntent(SettingsIntent.AuthenticateClicked)
                        },
                    ),
            )

            SettingsSection(
                title = "PREFERENCES",
                items =
                    listOf(
                        SettingsRowItem(
                            title = "Notifications",
                            icon = "🔔",
                            iconBackground = TokenProvider.colors.success.copy(alpha = 0.12f),
                            toggleKey = "Notifications",
                            onToggle = { toggles["Notifications"] = it },
                        ),
                        SettingsRowItem(
                            title = "Sound & Audio",
                            icon = "🔊",
                            iconBackground = TokenProvider.colors.bgInfoSoft,
                            toggleKey = "Sound & Audio",
                            onToggle = { toggles["Sound & Audio"] = it },
                        ),
                    ),
                toggles = toggles,
            )

            SettingsSection(
                title = "SUPPORT",
                items =
                    listOf(
                        SettingsRowItem("Help Center", "?", TokenProvider.colors.bgWarningSoft) {
                            onShowMessage("Help Center coming next.")
                        },
                        SettingsRowItem("Contact Us", "✉", TokenProvider.colors.bgSuccessSoft.copy(alpha = 0.36f)) {
                            onShowMessage("Contact options are not wired yet.")
                        },
                    ),
            )

            SettingsSection(
                title = "MORE",
                items =
                    listOf(
                        SettingsRowItem("Invite a friend", "➕", TokenProvider.colors.bgInfoSoft) {
                            onShowMessage("Invite flow coming next.")
                        },
                        SettingsRowItem("Rate the app", "★", TokenProvider.colors.bgWarningSoft) {
                            onShowMessage("Store rating hook not connected yet.")
                        },
                        SettingsRowItem("Language", "◎", TokenProvider.colors.success.copy(alpha = 0.12f)) {
                            onShowMessage("Language picker coming next.")
                        },
                    ),
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .background(TokenProvider.colors.bgDangerSoft)
                            .clickable { onIntent(SettingsIntent.SignOutClicked) }
                            .padding(
                                horizontal = TokenProvider.spacings.lg,
                                vertical = TokenProvider.spacings.sm,
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
                ) {
                    Text(
                        text = "↪",
                        style = TokenProvider.textStyles.bodyStrong,
                        color = TokenProvider.colors.textDanger,
                    )
                    Text(
                        text = "Log Out",
                        style = TokenProvider.textStyles.bodyStrong,
                        color = TokenProvider.colors.textDanger,
                    )
                }
            }
        }
    }
}

@Composable
private fun GuestSettingsScreen(
    onBack: () -> Unit,
    onJoin: () -> Unit,
    onLogin: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        Header(
            properties =
                HeaderProperties(
                    title = "Settings",
                    variant = HeaderProperties.Variant.Compact,
                ),
            onBackClick = onBack,
        )
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TokenProvider.spacings.lg,
                        vertical = TokenProvider.spacings.xl,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Welcome to the\nPlayroom!",
                style = TokenProvider.textStyles.headline.copy(fontWeight = FontWeight.ExtraBold),
                color = TokenProvider.colors.text,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
            Text(
                text = "Create an account to save your favorite books and track your progress.",
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.xxl + TokenProvider.spacings.xs))
            Button(
                properties = primaryButtonProperties(label = "Join the Playroom", enabled = true),
                onClick = onJoin,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.xl - TokenProvider.spacings.xs))
            Text(
                text = "Already have an account? Log in",
                modifier = Modifier.clickable(onClick = onLogin),
                style = TokenProvider.textStyles.bodyStrong,
                color = TokenProvider.colors.textAccent,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.xl))
            PlayroomDivider()
            Spacer(modifier = Modifier.height(TokenProvider.spacings.lg))
            PlayroomSocialButton(
                label = "Continue with Google",
                accent = TokenProvider.colors.socialGoogle,
                icon = "G",
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
            PlayroomSocialButton(
                label = "Continue with Facebook",
                accent = TokenProvider.colors.socialFacebook,
                icon = "f",
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    items: List<SettingsRowItem>,
    toggles: Map<String, Boolean> = emptyMap(),
) {
    Column(verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md)) {
        Text(
            text = title,
            modifier = Modifier.padding(start = TokenProvider.spacings.md),
            style = TokenProvider.textStyles.eyebrow.copy(fontWeight = FontWeight.Bold),
            color = TokenProvider.colors.textMuted,
        )
        Column(verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm)) {
            items.forEach { item ->
                if (item.toggleKey == null) {
                    PlayroomMenuItem(
                        title = item.title,
                        icon = item.icon,
                        iconBackground = item.iconBackground,
                        onClick = { item.onClick?.invoke() },
                    )
                } else {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    TokenProvider.colors.bgElevated,
                                    RoundedCornerShape(TokenProvider.borderRadius.md),
                                )
                                .padding(
                                    horizontal = TokenProvider.spacings.lg - TokenProvider.spacings.xxs,
                                    vertical = TokenProvider.spacings.lg - TokenProvider.spacings.xs,
                                ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(item.iconBackground),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = item.icon)
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = item.title,
                                modifier = Modifier.align(Alignment.CenterStart),
                                style = TokenProvider.textStyles.bodyStrong,
                                color = TokenProvider.colors.text,
                            )
                            Switch(
                                checked = toggles[item.toggleKey] == true,
                                onCheckedChange = { item.onToggle?.invoke(it) },
                                modifier = Modifier.align(Alignment.CenterEnd),
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class SettingsRowItem(
    val title: String,
    val icon: String,
    val iconBackground: Color,
    val onClick: (() -> Unit)? = null,
    val toggleKey: String? = null,
    val onToggle: ((Boolean) -> Unit)? = null,
)

private fun primaryButtonProperties(
    label: String,
    enabled: Boolean,
): ButtonProperties =
    ButtonProperties(
        label = label,
        size = ButtonProperties.Size.Large,
        state = if (enabled) ButtonProperties.State.Default else ButtonProperties.State.Disabled,
    )
