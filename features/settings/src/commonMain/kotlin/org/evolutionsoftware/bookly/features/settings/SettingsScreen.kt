package org.evolutionsoftware.bookly.features.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.settings.generated.resources.Res
import bookly.features.settings.generated.resources.settings_account
import bookly.features.settings.generated.resources.settings_change_password
import bookly.features.settings.generated.resources.settings_contact_us
import bookly.features.settings.generated.resources.settings_edit_profile
import bookly.features.settings.generated.resources.settings_edit_profile_aria
import bookly.features.settings.generated.resources.settings_free_plan
import bookly.features.settings.generated.resources.settings_help_center
import bookly.features.settings.generated.resources.settings_invite_body
import bookly.features.settings.generated.resources.settings_invite_close
import bookly.features.settings.generated.resources.settings_invite_copy
import bookly.features.settings.generated.resources.settings_invite_email
import bookly.features.settings.generated.resources.settings_invite_friend
import bookly.features.settings.generated.resources.settings_invite_link
import bookly.features.settings.generated.resources.settings_invite_message
import bookly.features.settings.generated.resources.settings_invite_more
import bookly.features.settings.generated.resources.settings_invite_title
import bookly.features.settings.generated.resources.settings_language
import bookly.features.settings.generated.resources.settings_language_subtitle
import bookly.features.settings.generated.resources.settings_language_title
import bookly.features.settings.generated.resources.settings_log_out
import bookly.features.settings.generated.resources.settings_login_banner_body
import bookly.features.settings.generated.resources.settings_login_banner_title
import bookly.features.settings.generated.resources.settings_logout_body
import bookly.features.settings.generated.resources.settings_logout_cancel
import bookly.features.settings.generated.resources.settings_logout_confirm
import bookly.features.settings.generated.resources.settings_logout_title
import bookly.features.settings.generated.resources.settings_more
import bookly.features.settings.generated.resources.settings_notifications
import bookly.features.settings.generated.resources.settings_preferences
import bookly.features.settings.generated.resources.settings_rate_app
import bookly.features.settings.generated.resources.settings_rate_later
import bookly.features.settings.generated.resources.settings_rate_placeholder_improve
import bookly.features.settings.generated.resources.settings_rate_placeholder_love
import bookly.features.settings.generated.resources.settings_rate_submit
import bookly.features.settings.generated.resources.settings_rate_subtitle
import bookly.features.settings.generated.resources.settings_rate_title
import bookly.features.settings.generated.resources.settings_reset_password
import bookly.features.settings.generated.resources.settings_signed_out_message
import bookly.features.settings.generated.resources.settings_sound_audio
import bookly.features.settings.generated.resources.settings_support
import bookly.features.settings.generated.resources.settings_title
import bookly.features.settings.generated.resources.settings_version
import kotlinx.coroutines.flow.collectLatest
import org.evolutionsoftware.bookly.components.ui.BooklySheet
import org.evolutionsoftware.bookly.components.ui.BooklyToastKind
import org.evolutionsoftware.bookly.components.ui.PlayroomDivider
import org.evolutionsoftware.bookly.components.ui.PlayroomGhostButton
import org.evolutionsoftware.bookly.components.ui.PlayroomSocialButton
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.services.profiles.domain.model.ParentProfile
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsRoute(
    refreshKey: Int,
    onClose: () -> Unit,
    onRequireAuthentication: (SettingsAuthDestination) -> Unit,
    onShowMessage: (String) -> Unit,
    onShowToast: (String, BooklyToastKind) -> Unit = { message, _ -> onShowMessage(message) },
    onOpenNotifications: () -> Unit = {},
    onOpenContactUs: () -> Unit = {},
    onOpenEditProfile: () -> Unit = {},
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
                SettingsSideEffect.SignedOut ->
                    onShowToast(getString(Res.string.settings_signed_out_message), BooklyToastKind.Info)
                is SettingsSideEffect.ShowMessage ->
                    onShowToast(
                        getString(effect.message, *effect.args.toTypedArray()),
                        if (effect.isSuccess) BooklyToastKind.Success else BooklyToastKind.Info,
                    )
                SettingsSideEffect.OpenNotifications -> onOpenNotifications()
                SettingsSideEffect.OpenContactUs -> onOpenContactUs()
                SettingsSideEffect.OpenEditProfile -> onOpenEditProfile()
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

    var showRateSheet by remember { mutableStateOf(false) }
    var showInviteSheet by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showLogoutSheet by remember { mutableStateOf(false) }

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
            onLeadingClick = onClose,
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
            // Signed out, the profile card's slot invites the parent to sign in rather
            // than hiding the whole menu behind a wall.
            val profile = state.profile
            if (profile != null) {
                ProfileCard(
                    profile = profile,
                    onEditClick = { onIntent(SettingsIntent.EditProfileClicked) },
                )
            } else {
                LoginBanner(onClick = { onIntent(SettingsIntent.LoginClicked) })
            }

            if (state.isAuthenticated) {
                SettingsSection(
                    title = stringResource(Res.string.settings_account),
                    items =
                        listOf(
                            SettingsRowItem(stringResource(Res.string.settings_edit_profile), Icons.SettingsEditProfile, SettingsRowStyles.Info) {
                                onIntent(SettingsIntent.EditProfileClicked)
                            },
                            SettingsRowItem(stringResource(Res.string.settings_change_password), Icons.SettingsChangePassword, SettingsRowStyles.Accent) {
                                onIntent(SettingsIntent.ChangePasswordClicked)
                            },
                            SettingsRowItem(stringResource(Res.string.settings_reset_password), Icons.SettingsResetPassword, SettingsRowStyles.Danger) {
                                onIntent(SettingsIntent.ResetPasswordClicked)
                            },
                        ),
                )
            }

            SettingsSection(
                title = stringResource(Res.string.settings_preferences),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_notifications), Icons.SettingsNotifications, SettingsRowStyles.Success) {
                            onIntent(SettingsIntent.NotificationsClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_sound_audio), Icons.SettingsSound, SettingsRowStyles.Info) {
                            onIntent(SettingsIntent.SoundClicked)
                        },
                    ),
            )

            SettingsSection(
                title = stringResource(Res.string.settings_support),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_help_center), Icons.SettingsHelp, SettingsRowStyles.Help) {
                            onIntent(SettingsIntent.HelpCenterClicked)
                        },
                        SettingsRowItem(stringResource(Res.string.settings_contact_us), Icons.SettingsContact, SettingsRowStyles.Success) {
                            onIntent(SettingsIntent.ContactUsClicked)
                        },
                    ),
            )

            SettingsSection(
                title = stringResource(Res.string.settings_more),
                items =
                    listOf(
                        SettingsRowItem(stringResource(Res.string.settings_invite_friend), Icons.SettingsInviteFriend, SettingsRowStyles.Info) {
                            showInviteSheet = true
                        },
                        SettingsRowItem(stringResource(Res.string.settings_rate_app), Icons.SettingsRateApp, SettingsRowStyles.Accent) {
                            showRateSheet = true
                        },
                        SettingsRowItem(
                            title = stringResource(Res.string.settings_language),
                            icon = Icons.SettingsLanguage,
                            style = SettingsRowStyles.Success,
                            trailingText = state.selectedLanguage,
                        ) {
                            showLanguageSheet = true
                        },
                    ),
            )

            if (state.isAuthenticated) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .background(TokenProvider.colors.bgDangerSoft)
                                .clickable { showLogoutSheet = true }
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

            Text(
                text = stringResource(Res.string.settings_version).uppercase(),
                modifier = Modifier.fillMaxWidth(),
                style =
                    TokenProvider.textStyles.eyebrow.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.6.sp,
                    ),
                color = TokenProvider.colors.textMuted.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )
        }
    }

    RateAppSheet(
        visible = showRateSheet,
        onDismiss = { showRateSheet = false },
        onSubmit = { stars ->
            showRateSheet = false
            onIntent(SettingsIntent.RateSubmitted(stars))
        },
    )

    InviteFriendSheet(
        visible = showInviteSheet,
        onDismiss = { showInviteSheet = false },
        onCopy = {
            showInviteSheet = false
            onIntent(SettingsIntent.InviteLinkCopied)
        },
    )

    LanguageSheet(
        visible = showLanguageSheet,
        selectedLanguage = state.selectedLanguage,
        onDismiss = { showLanguageSheet = false },
        onLanguageSelected = { language ->
            showLanguageSheet = false
            onIntent(SettingsIntent.LanguageSelected(language))
        },
    )

    LogoutConfirmSheet(
        visible = showLogoutSheet,
        onDismiss = { showLogoutSheet = false },
        onConfirm = {
            showLogoutSheet = false
            onIntent(SettingsIntent.SignOutClicked)
        },
    )
}

