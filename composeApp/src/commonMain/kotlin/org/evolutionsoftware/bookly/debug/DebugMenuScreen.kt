package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun DebugMenuScreen(
    onClose: () -> Unit,
    onNavigateToButtons: () -> Unit,
    onNavigateToTextFields: () -> Unit,
    onNavigateToIconButtons: () -> Unit,
    onNavigateToColors: () -> Unit,
    onNavigateToTypography: () -> Unit,
    onNavigateToReader: () -> Unit,
    onNavigateToLoading: () -> Unit,
    onNavigateToEmpty: () -> Unit,
    onNavigateToError: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugMenuHeader(onClose = onClose)

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = TokenProvider.spacings.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
        ) {
            item { Spacer(modifier = Modifier.height(TokenProvider.spacings.sm)) }

            item { SectionTitle("Design System") }

            item {
                NavigationItem(
                    title = "Buttons",
                    subtitle = "Primary, disabled states",
                    onClick = onNavigateToButtons,
                )
            }

            item {
                NavigationItem(
                    title = "Text Fields",
                    subtitle = "Input states and variations",
                    onClick = onNavigateToTextFields,
                )
            }

            item {
                NavigationItem(
                    title = "Icon Buttons",
                    subtitle = "All icons and styles",
                    onClick = onNavigateToIconButtons,
                )
            }

            item {
                NavigationItem(
                    title = "Colors",
                    subtitle = "Color palette",
                    onClick = onNavigateToColors,
                )
            }

            item {
                NavigationItem(
                    title = "Typography",
                    subtitle = "Text styles",
                    onClick = onNavigateToTypography,
                )
            }

            item { Spacer(modifier = Modifier.height(TokenProvider.spacings.md)) }

            item { SectionTitle("Feedback States") }

            item {
                NavigationItem(
                    title = "Loading",
                    subtitle = "Lottie animation",
                    onClick = onNavigateToLoading,
                )
            }

            item {
                NavigationItem(
                    title = "Empty",
                    subtitle = "No content state",
                    onClick = onNavigateToEmpty,
                )
            }

            item {
                NavigationItem(
                    title = "Error",
                    subtitle = "Error message state",
                    onClick = onNavigateToError,
                )
            }

            item { Spacer(modifier = Modifier.height(TokenProvider.spacings.md)) }

            item { SectionTitle("Screens") }

            item {
                NavigationItem(
                    title = "Reader",
                    subtitle = "Book reader screen",
                    onClick = onNavigateToReader,
                )
            }

            item { Spacer(modifier = Modifier.height(TokenProvider.spacings.xxl)) }
        }
    }
}

@Composable
private fun DebugMenuHeader(onClose: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(TokenProvider.spacings.horizontalSpacing),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Debug Menu",
            style = TokenProvider.textStyles.headline.copy(fontWeight = FontWeight.ExtraBold),
            color = TokenProvider.colors.textAccent,
        )
        IconButton(
            properties = IconButtonProperties(icon = Icons.Close, ariaLabel = "Close"),
            onClick = onClose,
            content = {
                Icon(
                    painter = painterResource(Icons.Close.icon),
                    contentDescription = "Close",
                    tint = TokenProvider.colors.textAccent,
                )
            },
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = TokenProvider.textStyles.label.copy(fontWeight = FontWeight.Bold),
        color = TokenProvider.colors.textMuted,
        modifier = Modifier.padding(vertical = TokenProvider.spacings.xs),
    )
}

@Composable
private fun NavigationItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgSurface)
                .clickable(onClick = onClick)
                .padding(TokenProvider.spacings.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = TokenProvider.textStyles.bodyStrong,
                color = TokenProvider.colors.text,
            )
            Text(
                text = subtitle,
                style = TokenProvider.textStyles.label,
                color = TokenProvider.colors.textMuted,
            )
        }
        Icon(
            painter = painterResource(Icons.SettingsChevron.icon),
            contentDescription = null,
            tint = TokenProvider.colors.textMuted,
            modifier = Modifier.size(20.dp),
        )
    }
}
