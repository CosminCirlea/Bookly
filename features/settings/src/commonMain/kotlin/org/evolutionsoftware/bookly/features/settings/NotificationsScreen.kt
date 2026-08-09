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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.settings.generated.resources.Res
import bookly.features.settings.generated.resources.notifications_bedtime
import bookly.features.settings.generated.resources.notifications_bedtime_sub
import bookly.features.settings.generated.resources.notifications_bedtime_time
import bookly.features.settings.generated.resources.notifications_email
import bookly.features.settings.generated.resources.notifications_email_sub
import bookly.features.settings.generated.resources.notifications_for_child
import bookly.features.settings.generated.resources.notifications_general
import bookly.features.settings.generated.resources.notifications_marketing
import bookly.features.settings.generated.resources.notifications_new_books
import bookly.features.settings.generated.resources.notifications_new_books_sub
import bookly.features.settings.generated.resources.notifications_promotions
import bookly.features.settings.generated.resources.notifications_promotions_sub
import bookly.features.settings.generated.resources.notifications_push
import bookly.features.settings.generated.resources.notifications_push_sub
import bookly.features.settings.generated.resources.notifications_save
import bookly.features.settings.generated.resources.notifications_saved
import bookly.features.settings.generated.resources.notifications_title
import bookly.features.settings.generated.resources.notifications_weekly_recap
import bookly.features.settings.generated.resources.notifications_weekly_recap_sub
import kotlinx.coroutines.launch
import org.evolutionsoftware.bookly.components.ui.BooklyToastKind
import org.evolutionsoftware.bookly.components.ui.BooklyToggle
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
fun NotificationsRoute(
    onBack: () -> Unit,
    onShowToast: (String, BooklyToastKind) -> Unit,
) {
    var push by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf(true) }
    var newBooks by remember { mutableStateOf(true) }
    var weeklyRecap by remember { mutableStateOf(false) }
    var bedtimeReminder by remember { mutableStateOf(true) }
    var bedtimeTime by remember { mutableStateOf("19:30") }
    var promotions by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        Header(
            properties = HeaderProperties(title = stringResource(Res.string.notifications_title)),
            onLeadingClick = onBack,
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = TokenProvider.spacings.horizontalSpacing,
                        vertical = TokenProvider.spacings.md,
                    ),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.lg),
        ) {
            NotificationsSection(title = stringResource(Res.string.notifications_general)) {
                ToggleRow(
                    icon = Icons.SettingsNotifications,
                    label = stringResource(Res.string.notifications_push),
                    sub = stringResource(Res.string.notifications_push_sub),
                    checked = push,
                    onCheckedChange = { push = it },
                )
                ToggleRow(
                    icon = Icons.Mail,
                    label = stringResource(Res.string.notifications_email),
                    sub = stringResource(Res.string.notifications_email_sub),
                    checked = email,
                    onCheckedChange = { email = it },
                )
            }

            NotificationsSection(title = stringResource(Res.string.notifications_for_child)) {
                ToggleRow(
                    icon = Icons.Book,
                    label = stringResource(Res.string.notifications_new_books),
                    sub = stringResource(Res.string.notifications_new_books_sub),
                    checked = newBooks,
                    onCheckedChange = { newBooks = it },
                )
                ToggleRow(
                    icon = Icons.Star,
                    label = stringResource(Res.string.notifications_weekly_recap),
                    sub = stringResource(Res.string.notifications_weekly_recap_sub),
                    checked = weeklyRecap,
                    onCheckedChange = { weeklyRecap = it },
                )
                BedtimeRow(
                    checked = bedtimeReminder,
                    onCheckedChange = { bedtimeReminder = it },
                    time = bedtimeTime,
                    onTimeChange = { bedtimeTime = it },
                )
            }

            NotificationsSection(title = stringResource(Res.string.notifications_marketing)) {
                ToggleRow(
                    icon = Icons.Offer,
                    label = stringResource(Res.string.notifications_promotions),
                    sub = stringResource(Res.string.notifications_promotions_sub),
                    checked = promotions,
                    onCheckedChange = { promotions = it },
                )
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TokenProvider.spacings.horizontalSpacing,
                        vertical = TokenProvider.spacings.md,
                    ),
        ) {
            Button(
                properties =
                    ButtonProperties(
                        label = stringResource(Res.string.notifications_save),
                        size = ButtonProperties.Size.Large,
                    ),
                onClick = {
                    scope.launch {
                        onShowToast(getString(Res.string.notifications_saved), BooklyToastKind.Success)
                    }
                    onBack()
                },
            )
        }
    }
}

@Composable
private fun NotificationsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.formGapSm)) {
        Text(
            text = title,
            modifier = Modifier.padding(start = TokenProvider.spacings.md),
            style =
                TokenProvider.textStyles.eyebrow.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp,
                ),
            color = TokenProvider.colors.textMuted,
        )
        content()
    }
}

@Composable
private fun ToggleRow(
    icon: Icons,
    label: String,
    sub: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgElevated)
                .padding(TokenProvider.spacings.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
    ) {
        Box(
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE0B2)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon.icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = TokenProvider.textStyles.bodyStrong.copy(fontSize = 15.sp),
                color = TokenProvider.colors.text,
            )
            Text(
                text = sub,
                style = TokenProvider.textStyles.body.copy(fontSize = 12.sp, lineHeight = 16.sp),
                color = TokenProvider.colors.textMuted,
            )
        }
        BooklyToggle(checked = checked, onCheckedChange = onCheckedChange)
    }
}

private val bedtimeTimes = listOf("19:00", "19:30", "20:00")

@Composable
private fun BedtimeRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    time: String,
    onTimeChange: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgElevated)
                .padding(TokenProvider.spacings.md),
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Icons.Bedtime.icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.notifications_bedtime),
                    style = TokenProvider.textStyles.bodyStrong.copy(fontSize = 15.sp),
                    color = TokenProvider.colors.text,
                )
                Text(
                    text = stringResource(Res.string.notifications_bedtime_sub),
                    style = TokenProvider.textStyles.body.copy(fontSize = 12.sp, lineHeight = 16.sp),
                    color = TokenProvider.colors.textMuted,
                )
            }
            BooklyToggle(checked = checked, onCheckedChange = onCheckedChange)
        }
        if (checked) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.notifications_bedtime_time),
                    modifier = Modifier.weight(1f),
                    style = TokenProvider.textStyles.bodyStrong.copy(fontSize = TokenProvider.fontSizes.caption),
                    color = TokenProvider.colors.text,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs)) {
                    bedtimeTimes.forEach { option ->
                        val active = option == time
                        Box(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(TokenProvider.borderRadius.sm))
                                    .background(
                                        if (active) TokenProvider.colors.borderAccent else TokenProvider.colors.bgSurface,
                                    )
                                    .clickable { onTimeChange(option) }
                                    .padding(
                                        horizontal = TokenProvider.spacings.xs,
                                        vertical = TokenProvider.spacings.xxs,
                                    ),
                        ) {
                            Text(
                                text = option,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TokenProvider.colors.text,
                            )
                        }
                    }
                }
            }
        }
    }
}
