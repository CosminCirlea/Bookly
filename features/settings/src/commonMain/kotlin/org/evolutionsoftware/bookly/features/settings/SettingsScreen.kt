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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bookly.features.settings.generated.resources.Res
import bookly.features.settings.generated.resources.settings_account
import bookly.features.settings.generated.resources.settings_change_password
import bookly.features.settings.generated.resources.settings_contact_us
import bookly.features.settings.generated.resources.settings_continue_facebook
import bookly.features.settings.generated.resources.settings_continue_google
import bookly.features.settings.generated.resources.settings_edit_profile
import bookly.features.settings.generated.resources.settings_guest_existing_account
import bookly.features.settings.generated.resources.settings_guest_playroom_body
import bookly.features.settings.generated.resources.settings_guest_playroom_title
import bookly.features.settings.generated.resources.settings_help_center
import bookly.features.settings.generated.resources.settings_invite_friend
import bookly.features.settings.generated.resources.settings_join_playroom
import bookly.features.settings.generated.resources.settings_language
import bookly.features.settings.generated.resources.settings_log_out
import bookly.features.settings.generated.resources.settings_more
import bookly.features.settings.generated.resources.settings_notifications
import bookly.features.settings.generated.resources.settings_preferences
import bookly.features.settings.generated.resources.settings_rate_app
import bookly.features.settings.generated.resources.settings_reset_password
import bookly.features.settings.generated.resources.settings_sound_audio
import bookly.features.settings.generated.resources.settings_signed_out_message
import bookly.features.settings.generated.resources.settings_support
import bookly.features.settings.generated.resources.settings_title
import kotlinx.coroutines.flow.collectLatest
import org.evolutionsoftware.bookly.components.ui.PlayroomDivider
import org.evolutionsoftware.bookly.components.ui.PlayroomMenuItem
import org.evolutionsoftware.bookly.components.ui.PlayroomSocialButton
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.getString
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

    LaunchedEffect(refreshKey) {
        viewModel.onUserIntent(SettingsIntent.Load)
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                SettingsSideEffect.RequireAuthentication -> onRequireAuthentication()
                SettingsSideEffect.SignedOut -> onShowMessage(getString(Res.string.settings_signed_out_message))
                is SettingsSideEffect.ShowMessage -> onShowMessage(getString(effect.message))
            }
        }
    }

    SettingsScreen(
        state = state,
        onIntent = viewModel::onUserIntent,
        onClose = onClose,
    )
}

