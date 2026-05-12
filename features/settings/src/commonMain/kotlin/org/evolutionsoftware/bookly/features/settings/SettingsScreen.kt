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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
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
import org.evolutionsoftware.bookly.components.ui.PlayroomSocialButton
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsRoute(
    refreshKey: Int,
    onClose: () -> Unit,
    onRequireAuthentication: (SettingsAuthDestination) -> Unit,
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
                is SettingsSideEffect.RequireAuthentication -> onRequireAuthentication(effect.destination)
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
    if (state.isLoading && !state.isAuthenticated) return

    if (!state.isAuthenticated) {
        GuestSettingsScreen(
            onBack = onClose,
            onJoin = { onIntent(SettingsIntent.JoinClicked) },
            onLogin = { onIntent(SettingsIntent.LoginClicked) },
            onContinueWithFacebook = { onIntent(SettingsIntent.FacebookContinueClicked) },
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
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xxl),
        ) {
            SettingsSection(
                title = stringResource(Res.string.settings_account),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_edit_profile), Icons.SettingsEditProfile, TokenProvider.colors.bgInfoSoft) {
                            onIntent(SettingsIntent.EditProfileClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_change_password), Icons.SettingsChangePassword, TokenProvider.colors.bgWarningSoft) {
                            onIntent(SettingsIntent.ChangePasswordClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_reset_password), Icons.SettingsResetPassword, TokenProvider.colors.bgDangerSoft) {
                            onIntent(SettingsIntent.ResetPasswordClicked)
                        },
                    ),
            )

            SettingsSection(
                title = stringResource(Res.string.settings_preferences),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_notifications), Icons.SettingsNotifications, TokenProvider.colors.success.copy(alpha = 0.12f)) {
                            onIntent(SettingsIntent.NotificationsToggled(!state.notificationsEnabled))
                        },
                        SettingsRowItem(stringResource(Res.string.settings_sound_audio), Icons.SettingsSound, TokenProvider.colors.bgInfoSoft) {
                            onIntent(SettingsIntent.SoundToggled(!state.soundEnabled))
                        },
                    ),
            )

            SettingsSection(
                title = stringResource(Res.string.settings_support),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_help_center), Icons.SettingsHelp, TokenProvider.colors.bgWarningSoft) {
                            onIntent(SettingsIntent.HelpCenterClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_contact_us), Icons.SettingsContact, TokenProvider.colors.bgSuccessSoft.copy(alpha = 0.36f)) {
                            onIntent(SettingsIntent.ContactUsClicked)
                        },
                    ),
            )

            SettingsSection(
                title = stringResource(Res.string.settings_more),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_invite_friend), Icons.SettingsInviteFriend, TokenProvider.colors.bgInfoSoft) {
                            onIntent(SettingsIntent.InviteFriendClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_rate_app), Icons.SettingsRateApp, TokenProvider.colors.bgWarningSoft) {
                            onIntent(SettingsIntent.RateAppClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_language), Icons.SettingsLanguage, TokenProvider.colors.success.copy(alpha = 0.12f)) {
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
                                horizontal = TokenProvider.spacings.lg,
                                vertical = TokenProvider.spacings.sm,
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
                ) {
                    Icon(
                        painter = painterResource(Icons.SettingsLogout.icon),
                        contentDescription = null,
                        tint = TokenProvider.colors.textDanger,
                        modifier = Modifier.size(13.5.dp),
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
    onContinueWithFacebook: () -> Unit,
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
                onClick = onContinueWithFacebook,
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    items: List<SettingsRowItem>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapMd)) {
        Text(
            text = title.uppercase(),
            modifier = Modifier.padding(start = TokenProvider.spacings.md),
            style =
                TokenProvider.textStyles.eyebrow.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp,
                ),
            color = TokenProvider.colors.textMuted,
        )
        Column(verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapSm)) {
            items.forEach { item ->
                SettingsMenuRow(item)
            }
        }
    }
}

@Composable
private fun SettingsMenuRow(item: SettingsRowItem) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgElevated)
                .clickable(onClick = item.onClick)
                .padding(
                    horizontal = TokenProvider.spacings.lg - TokenProvider.spacings.xxs,
                    vertical = TokenProvider.spacings.lg - TokenProvider.spacings.xxs,
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
            Icon(
                painter = painterResource(item.icon.icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp),
            )
        }
        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            style = TokenProvider.textStyles.bodyStrong,
            color = TokenProvider.colors.text,
        )
        Icon(
            painter = painterResource(Icons.SettingsChevron.icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier =
                Modifier
                    .width(7.4.dp)
                    .height(12.dp)
        )
    }
}

private data class SettingsRowItem(
    val title: String,
    val icon: Icons,
    val iconBackground: Color,
    val onClick: () -> Unit,
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
