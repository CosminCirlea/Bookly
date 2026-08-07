package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.IconButton
import org.evolutionsoftware.bookly.design.components.properties.IconButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.painterResource

@Composable
fun DebugScreenHeader(
    title: String,
    onClose: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(TokenProvider.spacings.horizontalSpacing),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = TokenProvider.textStyles.title.copy(fontWeight = FontWeight.Bold),
            color = TokenProvider.colors.text,
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
