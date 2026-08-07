package org.evolutionsoftware.bookly.debug

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.evolutionsoftware.bookly.design.theme.bookly.BooklyTheme

@Preview(showBackground = true, showSystemUi = true, name = "Debug menu")
@Composable
private fun DebugMenuPreview() {
    BooklyTheme {
        DebugMenuScreen(
            onClose = {},
            onNavigateToButtons = {},
            onNavigateToTextFields = {},
            onNavigateToIconButtons = {},
            onNavigateToColors = {},
            onNavigateToTypography = {},
            onNavigateToReader = {},
            onNavigateToLoading = {},
            onNavigateToEmpty = {},
            onNavigateToError = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Debug - buttons")
@Composable
private fun DebugButtonsPreview() {
    BooklyTheme { DebugButtonsScreen(onClose = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Debug - text fields")
@Composable
private fun DebugTextFieldsPreview() {
    BooklyTheme { DebugTextFieldsScreen(onClose = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Debug - icon buttons")
@Composable
private fun DebugIconButtonsPreview() {
    BooklyTheme { DebugIconButtonsScreen(onClose = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Debug - colors")
@Composable
private fun DebugColorsPreview() {
    BooklyTheme { DebugColorsScreen(onClose = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Debug - typography")
@Composable
private fun DebugTypographyPreview() {
    BooklyTheme { DebugTypographyScreen(onClose = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Debug - loading")
@Composable
private fun DebugLoadingPreview() {
    BooklyTheme { DebugLoadingScreen(onClose = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Debug - empty")
@Composable
private fun DebugEmptyPreview() {
    BooklyTheme { DebugEmptyScreen(onClose = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Debug - error")
@Composable
private fun DebugErrorPreview() {
    BooklyTheme { DebugErrorScreen(onClose = {}) }
}