// === Profile card =========================================================

/**
 * Occupies the profile card's slot when signed out: a pressable card inviting the
 * parent to sign in, rather than hiding the whole menu behind an auth wall.
 */
@Composable
private fun LoginBanner(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.99f else 1f,
        label = "loginBannerScale",
    )

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .scale(scale)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.lg),
                    ambientColor = Color(0x0F392E00),
                    spotColor = Color(0x0F392E00),
                ).clip(RoundedCornerShape(TokenProvider.borderRadius.lg))
                .background(TokenProvider.colors.bgSurface)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ).padding(TokenProvider.spacings.lg - TokenProvider.spacings.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
    ) {
        Box(
            modifier =
                Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(TokenProvider.colors.bgElevated),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Icons.SettingsEditProfile.icon),
                contentDescription = null,
                tint = TokenProvider.colors.textSubtle,
                modifier = Modifier.size(32.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.settings_login_banner_title),
                style = TokenProvider.textStyles.title,
                color = TokenProvider.colors.text,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(Res.string.settings_login_banner_body),
                style = TokenProvider.textStyles.eyebrow,
                color = TokenProvider.colors.textSubtle,
            )
        }
        Icon(
            painter = painterResource(Icons.SettingsChevron.icon),
            contentDescription = null,
            tint = TokenProvider.colors.textSubtle.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun ProfileCard(
    profile: ParentProfile,
    onEditClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.lg),
                    ambientColor = Color(0x0F392E00),
                    spotColor = Color(0x0F392E00),
                )
                .clip(RoundedCornerShape(TokenProvider.borderRadius.lg))
                .background(TokenProvider.colors.bgSurface)
                .padding(TokenProvider.spacings.lg - TokenProvider.spacings.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
    ) {
        Box(
            modifier =
                Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(TokenProvider.colors.bgElevated),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = profile.initials,
                style = TokenProvider.textStyles.title.copy(fontSize = 22.sp),
                color = TokenProvider.colors.text,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = profile.displayName,
                style = TokenProvider.textStyles.title,
                color = TokenProvider.colors.text,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.xs - 2.dp))
            Row(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFFFE0B2))
                        .padding(
                            horizontal = TokenProvider.spacings.xs,
                            vertical = 2.dp,
                        ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Icon(
                    painter = painterResource(Icons.Star.icon),
                    contentDescription = null,
                    tint = Color(0xFF874E00),
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    text = stringResource(Res.string.settings_free_plan).uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Color(0xFF874E00),
                )
            }
        }
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(TokenProvider.colors.bgElevated)
                    .clickable(onClick = onEditClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Icons.Edit.icon),
                contentDescription = stringResource(Res.string.settings_edit_profile_aria),
                tint = TokenProvider.colors.textAccent,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

