package org.evolutionsoftware.bookly.components.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.painterResource

enum class BooklyToastKind {
    Success,
    Error,
    Info,
}

class BooklyToastData internal constructor(
    internal val id: Long,
    val kind: BooklyToastKind,
    val message: String,
)

class BooklyToastState {
    private var nextId = 0L
    internal val toasts = mutableStateListOf<BooklyToastData>()

    fun show(
        message: String,
        kind: BooklyToastKind = BooklyToastKind.Info,
    ) {
        toasts.add(BooklyToastData(id = nextId++, kind = kind, message = message))
    }

    fun success(message: String) = show(message, BooklyToastKind.Success)

    fun error(message: String) = show(message, BooklyToastKind.Error)

    fun info(message: String) = show(message, BooklyToastKind.Info)

    internal fun dismiss(toast: BooklyToastData) {
        toasts.remove(toast)
    }
}

@Composable
fun rememberBooklyToastState(): BooklyToastState = remember { BooklyToastState() }

/**
 * Toast host matching the Bookly prototype: stacked at the top of the screen,
 * soft tonal card with a filled status icon and bold message.
 */
@Composable
fun BooklyToastHost(
    state: BooklyToastState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = TokenProvider.spacings.md, vertical = TokenProvider.spacings.sm),
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.toasts.forEach { toast ->
            BooklyToast(
                toast = toast,
                onTimeout = { state.dismiss(toast) },
            )
        }
    }
}

private const val TOAST_DURATION_MS = 2_800L

@Composable
private fun BooklyToast(
    toast: BooklyToastData,
    onTimeout: () -> Unit,
) {
    // Drive visibility through a transition state so the toast plays its exit
    // animation before it is removed from the host.
    val visibleState = remember { MutableTransitionState(false).apply { targetState = true } }

    LaunchedEffect(toast.id) {
        delay(TOAST_DURATION_MS)
        visibleState.targetState = false
    }

    LaunchedEffect(visibleState.isIdle, visibleState.currentState) {
        if (visibleState.isIdle && !visibleState.currentState) {
            onTimeout()
        }
    }

    val (background, foreground, icon) =
        when (toast.kind) {
            BooklyToastKind.Success -> Triple(Color(0xFFE8F5E9), Color(0xFF1B5E20), Icons.CheckCircle)
            BooklyToastKind.Error -> Triple(Color(0xFFFFEBEE), Color(0xFF7F0F00), Icons.ErrorCircle)
            BooklyToastKind.Info -> Triple(Color(0xFFFFF0C4), Color(0xFF392E00), Icons.InfoCircle)
        }
    val iconTint =
        when (toast.kind) {
            BooklyToastKind.Success -> Color(0xFF43A047)
            BooklyToastKind.Error -> TokenProvider.colors.textDanger
            BooklyToastKind.Info -> TokenProvider.colors.borderAccent
        }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(TokenProvider.borderRadius.md),
                        ambientColor = Color(0x26392E00),
                        spotColor = Color(0x26392E00),
                    )
                    .background(
                        color = background,
                        shape = RoundedCornerShape(TokenProvider.borderRadius.md),
                    )
                    .padding(
                        horizontal = TokenProvider.spacings.md,
                        vertical = TokenProvider.spacings.sm,
                    ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
        ) {
            Icon(
                painter = painterResource(icon.icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = toast.message,
                modifier = Modifier.weight(1f),
                style = TokenProvider.textStyles.bodyStrong.copy(fontSize = TokenProvider.fontSizes.caption),
                color = foreground,
            )
        }
    }
}
