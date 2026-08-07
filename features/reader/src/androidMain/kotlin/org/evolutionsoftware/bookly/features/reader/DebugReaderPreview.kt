package org.evolutionsoftware.bookly.features.reader

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.evolutionsoftware.bookly.design.theme.bookly.BooklyTheme
import org.evolutionsoftware.bookly.features.reader.debug.DebugReaderRoute

@Preview(showBackground = true, showSystemUi = true, name = "Debug reader - 20 animal pages")
@Composable
private fun DebugReaderPreview() {
    BooklyTheme {
        DebugReaderRoute(onBack = {})
    }
}