// === Sheets ===============================================================

@Composable
private fun RateAppSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (Int) -> Unit,
) {
    var stars by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf("") }

    // Cleared on open rather than on close. Resetting on close removes the conditional
    // feedback field while the sheet is still sliding out, so it visibly shrinks first.
    LaunchedEffect(visible) {
        if (visible) {
            stars = 0
            feedback = ""
        }
    }

    BooklySheet(visible = visible, onDismiss = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SheetIconBadge(icon = Icons.Star, background = Color(0xFFFFE0B2), tint = Color(0xFF874E00))
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
            Text(
                text = stringResource(Res.string.settings_rate_title),
                style = TokenProvider.textStyles.title.copy(fontSize = 24.sp),
                color = TokenProvider.colors.text,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.xs))
            Text(
                text = stringResource(Res.string.settings_rate_subtitle),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.lg - TokenProvider.spacings.xxs))
            Row(horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs)) {
                repeat(5) { index ->
                    val starNumber = index + 1
                    val filled = starNumber <= stars
                    val interactionSource = remember { MutableInteractionSource() }
                    Icon(
                        painter = painterResource(if (filled) Icons.Star.icon else Icons.StarOutline.icon),
                        contentDescription = null,
                        tint = if (filled) TokenProvider.colors.borderAccent else Color(0xFFE6D8A0),
                        modifier =
                            Modifier
                                .size(44.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                ) { stars = starNumber },
                    )
                }
            }
            if (stars > 0) {
                Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
                SheetTextArea(
                    value = feedback,
                    onValueChange = { feedback = it },
                    placeholder =
                        stringResource(
                            if (stars >= 4) {
                                Res.string.settings_rate_placeholder_love
                            } else {
                                Res.string.settings_rate_placeholder_improve
                            },
                        ),
                    minHeight = 88.dp,
                )
            }
            Spacer(modifier = Modifier.height(TokenProvider.spacings.lg - TokenProvider.spacings.xxs))
            Button(
                properties =
                    ButtonProperties(
                        label = stringResource(Res.string.settings_rate_submit),
                        size = ButtonProperties.Size.Large,
                        state = if (stars > 0) ButtonProperties.State.Default else ButtonProperties.State.Disabled,
                    ),
                onClick = { onSubmit(stars) },
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
            PlayroomGhostButton(
                label = stringResource(Res.string.settings_rate_later),
                onClick = onDismiss,
            )
        }
    }
}

