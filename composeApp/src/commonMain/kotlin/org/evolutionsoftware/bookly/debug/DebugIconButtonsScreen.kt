package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.IconButton
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DebugIconButtonsScreen(onClose: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugScreenHeader(title = "Icon Buttons", onClose = onClose)

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TokenProvider.spacings.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xl),
        ) {
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))

            DebugSectionTitle("Component States")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            ) {
                IconButtonState(
                    label = "Default",
                    state = IconButtonProperties.State.Default,
                )
                IconButtonState(
                    label = "Disabled",
                    state = IconButtonProperties.State.Disabled,
                )
            }

            DebugSectionTitle("Navigation")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            ) {
                listOf(Icons.ArrowLeft, Icons.Close, Icons.Settings).forEach { icon ->
                    IconButtonWithLabel(
                        icon = icon,
                        label = icon.name,
                    )
                }
            }

            DebugSectionTitle("Playback & Favorites")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            ) {
                listOf(Icons.Play, Icons.Pause, Icons.Heart, Icons.HeartFilled).forEach { icon ->
                    IconButtonWithLabel(
                        icon = icon,
                        label = icon.name,
                    )
                }
            }

            DebugSectionTitle("Disabled Controls")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            ) {
                listOf(Icons.ArrowLeft, Icons.Close, Icons.Eye, Icons.EyeOff).forEach { icon ->
                    IconButtonWithLabel(
                        icon = icon,
                        label = icon.name,
                        state = IconButtonProperties.State.Disabled,
                    )
                }
            }

            DebugSectionTitle("All Icons")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            ) {
                Icons.entries.forEach { icon ->
                    IconButtonWithLabel(
                        icon = icon,
                        label = icon.name,
                    )
                }
            }

            Spacer(modifier = Modifier.height(TokenProvider.spacings.xxl))
        }
    }
}

@Composable
private fun IconButtonState(
    label: String,
    state: IconButtonProperties.State,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            properties =
                IconButtonProperties(
                    icon = Icons.Settings,
                    ariaLabel = label,
                    state = state,
                ),
            onClick = {},
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = TokenProvider.textStyles.label,
            color = TokenProvider.colors.textMuted,
        )
    }
}

@Composable
private fun IconButtonWithLabel(
    icon: Icons,
    label: String,
    state: IconButtonProperties.State = IconButtonProperties.State.Default,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            properties = IconButtonProperties(icon = icon, ariaLabel = label, state = state),
            onClick = {},
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = TokenProvider.textStyles.label,
            color = TokenProvider.colors.textMuted,
        )
    }
}

@Composable
private fun DebugSectionTitle(title: String) {
    Text(
        text = title,
        style = TokenProvider.textStyles.title.copy(fontWeight = FontWeight.Bold),
        color = TokenProvider.colors.text,
    )
}
