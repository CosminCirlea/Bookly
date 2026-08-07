package org.evolutionsoftware.bookly.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.theme.TokenProvider

/**
 * Bottom sheet matching the Bookly prototype: cream surface, large top radius,
 * muted grab handle and a dark scrim.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooklySheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!visible) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = TokenProvider.colors.bgBase,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        scrimColor = Color.Black.copy(alpha = 0.4f),
        dragHandle = {
            Box(
                modifier =
                    Modifier
                        .padding(top = TokenProvider.spacings.md, bottom = TokenProvider.spacings.xxs)
                        .width(48.dp)
                        .height(6.dp)
                        .background(
                            color = TokenProvider.colors.textMuted.copy(alpha = 0.25f),
                            shape = CircleShape,
                        ),
            )
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = TokenProvider.spacings.lg,
                        end = TokenProvider.spacings.lg,
                        top = TokenProvider.spacings.md,
                        bottom = TokenProvider.spacings.xl,
                    )
                    .navigationBarsPadding(),
            content = content,
        )
    }
}
