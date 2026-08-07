package org.evolutionsoftware.bookly.features.auth.forgotpassword

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_forgot_password_description
import bookly.features.auth.generated.resources.auth_forgot_password_email_label
import bookly.features.auth.generated.resources.auth_forgot_password_email_placeholder
import bookly.features.auth.generated.resources.auth_forgot_password_submit
import bookly.features.auth.generated.resources.auth_forgot_password_title
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.features.auth.common.AuthScreenScaffold
import org.evolutionsoftware.bookly.features.auth.common.primaryButtonProperties
import org.jetbrains.compose.resources.stringResource

private val EMAIL_PATTERN = Regex(".+@.+\\..+")

@Composable
internal fun ForgotPasswordRoute(
    onBack: () -> Unit,
    onLinkSent: (String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    val isValid = EMAIL_PATTERN.matches(email.trim())

    AuthScreenScaffold(
        title = stringResource(Res.string.auth_forgot_password_title),
        onBack = onBack,
    ) {
        Text(
            text = stringResource(Res.string.auth_forgot_password_description),
            style = TokenProvider.textStyles.body,
            color = TokenProvider.colors.textMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))
        TextField(
            properties =
                TextFieldProperties(
                    label = stringResource(Res.string.auth_forgot_password_email_label),
                    placeholder = stringResource(Res.string.auth_forgot_password_email_placeholder),
                ),
            value = email,
            onValueChange = { email = it },
        )
        Spacer(modifier = Modifier.height(TokenProvider.spacings.sectionGap))
        Button(
            properties =
                primaryButtonProperties(
                    label = stringResource(Res.string.auth_forgot_password_submit),
                    enabled = isValid,
                ),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onLinkSent(email.trim()) },
        )
    }
}