@Composable
private fun InviteFriendSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onCopy: () -> Unit,
) {
    BooklySheet(visible = visible, onDismiss = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SheetIconBadge(icon = Icons.Gift, background = Color(0xFFE3F2FD), tint = Color(0xFF005E9F))
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
            Text(
                text = stringResource(Res.string.settings_invite_title),
                style = TokenProvider.textStyles.title.copy(fontSize = 24.sp),
                color = TokenProvider.colors.text,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.xs))
            Text(
                text = stringResource(Res.string.settings_invite_body),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.lg - TokenProvider.spacings.xxs))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                        .background(TokenProvider.colors.bgElevated)
                        .padding(TokenProvider.spacings.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
            ) {
                Icon(
                    painter = painterResource(Icons.Link.icon),
                    contentDescription = null,
                    tint = TokenProvider.colors.textMuted,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = stringResource(Res.string.settings_invite_link),
                    modifier = Modifier.weight(1f),
                    style = TokenProvider.textStyles.bodyStrong.copy(fontSize = TokenProvider.fontSizes.caption),
                    color = TokenProvider.colors.text,
                    maxLines = 1,
                )
                Box(
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .background(TokenProvider.colors.text)
                            .clickable(onClick = onCopy)
                            .padding(
                                horizontal = TokenProvider.spacings.sm,
                                vertical = TokenProvider.spacings.xs - 2.dp,
                            ),
                ) {
                    Text(
                        text = stringResource(Res.string.settings_invite_copy),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TokenProvider.colors.textInverse,
                    )
                }
            }
            Spacer(modifier = Modifier.height(TokenProvider.spacings.lg - TokenProvider.spacings.xxs))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
            ) {
                InviteShareOption(
                    icon = Icons.Mail,
                    iconTint = Color(0xFF874E00),
                    label = stringResource(Res.string.settings_invite_email),
                    iconBackground = Color(0xFFFFE0B2),
                    onClick = onCopy,
                    modifier = Modifier.weight(1f),
                )
                InviteShareOption(
                    icon = Icons.Chat,
                    iconTint = Color(0xFF006B1B),
                    label = stringResource(Res.string.settings_invite_message),
                    iconBackground = Color(0xFFC8E6C9),
                    onClick = onCopy,
                    modifier = Modifier.weight(1f),
                )
                InviteShareOption(
                    icon = Icons.Share,
                    iconTint = Color(0xFF005E9F),
                    label = stringResource(Res.string.settings_invite_more),
                    iconBackground = Color(0xFFE3F2FD),
                    onClick = onCopy,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(TokenProvider.spacings.lg - TokenProvider.spacings.xxs))
            PlayroomGhostButton(
                label = stringResource(Res.string.settings_invite_close),
                onClick = onDismiss,
            )
        }
    }
}

@Composable
private fun InviteShareOption(
    icon: Icons,
    iconTint: Color,
    label: String,
    iconBackground: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgElevated)
                .clickable(onClick = onClick)
                .padding(vertical = TokenProvider.spacings.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
    ) {
        Box(
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon.icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TokenProvider.colors.text,
        )
    }
}

private data class LanguageOption(
    val nativeName: String,
    val flag: String,
)

private val languageOptions =
    listOf(
        LanguageOption("English", "🇬🇧"),
        LanguageOption("Español", "🇪🇸"),
        LanguageOption("Français", "🇫🇷"),
        LanguageOption("Deutsch", "🇩🇪"),
        LanguageOption("Italiano", "🇮🇹"),
        LanguageOption("Português", "🇵🇹"),
        LanguageOption("Română", "🇷🇴"),
        LanguageOption("日本語", "🇯🇵"),
    )

