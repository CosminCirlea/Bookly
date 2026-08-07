package org.evolutionsoftware.bookly.features.reader.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.evolutionsoftware.bookly.features.reader.ReaderAction
import org.evolutionsoftware.bookly.features.reader.ReaderIntent
import org.evolutionsoftware.bookly.features.reader.ReaderScreenContent
import org.evolutionsoftware.bookly.features.reader.ReaderStateMapper
import org.evolutionsoftware.bookly.features.reader.ReaderViewState

/**
 * Reader driven by [MockAnimalBook] instead of the catalog API, for the debug menu.
 *
 * Intents are folded through the real [ReaderStateMapper] so paging, autoplay and
 * favouriting behave exactly as they do in the production route.
 */
@Composable
fun DebugReaderRoute(onBack: () -> Unit) {
    val stateMapper = remember { ReaderStateMapper() }
    var state by remember {
        mutableStateOf(
            ReaderViewState(
                isLoading = false,
                book = MockAnimalBook.book,
                currentPage = 0,
            ),
        )
    }

    ReaderScreenContent(
        state = state,
        onIntent = { intent ->
            debugAction(intent)?.let { action -> state = stateMapper(action, state) }
        },
        onBack = onBack,
    )
}

private fun debugAction(intent: ReaderIntent): ReaderAction? =
    when (intent) {
        is ReaderIntent.PageChanged -> ReaderAction.CurrentPageUpdated(intent.page)
        is ReaderIntent.AutoplayToggled -> ReaderAction.AutoplayUpdated(intent.isEnabled)
        is ReaderIntent.AutoplayAdvanceRequested ->
            if (intent.currentPage >= intent.totalPages - 1) {
                ReaderAction.AutoplayUpdated(isEnabled = false)
            } else {
                ReaderAction.CurrentPageUpdated(intent.currentPage + 1)
            }
        is ReaderIntent.FavoriteToggled -> ReaderAction.FavoriteUpdated(intent.makeFavorite)
        is ReaderIntent.Load -> null
    }
