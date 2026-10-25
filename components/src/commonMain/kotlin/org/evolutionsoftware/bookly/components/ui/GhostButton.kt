package org.evolutionsoftware.bookly.components.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties

@Composable
fun PlayroomGhostButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        properties =
            ButtonProperties(
                label = label,
                size = ButtonProperties.Size.Large,
                variant = ButtonProperties.Variant.Outlined,
            ),
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
fun PlayroomPrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        properties =
            ButtonProperties(
                label = label,
                size = ButtonProperties.Size.Large,
                variant = ButtonProperties.Variant.Primary,
            ),
        onClick = onClick,
        modifier = modifier,
    )
}
