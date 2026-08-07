package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.components.Feedback
import org.evolutionsoftware.bookly.design.components.properties.FeedbackAction
import org.evolutionsoftware.bookly.design.components.properties.FeedbackProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun DebugLoadingScreen(onClose: () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugScreenHeader(title = "Loading State", onClose = onClose)

        Feedback(
            properties = FeedbackProperties.Loading,
            title = "Opening the playroom",
            description = "Just a moment while we gather the books.",
            modifier = Modifier.padding(top = 80.dp),
        )
    }
}

@Composable
fun DebugEmptyScreen(onClose: () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugScreenHeader(title = "Empty State", onClose = onClose)

        Feedback(
            properties =
                FeedbackProperties.Empty(
                    mainAction = FeedbackAction(text = "Back to the playroom", onClick = {}),
                ),
            title = "The playroom is empty",
            description = "New books will show up here as soon as they arrive.",
            modifier = Modifier.padding(top = 80.dp),
        )
    }
}

@Composable
fun DebugErrorScreen(onClose: () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugScreenHeader(title = "Error State", onClose = onClose)

        Feedback(
            properties =
                FeedbackProperties.Error(
                    mainAction = FeedbackAction(text = "Try again", onClick = {}),
                ),
            title = "Wait a minute…",
            description = "We hit a small snag loading the books. Check your connection and give it another go.",
            modifier = Modifier.padding(top = 80.dp),
        )
    }
}

