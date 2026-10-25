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
            // Neutrals — warm cream
            bgBase = Color(0xFFFFF7DF),
            bgSurface = Color(0xFFFFFDF6),
            bgElevated = Color(0xFFFFF0C2),
            // Brand — orange, four steps
            bgAccent = Color(0xFFE3924B),
            bgAccentPressed = Color(0xFFCB7835),
            bgAccentSoft = Color(0xFFFFE6B5),
            bgAccentStrong = Color(0xFFAA602C),
            // Semantic surfaces — one soft tint per hue
            bgSuccessSoft = Color(0xFFE1EED2),
            bgInfoSoft = Color(0xFFE0EDF2),
            bgWarningSoft = Color(0xFFFFE5A3),
            bgDangerSoft = Color(0xFFFFDCD1),
            text = Color(0xFFE3924B),
            textMuted = Color(0xFF90623F),
            textSubtle = Color(0xFF90623F),
            textInverse = Color(0xFFFFFFFF),
            textAccent = Color(0xFF9C5523),
            textBrand = Color(0xFFE3924B),
            textSuccess = Color(0xFF4D713A),
            textDanger = Color(0xFFA94531),
            textInfo = Color(0xFF476E80),
            border = Color(0xFFF0D49A),
            borderAccent = Color(0xFFE3924B),
            bgDisabled = Color(0xFFEFE6D3),
            bgDisabledBase = Color(0xFFDED0B6),
            success = Color(0xFF4D713A),
            warning = Color(0xFFAA602C),
            favorite = Color(0xFFC76049),
            shadowSoft = Color(0x14E3924B),
            shadowStrong = Color(0x26E3924B),
            socialGoogle = Color(0xFF3C4043),
            socialFacebook = Color(0xFF1877F2),
            bgPrimaryButton = Color(0xFFFFDF8A),
            bgPrimaryButtonPressed = Color(0xFFD7A63A),
            textPrimaryButton = Color(0xFF975017),
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSizes.body,
                    lineHeight = lineHeights.body,
                ),
            body =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSizes.body,
                    lineHeight = lineHeights.body,
                ),
            title =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSizes.title,
                    lineHeight = lineHeights.title,
                ),
            headline =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
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
            actionText =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSizes.body,
                    lineHeight = lineHeights.body,
                ),
            caption =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSizes.caption,
                    lineHeight = 20.sp,
                ),
            captionStrong =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSizes.caption,
                    lineHeight = 20.sp,
                ),
            overline =
                TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    letterSpacing = 1.2.sp,
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

private object OnboardingTokens : ThemeProvider by BooklyTokens {
    override val colors =
        BooklyTokens.colors.copy(
            bgBase = Color(0xFFFFF6E1),
            bgSurface = Color(0xFFFFFFFF),
            bgElevated = Color(0xFFFFF0C4),
            bgAccent = Color(0xFFF57C16),
            bgAccentPressed = Color(0xFFD95D08),
            bgAccentSoft = Color(0xFFFFE0B2),
            bgAccentStrong = Color(0xFFA34300),
            text = Color(0xFF392E00),
            textMuted = Color(0xFF695B23),
            textSubtle = Color(0xFF86763B),
            textAccent = Color(0xFFA34300),
            textBrand = Color(0xFFF57C16),
            border = Color(0xFFF5DC81),
            borderAccent = Color(0xFFF57C16),
            bgPrimaryButton = Color(0xFFF57C16),
            bgPrimaryButtonPressed = Color(0xFFD95D08),
            textPrimaryButton = Color(0xFFFFFFFF),
            bgSecondaryButton = Color(0xFFFFE0B2),
            textSecondaryButton = Color(0xFFA34300),
        )
}

@Composable
fun BooklyOnboardingTheme(content: @Composable () -> Unit) {
    FrameDesignSystem(
        themeProvider = OnboardingTokens,
        content = content,
    )
}
