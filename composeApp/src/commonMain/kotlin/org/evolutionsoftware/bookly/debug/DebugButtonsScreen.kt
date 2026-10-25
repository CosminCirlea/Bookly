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
import androidx.compose.ui.Modifier
import org.evolutionsoftware.bookly.components.ui.PlayroomGhostButton
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.evolutionsoftware.bookly.features.auth.onboarding.OnboardingActionButton
import org.evolutionsoftware.bookly.features.auth.onboarding.OnboardingGuestButton

@Composable
fun DebugButtonsScreen(onClose: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugScreenHeader(title = "Buttons", onClose = onClose)

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TokenProvider.spacings.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.lg),
        ) {
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))

            ButtonMatrix(size = ButtonProperties.Size.Large)
            ButtonMatrix(size = ButtonProperties.Size.Medium)
            ButtonMatrix(size = ButtonProperties.Size.Large, variant = ButtonProperties.Variant.Outlined)
            ButtonMatrix(size = ButtonProperties.Size.Medium, variant = ButtonProperties.Variant.Outlined)

            DebugSectionTitle("Onboarding / Secondary")
            OnboardingActionButton(
                label = "Create account",
                isPrimary = true,
                onClick = {},
            )
            OnboardingActionButton(
                label = "Log in",
                isPrimary = false,
                onClick = {},
            )
            PlayroomGhostButton(
                label = "Secondary action",
                onClick = {},
            )
            OnboardingGuestButton(
                label = "Continue as guest",
                onClick = {},
            )

            Spacer(modifier = Modifier.height(TokenProvider.spacings.xxl))
        }
    }
}

@Composable
private fun ButtonMatrix(
    size: ButtonProperties.Size,
    variant: ButtonProperties.Variant = ButtonProperties.Variant.Primary,
) {
    val name = if (variant == ButtonProperties.Variant.Primary) "Primary" else "Secondary"
    DebugSectionTitle("$name - ${size.name.lowercase().replaceFirstChar { it.uppercase() }}")
    ButtonProperties.State.entries.forEach { state ->
        Button(
            properties =
                ButtonProperties(
                    label = state.label,
                    size = size,
                    variant = variant,
                    state = state,
                ),
            onClick = {},
        )
    }
}

private val ButtonProperties.State.label: String
    get() =
        when (this) {
            ButtonProperties.State.Default -> "Default"
            ButtonProperties.State.Disabled -> "Disabled"
            ButtonProperties.State.Loading -> "Loading"
        }

@Composable
private fun DebugSectionTitle(title: String) {
    Text(
        text = title,
        style = TokenProvider.textStyles.label,
        color = TokenProvider.colors.textMuted,
    )
}
