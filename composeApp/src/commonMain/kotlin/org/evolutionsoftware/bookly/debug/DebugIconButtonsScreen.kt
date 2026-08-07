package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.IconButton
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.painterResource

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

            DebugSectionTitle("With Soft Background")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            ) {
                Icons.entries.take(8).forEach { icon ->
                    IconButtonWithLabel(
                        icon = icon,
                        label = icon.name,
                        style = IconButtonStyle.Soft,
                    )
                }
            }

            DebugSectionTitle("With Strong Background")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            ) {
                listOf(Icons.Play, Icons.Pause, Icons.Close, Icons.Settings).forEach { icon ->
                    IconButtonWithLabel(
                        icon = icon,
                        label = icon.name,
                        style = IconButtonStyle.Strong,
                    )
                }
            }

            DebugSectionTitle("Outline Style")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
                verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
            ) {
                listOf(Icons.ArrowLeft, Icons.Close, Icons.Eye, Icons.EyeOff).forEach { icon ->
                    IconButtonWithLabel(
                        icon = icon,
                        label = icon.name,
                        style = IconButtonStyle.Outline,
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
                        style = IconButtonStyle.Soft,
                    )
                }
            }

            Spacer(modifier = Modifier.height(TokenProvider.spacings.xxl))
        }
    }
}

private enum class IconButtonStyle {
    Soft,
    Strong,
    Outline,
}

@Composable
private fun IconButtonWithLabel(
    icon: Icons,
    label: String,
    style: IconButtonStyle,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            properties = IconButtonProperties(icon = icon, ariaLabel = label),
            onClick = {},
            content = {
                Box(
                    modifier =
                        Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(TokenProvider.borderRadius.pill))
                            .background(
                                when (style) {
                                    IconButtonStyle.Soft -> TokenProvider.colors.bgAccentSoft
                                    IconButtonStyle.Strong -> TokenProvider.colors.bgAccentStrong
                                    IconButtonStyle.Outline -> TokenProvider.colors.bgSurface
                                },
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(icon.icon),
                        contentDescription = label,
                        tint =
                            when (style) {
                                IconButtonStyle.Soft -> TokenProvider.colors.textAccent
                                IconButtonStyle.Strong -> TokenProvider.colors.textInverse
                                IconButtonStyle.Outline -> TokenProvider.colors.text
                            },
                        modifier = Modifier.size(24.dp),
                    )
                }
            },
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