@Composable
private fun SettingsScreen(
    state: SettingsViewState,
    onIntent: (SettingsIntent) -> Unit,
    onClose: () -> Unit,
) {
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
            properties = HeaderProperties(title = stringResource(Res.string.settings_title)),
            onBackClick = onClose,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TokenProvider.spacings.horizontalSpacing,
                        vertical = TokenProvider.spacings.md,
                    ),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sectionGap),
        ) {
            SettingsSection(
                title = stringResource(Res.string.settings_account),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_edit_profile), "☺", TokenProvider.colors.bgInfoSoft) {
                            onIntent(SettingsIntent.EditProfileClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_change_password), "🔒", TokenProvider.colors.bgWarningSoft) {
                            onIntent(SettingsIntent.ChangePasswordClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_reset_password), "↺", TokenProvider.colors.bgDangerSoft) {
                            onIntent(SettingsIntent.ResetPasswordClicked)
                        },
                    ),
            )

            SettingsSection(
                title = stringResource(Res.string.settings_preferences),
                items =
                    listOf(
                        SettingsRowItem(
                            title = stringResource(Res.string.settings_notifications),
                            icon = "🔔",
                            iconBackground = TokenProvider.colors.success.copy(alpha = 0.12f),
                            toggleKey = ToggleKey.Notifications,
                            onToggle = { onIntent(SettingsIntent.NotificationsToggled(it)) },
                        ),
                        SettingsRowItem(
                            title = stringResource(Res.string.settings_sound_audio),
                            icon = "🔊",
                            iconBackground = TokenProvider.colors.bgInfoSoft,
                            toggleKey = ToggleKey.Sound,
                            onToggle = { onIntent(SettingsIntent.SoundToggled(it)) },
                        ),
                    ),
                toggles =
                    mapOf(
                        ToggleKey.Notifications to state.notificationsEnabled,
                        ToggleKey.Sound to state.soundEnabled,
                    ),
            )

            SettingsSection(
                title = stringResource(Res.string.settings_support),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_help_center), "?", TokenProvider.colors.bgWarningSoft) {
                            onIntent(SettingsIntent.HelpCenterClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_contact_us), "✉", TokenProvider.colors.bgSuccessSoft.copy(alpha = 0.36f)) {
                            onIntent(SettingsIntent.ContactUsClicked)
                        },
                    ),
            )

            SettingsSection(
                title = stringResource(Res.string.settings_more),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_invite_friend), "➕", TokenProvider.colors.bgInfoSoft) {
                            onIntent(SettingsIntent.InviteFriendClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_rate_app), "★", TokenProvider.colors.bgWarningSoft) {
                            onIntent(SettingsIntent.RateAppClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_language), "◎", TokenProvider.colors.success.copy(alpha = 0.12f)) {
                            onIntent(SettingsIntent.LanguageClicked)
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
                                horizontal = TokenProvider.spacings.horizontalSpacing,
                                vertical = TokenProvider.spacings.formGapSm,
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
                        text = stringResource(Res.string.settings_log_out),
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
                    title = stringResource(Res.string.settings_title),
                    variant = HeaderProperties.Variant.Compact,
                ),
            onBackClick = onBack,
        )
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TokenProvider.spacings.horizontalSpacing,
                        vertical = TokenProvider.spacings.sectionGap,
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.settings_guest_playroom_title),
                style = TokenProvider.textStyles.headline.copy(fontWeight = FontWeight.ExtraBold),
                color = TokenProvider.colors.text,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapMd))
            Text(
                text = stringResource(Res.string.settings_guest_playroom_body),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.screenBottomSpacing))
            Button(
                properties = primaryButtonProperties(label = stringResource(Res.string.settings_join_playroom), enabled = true),
                onClick = onJoin,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))
            Text(
                text = stringResource(Res.string.settings_guest_existing_account),
                modifier = Modifier.clickable(onClick = onLogin),
                style = TokenProvider.textStyles.bodyStrong,
                color = TokenProvider.colors.textAccent,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))
            PlayroomDivider()
            Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapLg))
            PlayroomSocialButton(
                label = stringResource(Res.string.settings_continue_google),
                textColor = TokenProvider.colors.socialGoogle,
                icon = Icons.Google,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.formGapSm))
            PlayroomSocialButton(
                label = stringResource(Res.string.settings_continue_facebook),
                textColor = TokenProvider.colors.socialFacebook,
                icon = Icons.Facebook,
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    items: List<SettingsRowItem>,
    toggles: Map<ToggleKey, Boolean> = emptyMap(),
) {
    Column(verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapMd)) {
        Text(
            text = title,
            modifier = Modifier.padding(start = TokenProvider.spacings.md),
            style = TokenProvider.textStyles.eyebrow.copy(fontWeight = FontWeight.Bold),
            color = TokenProvider.colors.textMuted,
        )
        Column(verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapSm)) {
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
                                    horizontal = TokenProvider.spacings.horizontalSpacing,
                                    vertical = TokenProvider.spacings.formGapLg,
                                ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapMd),
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
    val toggleKey: ToggleKey? = null,
    val onToggle: ((Boolean) -> Unit)? = null,
)

private enum class ToggleKey {
    Notifications,
    Sound,
}

private fun primaryButtonProperties(
    label: String,
    enabled: Boolean,
): ButtonProperties =
    ButtonProperties(
        label = label,
        size = ButtonProperties.Size.Large,
        state = if (enabled) ButtonProperties.State.Default else ButtonProperties.State.Disabled,
    )
