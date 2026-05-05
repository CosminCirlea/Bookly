package org.evolutionsoftware.bookly.design

import bookly.design.generated.resources.Res
import bookly.design.generated.resources.ic_arrow_left
import bookly.design.generated.resources.ic_close
import bookly.design.generated.resources.ic_eye
import bookly.design.generated.resources.ic_eye_off
import bookly.design.generated.resources.ic_facebook
import bookly.design.generated.resources.ic_google
import bookly.design.generated.resources.ic_pause
import bookly.design.generated.resources.ic_play
import bookly.design.generated.resources.ic_settings
import bookly.design.generated.resources.ic_settings_change_password
import bookly.design.generated.resources.ic_settings_chevron
import bookly.design.generated.resources.ic_settings_contact
import bookly.design.generated.resources.ic_settings_edit_profile
import bookly.design.generated.resources.ic_settings_help
import bookly.design.generated.resources.ic_settings_invite_friend
import bookly.design.generated.resources.ic_settings_language
import bookly.design.generated.resources.ic_settings_logout
import bookly.design.generated.resources.ic_settings_notifications
import bookly.design.generated.resources.ic_settings_rate_app
import bookly.design.generated.resources.ic_settings_reset_password
import bookly.design.generated.resources.ic_settings_sound
import org.jetbrains.compose.resources.DrawableResource

enum class Icons(
    val icon: DrawableResource,
) {
    ArrowLeft(Res.drawable.ic_arrow_left),
    Close(Res.drawable.ic_close),
    Eye(Res.drawable.ic_eye),
    EyeOff(Res.drawable.ic_eye_off),
    Facebook(Res.drawable.ic_facebook),
    Google(Res.drawable.ic_google),
    Pause(Res.drawable.ic_pause),
    Play(Res.drawable.ic_play),
    Settings(Res.drawable.ic_settings),
    SettingsChangePassword(Res.drawable.ic_settings_change_password),
    SettingsChevron(Res.drawable.ic_settings_chevron),
    SettingsContact(Res.drawable.ic_settings_contact),
    SettingsEditProfile(Res.drawable.ic_settings_edit_profile),
    SettingsHelp(Res.drawable.ic_settings_help),
    SettingsInviteFriend(Res.drawable.ic_settings_invite_friend),
    SettingsLanguage(Res.drawable.ic_settings_language),
    SettingsLogout(Res.drawable.ic_settings_logout),
    SettingsNotifications(Res.drawable.ic_settings_notifications),
    SettingsRateApp(Res.drawable.ic_settings_rate_app),
    SettingsResetPassword(Res.drawable.ic_settings_reset_password),
    SettingsSound(Res.drawable.ic_settings_sound),
}
