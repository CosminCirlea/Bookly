package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun DebugTypographyScreen(onClose: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugScreenHeader(title = "Typography", onClose = onClose)

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TokenProvider.spacings.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.lg),
        ) {
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))

            TypographyItem(
                name = "Headline",
                style = TokenProvider.textStyles.headline,
                sample = "The quick brown fox",
            )

            TypographyItem(
                name = "Title",
                style = TokenProvider.textStyles.title,
                sample = "The quick brown fox jumps",
            )

            TypographyItem(
                name = "Body Strong",
                style = TokenProvider.textStyles.bodyStrong,
                sample = "The quick brown fox jumps over the lazy dog",
            )

            TypographyItem(
                name = "Body",
                style = TokenProvider.textStyles.body,
                sample = "The quick brown fox jumps over the lazy dog. Pack my box with five dozen liquor jugs.",
            )

            TypographyItem(
                name = "Label",
                style = TokenProvider.textStyles.label,
                sample = "The quick brown fox jumps over the lazy dog",
            )

            TypographyItem(
                name = "Button",
                style = TokenProvider.textStyles.button,
                sample = "CONTINUE",
            )

            TypographyItem(
                name = "Input",
                style = TokenProvider.textStyles.input,
                sample = "user@example.com",
            )

            Spacer(modifier = Modifier.height(TokenProvider.spacings.xxl))
        }
    }
}

@Composable
private fun TypographyItem(
    name: String,
    style: TextStyle,
    sample: String,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                .background(TokenProvider.colors.bgSurface)
                .padding(TokenProvider.spacings.lg),
        verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
    ) {
        Text(
            text = name,
            style = TokenProvider.textStyles.label.copy(fontWeight = FontWeight.Bold),
            color = TokenProvider.colors.textAccent,
        )
        Text(
            text = sample,
            style = style,
            color = TokenProvider.colors.text,
        )
        Text(
            text = buildString {
                append("Size: ${style.fontSize}")
                style.lineHeight.let { append(" • Line: $it") }
                style.fontWeight?.let { append(" • Weight: $it") }
            },
            style = TokenProvider.textStyles.label,
            color = TokenProvider.colors.textMuted,
        )
    }
}
