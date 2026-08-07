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
            title = "Loading...",
            description = "Please wait while we fetch your content",
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
                    mainAction = FeedbackAction(text = "Browse Books", onClick = {}),
                ),
            title = "No Books Found",
            description = "Your library is empty. Start by adding some books to read offline.",
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
                    mainAction = FeedbackAction(text = "Try Again", onClick = {}),
                ),
            title = "Something Went Wrong",
            description = "We couldn't load your content. Please check your connection and try again.",
            modifier = Modifier.padding(top = 80.dp),
        )
    }
}