@Composable
private fun LanguageSheet(
    visible: Boolean,
    selectedLanguage: String,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit,
) {
    BooklySheet(visible = visible, onDismiss = onDismiss) {
        Text(
            text = stringResource(Res.string.settings_language_title),
            modifier = Modifier.fillMaxWidth(),
            style = TokenProvider.textStyles.title.copy(fontSize = 22.sp),
            color = TokenProvider.colors.text,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xxs))
        Text(
            text = stringResource(Res.string.settings_language_subtitle),
            modifier = Modifier.fillMaxWidth(),
            style = TokenProvider.textStyles.body.copy(fontSize = TokenProvider.fontSizes.caption),
            color = TokenProvider.colors.textMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.md + TokenProvider.spacings.xxs))
        languageOptions.forEach { option ->
            val active = option.nativeName == selectedLanguage
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = TokenProvider.spacings.xs)
                        .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                        .background(
                            if (active) {
                                TokenProvider.colors.borderAccent.copy(alpha = 0.2f)
                            } else {
                                TokenProvider.colors.bgElevated
                            },
                        )
                        .clickable { onLanguageSelected(option.nativeName) }
                        .padding(TokenProvider.spacings.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
            ) {
                Text(text = option.flag, fontSize = 24.sp)
                Text(
                    text = option.nativeName,
                    modifier = Modifier.weight(1f),
                    style = TokenProvider.textStyles.bodyStrong,
                    color = TokenProvider.colors.text,
                )
                if (active) {
                    Icon(
                        painter = painterResource(Icons.CheckCircle.icon),
                        contentDescription = null,
                        tint = TokenProvider.colors.textAccent,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun LogoutConfirmSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    BooklySheet(visible = visible, onDismiss = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEBEE)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Icons.SettingsLogout.icon),
                    contentDescription = null,
                    tint = TokenProvider.colors.textDanger,
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
            Text(
                text = stringResource(Res.string.settings_logout_title),
                style = TokenProvider.textStyles.title.copy(fontSize = 24.sp),
                color = TokenProvider.colors.text,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.xs))
            Text(
                text = stringResource(Res.string.settings_logout_body),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.lg))
            Button(
                properties =
                    ButtonProperties(
                        label = stringResource(Res.string.settings_logout_confirm),
                        size = ButtonProperties.Size.Large,
                    ),
                onClick = onConfirm,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
            PlayroomGhostButton(
                label = stringResource(Res.string.settings_logout_cancel),
                onClick = onDismiss,
            )
        }
    }
}

@Composable
private fun SheetIconBadge(
    icon: Icons,
    background: Color,
    tint: Color,
) {
    Box(
        modifier =
            Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(background),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon.icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
internal fun SheetTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minHeight: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .background(
                    color = TokenProvider.colors.bgElevated,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.md),
                ),
        textStyle =
            TokenProvider.textStyles.input.copy(
                color = TokenProvider.colors.text,
            ),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.padding(TokenProvider.spacings.sm)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TokenProvider.textStyles.input,
                        color = TokenProvider.colors.textMuted.copy(alpha = 0.7f),
                    )
                }
                innerTextField()
            }
        },
    )
}


// === Rows =================================================================

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
                    .background(item.style.background),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(item.icon.icon),
                contentDescription = null,
                tint = item.style.tint,
                modifier = Modifier.size(22.dp),
            )
        }
        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            style = TokenProvider.textStyles.bodyStrong,
            color = TokenProvider.colors.text,
        )
        if (item.trailingText != null) {
            Text(
                text = item.trailingText,
                style = TokenProvider.textStyles.eyebrow.copy(fontWeight = FontWeight.Bold),
                color = TokenProvider.colors.textMuted,
            )
        }
        Icon(
            painter = painterResource(Icons.SettingsChevron.icon),
            contentDescription = null,
            tint = TokenProvider.colors.textSubtle.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp),
        )
    }
}

private data class SettingsRowItem(
    val title: String,
    val icon: Icons,
    val style: SettingsRowStyle,
    val trailingText: String? = null,
    val onClick: () -> Unit,
)

/** Icon tint and badge fill for a settings row. */
private data class SettingsRowStyle(
    val tint: Color,
    val background: Color,
)

/**
 * The five row palettes used by the prototype's settings menu. Each badge is the
 * icon's own colour at 10% opacity, except Help, which sits on soft amber.
 */
private object SettingsRowStyles {
    val Info = SettingsRowStyle(tint = Color(0xFF005E9F), background = Color(0x1A005E9F))
    val Accent = SettingsRowStyle(tint = Color(0xFF874E00), background = Color(0x1A874E00))
    val Danger = SettingsRowStyle(tint = Color(0xFFB02500), background = Color(0x1AB02500))
    val Success = SettingsRowStyle(tint = Color(0xFF006B1B), background = Color(0x1A006B1B))
    val Help = SettingsRowStyle(tint = Color(0xFF874E00), background = Color(0x33FFC107))
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

// === Preview seams ========================================================

@Composable
internal fun SettingsScreenContent(
    state: SettingsViewState,
    onIntent: (SettingsIntent) -> Unit = {},
    onClose: () -> Unit = {},
) {
    SettingsScreen(
        state = state,
        onIntent = onIntent,
        onClose = onClose,
    )
}
