package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.evolutionsoftware.bookly.design.components.SecureTextField
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun DebugTextFieldsScreen(onClose: () -> Unit) {
    val defaultState = rememberTextFieldState()
    val emailState = rememberTextFieldState()
    val filledState = rememberTextFieldState("John Doe")
    val passwordState = rememberTextFieldState("password123")
    val errorState = rememberTextFieldState("Invalid input")
    val disabledEmptyState = rememberTextFieldState()
    val disabledFilledState = rememberTextFieldState("This value cannot be changed")
    val noLabelState = rememberTextFieldState()
    var isPasswordRevealed by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugScreenHeader(title = "Text Fields", onClose = onClose)

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TokenProvider.spacings.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.lg),
        ) {
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))

            DebugSectionTitle("Default - Empty")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Full Name",
                        placeholder = "Enter your full name",
                    ),
                state = defaultState,
            )

            DebugSectionTitle("Default - With Placeholder")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Email Address",
                        placeholder = "example@email.com",
                    ),
                state = emailState,
            )

            DebugSectionTitle("Filled")
            TextField(
                properties = TextFieldProperties(label = "Name"),
                state = filledState,
            )

            DebugSectionTitle("Password Field")
            SecureTextField(
                properties =
                    TextFieldProperties(
                        label = "Password",
                        placeholder = "Enter password",
                    ),
                state = passwordState,
                isRevealed = isPasswordRevealed,
                suffix = {
                    Text(
                        text = if (isPasswordRevealed) "Hide" else "Show",
                        style = TokenProvider.textStyles.label,
                        color = TokenProvider.colors.textMuted,
                        modifier =
                            Modifier.padding(end = TokenProvider.spacings.xs)
                                .clickable { isPasswordRevealed = !isPasswordRevealed },
                    )
                },
            )

            DebugSectionTitle("Error State")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Username",
                        state = TextFieldProperties.State.Error,
                    ),
                state = errorState,
            )

            DebugSectionTitle("Disabled - Empty")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Disabled Field",
                        placeholder = "Cannot edit this",
                        state = TextFieldProperties.State.Disabled,
                    ),
                state = disabledEmptyState,
                enabled = false,
            )

            DebugSectionTitle("Disabled - With Value")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Read Only",
                        state = TextFieldProperties.State.Disabled,
                    ),
                state = disabledFilledState,
                enabled = false,
            )

            DebugSectionTitle("No Label")
            TextField(
                properties =
                    TextFieldProperties(
                        placeholder = "Search...",
                    ),
                state = noLabelState,
            )

            Spacer(modifier = Modifier.height(TokenProvider.spacings.xxl))
        }
    }
}

@Composable
private fun DebugSectionTitle(title: String) {
    Text(
        text = title,
        style = TokenProvider.textStyles.label,
        color = TokenProvider.colors.textMuted,
    )
}
