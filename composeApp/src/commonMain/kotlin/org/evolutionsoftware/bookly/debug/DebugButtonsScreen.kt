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
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

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

            DebugSectionTitle("Primary - Large")
            Button(
                properties = ButtonProperties(label = "Continue", size = ButtonProperties.Size.Large),
                onClick = {},
            )

            DebugSectionTitle("Primary - Medium")
            Button(
                properties = ButtonProperties(label = "Sign In", size = ButtonProperties.Size.Medium),
                onClick = {},
            )

            DebugSectionTitle("Disabled - Large")
            Button(
                properties =
                    ButtonProperties(
                        label = "Disabled Button",
                        size = ButtonProperties.Size.Large,
                        state = ButtonProperties.State.Disabled,
                    ),
                onClick = {},
            )

            DebugSectionTitle("Disabled - Medium")
            Button(
                properties =
                    ButtonProperties(
                        label = "Disabled",
                        size = ButtonProperties.Size.Medium,
                        state = ButtonProperties.State.Disabled,
                    ),
                onClick = {},
            )

            DebugSectionTitle("Various Labels")
            Button(
                properties = ButtonProperties(label = "Get Started"),
                onClick = {},
            )
            Button(
                properties = ButtonProperties(label = "Create Account"),
                onClick = {},
            )
            Button(
                properties = ButtonProperties(label = "Save Changes"),
                onClick = {},
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
