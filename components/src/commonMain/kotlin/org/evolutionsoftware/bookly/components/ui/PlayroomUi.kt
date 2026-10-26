package org.evolutionsoftware.bookly.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayroomSecondaryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .background(if (selected) TokenProvider.colors.bgAccentSoft else TokenProvider.colors.bgElevated)
                .clickable(onClick = onClick)
                .padding(
                    horizontal = TokenProvider.spacings.md + TokenProvider.spacings.xs,
                    vertical = TokenProvider.spacings.sm - TokenProvider.spacings.xxs,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = TokenProvider.textStyles.eyebrow.copy(fontWeight = FontWeight.Bold),
            color = if (selected) TokenProvider.colors.textAccent else TokenProvider.colors.textMuted,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun PlayroomSocialButton(
    label: String,
    icon: Icons,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier =
            Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(TokenProvider.colors.bgSurface)
                .border(
                    width = TokenProvider.borderWidths.strong,
                    color = TokenProvider.colors.border,
                    shape = CircleShape,
                )
                .semantics { contentDescription = label; role = Role.Button }
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon.icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(if (icon == Icons.Facebook) 28.dp else 24.dp),
        )
    }
}

@Composable
fun PlayroomDivider(label: String = "OR") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(TokenProvider.borderWidths.regular)
                    .background(TokenProvider.colors.border.copy(alpha = 0.8f)),
        )
        Text(
            text = label.uppercase(),
            style = TokenProvider.textStyles.eyebrow.copy(fontWeight = FontWeight.Bold),
            color = TokenProvider.colors.textMuted,
        )
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(TokenProvider.borderWidths.regular)
                    .background(TokenProvider.colors.border.copy(alpha = 0.8f)),
        )
    }
}

@Composable
fun PlayroomMenuItem(
    title: String,
    icon: String,
    iconBackground: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgElevated)
                .clickable(onClick = onClick)
                .padding(
                    horizontal = TokenProvider.spacings.lg - TokenProvider.spacings.xxs,
                    vertical = TokenProvider.spacings.lg - TokenProvider.spacings.xxs,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
    ) {
        Box(
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = icon,
                style = TokenProvider.textStyles.bodyStrong,
            )
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = TokenProvider.textStyles.bodyStrong,
            color = TokenProvider.colors.text,
        )
        Text(
            text = "›",
            style = TokenProvider.textStyles.title,
            color = TokenProvider.colors.textMuted.copy(alpha = 0.55f),
        )
    }
}
