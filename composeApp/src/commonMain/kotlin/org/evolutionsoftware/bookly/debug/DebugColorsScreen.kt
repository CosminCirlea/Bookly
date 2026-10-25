package org.evolutionsoftware.bookly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.evolutionsoftware.bookly.design.theme.TokenProvider

@Composable
fun DebugColorsScreen(onClose: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DebugScreenHeader(title = "Colors", onClose = onClose)

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TokenProvider.spacings.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.lg),
        ) {
            Spacer(modifier = Modifier.height(TokenProvider.spacings.md))

            ColorSection(
                title = "Neutrals & Brand",
                colors =
                    listOf(
                        "bgBase" to TokenProvider.colors.bgBase,
                        "bgSurface" to TokenProvider.colors.bgSurface,
                        "bgElevated" to TokenProvider.colors.bgElevated,
                        "bgAccent" to TokenProvider.colors.bgAccent,
                        "bgAccentPressed" to TokenProvider.colors.bgAccentPressed,
                        "bgAccentStrong" to TokenProvider.colors.bgAccentStrong,
                    ),
            )

            ColorSection(
                title = "Text Colors",
                colors =
                    listOf(
                        "text" to TokenProvider.colors.text,
                        "textMuted" to TokenProvider.colors.textMuted,
                        "textSubtle" to TokenProvider.colors.textSubtle,
                        "textInverse" to TokenProvider.colors.textInverse,
                        "textBrand" to TokenProvider.colors.textBrand,
                    ),
            )

            ColorSection(
                title = "Border Colors",
                colors =
                    listOf(
                        "border" to TokenProvider.colors.border,
                        "borderAccent" to TokenProvider.colors.borderAccent,
                    ),
            )

            ColorSection(
                title = "Semantic - Soft Surfaces",
                colors =
                    listOf(
                        "bgAccentSoft" to TokenProvider.colors.bgAccentSoft,
                        "bgSuccessSoft" to TokenProvider.colors.bgSuccessSoft,
                        "bgDangerSoft" to TokenProvider.colors.bgDangerSoft,
                        "bgInfoSoft" to TokenProvider.colors.bgInfoSoft,
                        "bgWarningSoft" to TokenProvider.colors.bgWarningSoft,
                    ),
            )

            ColorSection(
                title = "Semantic - Ink",
                colors =
                    listOf(
                        "textAccent" to TokenProvider.colors.textAccent,
                        "textSuccess" to TokenProvider.colors.textSuccess,
                        "textDanger" to TokenProvider.colors.textDanger,
                        "textInfo" to TokenProvider.colors.textInfo,
                        "success" to TokenProvider.colors.success,
                        "warning" to TokenProvider.colors.warning,
                        "favorite" to TokenProvider.colors.favorite,
                    ),
            )

            ColorSection(
                title = "Elevation",
                colors =
                    listOf(
                        "shadowSoft" to TokenProvider.colors.shadowSoft,
                        "shadowStrong" to TokenProvider.colors.shadowStrong,
                    ),
            )

            ColorSection(
                title = "Vendor Brands",
                colors =
                    listOf(
                        "socialGoogle" to TokenProvider.colors.socialGoogle,
                        "socialFacebook" to TokenProvider.colors.socialFacebook,
                    ),
            )

            Spacer(modifier = Modifier.height(TokenProvider.spacings.xxl))
        }
    }
}

@Composable
private fun ColorSection(
    title: String,
    colors: List<Pair<String, Color>>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm)) {
        Text(
            text = title,
            style = TokenProvider.textStyles.title.copy(fontWeight = FontWeight.Bold),
            color = TokenProvider.colors.text,
            modifier = Modifier.padding(bottom = TokenProvider.spacings.xs),
        )

        colors.forEach { (name, color) ->
            ColorRow(name = name, color = color)
        }
    }
}

@Composable
private fun ColorRow(
    name: String,
    color: Color,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(TokenProvider.borderRadius.sm))
                .background(TokenProvider.colors.bgSurface)
                .padding(TokenProvider.spacings.md),
        horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(TokenProvider.borderRadius.sm))
                    .background(color)
                    .border(
                        width = 1.dp,
                        color = TokenProvider.colors.border,
                        shape = RoundedCornerShape(TokenProvider.borderRadius.sm),
                    ),
        )
        Column {
            Text(
                text = name,
                style = TokenProvider.textStyles.bodyStrong,
                color = TokenProvider.colors.text,
            )
            Text(
                text = color.toHexString(),
                style = TokenProvider.textStyles.label,
                color = TokenProvider.colors.textMuted,
            )
        }
    }
}

private fun Color.toHexString(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    val alpha = (this.alpha * 255).toInt()
    fun Int.hex(): String = toString(16).uppercase().padStart(2, '0')
    return if (alpha == 255) {
        "#${red.hex()}${green.hex()}${blue.hex()}"
    } else {
        "#${alpha.hex()}${red.hex()}${green.hex()}${blue.hex()}"
    }
}
