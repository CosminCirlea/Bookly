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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.settings.generated.resources.Res
import bookly.features.settings.generated.resources.contact_banner
import bookly.features.settings.generated.resources.contact_email_label
import bookly.features.settings.generated.resources.contact_email_placeholder
import bookly.features.settings.generated.resources.contact_invalid
import bookly.features.settings.generated.resources.contact_message_label
import bookly.features.settings.generated.resources.contact_message_placeholder
import bookly.features.settings.generated.resources.contact_send
import bookly.features.settings.generated.resources.contact_sent
import bookly.features.settings.generated.resources.contact_title
import bookly.features.settings.generated.resources.contact_topic_account
import bookly.features.settings.generated.resources.contact_topic_billing
import bookly.features.settings.generated.resources.contact_topic_bug
import bookly.features.settings.generated.resources.contact_topic_content
import bookly.features.settings.generated.resources.contact_topic_general
import bookly.features.settings.generated.resources.contact_topic_label
import bookly.features.settings.generated.resources.contact_topic_other
import bookly.features.settings.generated.resources.contact_topic_sheet_title
import kotlinx.coroutines.launch
import org.evolutionsoftware.bookly.components.ui.BooklySheet
import org.evolutionsoftware.bookly.components.ui.BooklyToastKind
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.Header
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.components.properties.HeaderProperties
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val MESSAGE_LIMIT = 500

@Composable
fun ContactUsRoute(
    onBack: () -> Unit,
    onShowToast: (String, BooklyToastKind) -> Unit,
) {
    val topics =
        listOf(
            stringResource(Res.string.contact_topic_general),
            stringResource(Res.string.contact_topic_bug),
            stringResource(Res.string.contact_topic_billing),
            stringResource(Res.string.contact_topic_content),
            stringResource(Res.string.contact_topic_account),
            stringResource(Res.string.contact_topic_other),
        )
    var topic by remember { mutableStateOf("") }
    val selectedTopic = topic.ifEmpty { topics.first() }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showTopics by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val emailValid = email.contains("@") && email.substringAfter("@").contains(".")
    val isValid = emailValid && message.trim().length >= 5

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        Header(
            properties = HeaderProperties(title = stringResource(Res.string.contact_title)),
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
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                        .background(TokenProvider.colors.bgElevated)
                        .padding(TokenProvider.spacings.md),
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    painter = painterResource(Icons.SettingsHelp.icon),
                    contentDescription = null,
                    tint = TokenProvider.colors.textAccent,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = stringResource(Res.string.contact_banner),
                    style = TokenProvider.textStyles.body.copy(fontSize = TokenProvider.fontSizes.caption),
                    color = TokenProvider.colors.textMuted,
                )
            }

            Column {
                FieldLabel(stringResource(Res.string.contact_topic_label))
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                            .background(TokenProvider.colors.bgElevated)
                            .clickable { showTopics = true }
                            .padding(TokenProvider.spacings.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = selectedTopic,
                        modifier = Modifier.weight(1f),
                        style = TokenProvider.textStyles.input,
                        color = TokenProvider.colors.text,
                    )
                    Icon(
                        painter = painterResource(Icons.SettingsChevron.icon),
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                        modifier =
                            Modifier
                                .size(12.dp),
                    )
                }
            }

            TextField(
                properties =
                    TextFieldProperties(
                        label = stringResource(Res.string.contact_email_label),
                        placeholder = stringResource(Res.string.contact_email_placeholder),
                    ),
                value = email,
                onValueChange = { email = it },
            )

            Column {
                SheetTextArea(
                    value = message,
                    onValueChange = { if (it.length <= MESSAGE_LIMIT) message = it },
                    label = stringResource(Res.string.contact_message_label),
                    placeholder = stringResource(Res.string.contact_message_placeholder),
                    minHeight = 130.dp,
                )
                Spacer(modifier = Modifier.height(TokenProvider.spacings.xxs))
                Text(
                    text = "${message.length}/$MESSAGE_LIMIT",
                    modifier = Modifier.padding(start = TokenProvider.spacings.xxs),
                    style = TokenProvider.textStyles.body.copy(fontSize = 12.sp),
                    color = TokenProvider.colors.textMuted,
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
                        label = stringResource(Res.string.contact_send),
                        size = ButtonProperties.Size.Large,
                        state = if (isValid) ButtonProperties.State.Default else ButtonProperties.State.Disabled,
                    ),
                onClick = {
                    scope.launch {
                        if (isValid) {
                            onShowToast(getString(Res.string.contact_sent), BooklyToastKind.Success)
                        } else {
                            onShowToast(getString(Res.string.contact_invalid), BooklyToastKind.Error)
                        }
                    }
                    if (isValid) onBack()
                },
            )
        }
    }

    BooklySheet(visible = showTopics, onDismiss = { showTopics = false }) {
        Text(
            text = stringResource(Res.string.contact_topic_sheet_title),
            modifier = Modifier.fillMaxWidth(),
            style = TokenProvider.textStyles.title,
            color = TokenProvider.colors.text,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.md))
        topics.forEach { option ->
            val active = option == selectedTopic
            Box(
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
                        .clickable {
                            topic = option
                            showTopics = false
                        }
                        .padding(TokenProvider.spacings.sm),
            ) {
                Text(
                    text = option,
                    style = TokenProvider.textStyles.bodyStrong,
                    color = TokenProvider.colors.text,
                )
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        modifier =
            Modifier.padding(
                start = TokenProvider.spacings.xxs,
                bottom = TokenProvider.spacings.xs,
            ),
        style = TokenProvider.textStyles.bodyStrong.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
        color = TokenProvider.colors.text,
    )
}
