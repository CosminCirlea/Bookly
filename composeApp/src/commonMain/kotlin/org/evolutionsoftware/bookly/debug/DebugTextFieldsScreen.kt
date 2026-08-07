package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.evolutionsoftware.bookly.design.components.TextField
import org.evolutionsoftware.bookly.design.components.properties.TextFieldProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun DebugTextFieldsScreen(onClose: () -> Unit) {
    var defaultValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var filledValue by remember { mutableStateOf("John Doe") }
    var passwordValue by remember { mutableStateOf("password123") }
    var errorValue by remember { mutableStateOf("Invalid input") }

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
                value = defaultValue,
                onValueChange = { defaultValue = it },
            )

            DebugSectionTitle("Default - With Placeholder")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Email Address",
                        placeholder = "example@email.com",
                    ),
                value = emailValue,
                onValueChange = { emailValue = it },
            )

            DebugSectionTitle("Filled")
            TextField(
                properties = TextFieldProperties(label = "Name"),
                value = filledValue,
                onValueChange = { filledValue = it },
            )

            DebugSectionTitle("Password Field")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Password",
                        placeholder = "Enter password",
                    ),
                value = passwordValue,
                onValueChange = { passwordValue = it },
            )

            DebugSectionTitle("Error State")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Username",
                        state = TextFieldProperties.State.Error,
                    ),
                value = errorValue,
                onValueChange = { errorValue = it },
            )

            DebugSectionTitle("Disabled - Empty")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Disabled Field",
                        placeholder = "Cannot edit this",
                        state = TextFieldProperties.State.Disabled,
                    ),
                value = "",
                onValueChange = {},
                enabled = false,
            )

            DebugSectionTitle("Disabled - With Value")
            TextField(
                properties =
                    TextFieldProperties(
                        label = "Read Only",
                        state = TextFieldProperties.State.Disabled,
                    ),
                value = "This value cannot be changed",
                onValueChange = {},
                enabled = false,
            )

            DebugSectionTitle("No Label")
            TextField(
                properties =
                    TextFieldProperties(
                        placeholder = "Search...",
                    ),
                value = "",
                onValueChange = {},
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
