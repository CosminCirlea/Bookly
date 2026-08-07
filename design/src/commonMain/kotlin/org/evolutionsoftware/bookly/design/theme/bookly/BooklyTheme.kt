package org.evolutionsoftware.bookly.design.theme.bookly

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.evolutionsoftware.bookly.design.theme.FrameDesignSystem
import org.evolutionsoftware.bookly.design.theme.ThemeBorderRadius
import org.evolutionsoftware.bookly.design.theme.ThemeBorderWidth
import org.evolutionsoftware.bookly.design.theme.ThemeColors
import org.evolutionsoftware.bookly.design.theme.ThemeFontSizes
import org.evolutionsoftware.bookly.design.theme.ThemeLineHeights
import org.evolutionsoftware.bookly.design.theme.ThemeProvider
import org.evolutionsoftware.bookly.design.theme.ThemeSizing
import org.evolutionsoftware.bookly.design.theme.ThemeSpacing
import org.evolutionsoftware.bookly.design.theme.ThemeTextStyles

private object BooklyTokens : ThemeProvider {
    override val colors =
        ThemeColors(
            bgBase = Color(0xFFFFF6E1),
            bgSurface = Color(0xFFFFFFFF),
            bgElevated = Color(0xFFFFF0C4),
            bgAccent = Color(0xFF1CB0F6),
            bgAccentPressed = Color(0xFF1899D6),
            bgAccentSoft = Color(0xFFFAE18C),
            bgAccentStrong = Color(0xFF005E9F),
            bgSuccessSoft = Color(0xFF91F78E),
            bgInfoSoft = Color(0x3344A5FF),
            bgWarningSoft = Color(0x1AFFC791),
            bgDangerSoft = Color(0x22F95630),
            text = Color(0xFF392E00),
            textMuted = Color(0xFF695B23),
            textSubtle = Color(0xFF86763B),
            textInverse = Color(0xFFFFFFFF),
            textAccent = Color(0xFF874E00),
            textBrand = Color(0xFF1CB0F6),
            textSuccess = Color(0xFF005E17),
            textDanger = Color(0xFFB02500),
            border = Color(0xFFF5DC81),
            borderAccent = Color(0xFFFFC107),
            success = Color(0xFF22A447),
            warning = Color(0xFFF95630),
            favorite = Color(0xFFE53935),
            socialGoogle = Color(0xFF3C4043),
            socialFacebook = Color(0xFF1877F2),
        )

    override val borderRadius =
        ThemeBorderRadius(
            sm = 12.dp,
            md = 16.dp,
            lg = 24.dp,
            xl = 48.dp,
            pill = 999.dp,
        )

    override val sizing =
        ThemeSizing(
            toolbarHeight = 80.dp,
            bookCardHeight = 210.dp,
            detailCardHeight = 468.dp,
        )

    override val spacings =
        ThemeSpacing(
            xxs = 4.dp,
            xs = 8.dp,
            sm = 12.dp,
            md = 16.dp,
            lg = 24.dp,
            xl = 32.dp,
            xxl = 40.dp,
            horizontalSpacing = 24.dp,
            formGapSm = 8.dp,
            formGapMd = 16.dp,
            formGapLg = 24.dp,
            sectionGap = 32.dp,
            screenBottomSpacing = 48.dp,
        )

    override val fontSizes =
        ThemeFontSizes(
            caption = 14.sp,
            body = 16.sp,
            title = 24.sp,
            headline = 34.sp,
        )

    override val lineHeights =
        ThemeLineHeights(
            body = 24.sp,
            title = 32.sp,
            headline = 40.sp,
        )

    override val borderWidths =
        ThemeBorderWidth(
            regular = 1.dp,
            strong = 2.dp,
        )

    override val textStyles =
        ThemeTextStyles(
            eyebrow =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                ),
            label =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSizes.body,
                    lineHeight = lineHeights.body,
                ),
            body =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = fontSizes.body,
                    lineHeight = lineHeights.body,
                ),
            bodyStrong =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSizes.body,
                    lineHeight = lineHeights.body,
                ),
            input =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = fontSizes.body,
                    lineHeight = lineHeights.body,
                ),
            title =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                ),
            headline =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = fontSizes.headline,
                    lineHeight = lineHeights.headline,
                ),
            button =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                ),
        )
}

@Composable
fun BooklyTheme(content: @Composable () -> Unit) {
    FrameDesignSystem(
        themeProvider = BooklyTokens,
        content = content,
    )
}
