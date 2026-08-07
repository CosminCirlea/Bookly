package org.evolutionsoftware.bookly.features.auth.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.onboarding_card_animals
import bookly.features.auth.generated.resources.onboarding_card_bedtime
import bookly.features.auth.generated.resources.onboarding_card_colors
import bookly.features.auth.generated.resources.onboarding_card_first_book
import bookly.features.auth.generated.resources.onboarding_card_on_device
import bookly.features.auth.generated.resources.onboarding_favorites_body
import bookly.features.auth.generated.resources.onboarding_favorites_title
import bookly.features.auth.generated.resources.onboarding_next
import bookly.features.auth.generated.resources.onboarding_offline_body
import bookly.features.auth.generated.resources.onboarding_offline_title
import bookly.features.auth.generated.resources.onboarding_register
import bookly.features.auth.generated.resources.onboarding_skip
import bookly.features.auth.generated.resources.onboarding_start
import bookly.features.auth.generated.resources.onboarding_welcome_body
import bookly.features.auth.generated.resources.onboarding_welcome_title
import org.evolutionsoftware.bookly.components.ui.PlayroomGhostButton
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.components.Button
import org.evolutionsoftware.bookly.design.components.properties.ButtonProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingRoute(
    onDone: () -> Unit,
    onRegister: () -> Unit,
) {
    var step by remember { mutableIntStateOf(0) }
    val totalSteps = 3
    val isLast = step == totalSteps - 1

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TokenProvider.spacings.horizontalSpacing,
                        vertical = TokenProvider.spacings.sm,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs - 2.dp),
            ) {
                repeat(totalSteps) { index ->
                    Box(
                        modifier =
                            Modifier
                                .height(8.dp)
                                .width(if (index == step) 24.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == step) {
                                        TokenProvider.colors.bgAccent
                                    } else {
                                        TokenProvider.colors.text.copy(alpha = 0.15f)
                                    },
                                ),
                    )
                }
            }
            Text(
                text = stringResource(Res.string.onboarding_skip),
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onDone)
                        .padding(
                            horizontal = TokenProvider.spacings.sm,
                            vertical = TokenProvider.spacings.xs - 2.dp,
                        ),
                style = TokenProvider.textStyles.bodyStrong.copy(fontSize = TokenProvider.fontSizes.caption),
                color = TokenProvider.colors.textMuted,
            )
        }

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = TokenProvider.spacings.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .height(220.dp)
                        .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                when (step) {
                    0 -> WelcomeArt()
                    1 -> OfflineArt()
                    else -> FavoritesArt()
                }
            }
            Spacer(modifier = Modifier.height(TokenProvider.spacings.xl))
            Text(
                text =
                    stringResource(
                        when (step) {
                            0 -> Res.string.onboarding_welcome_title
                            1 -> Res.string.onboarding_offline_title
                            else -> Res.string.onboarding_favorites_title
                        },
                    ),
                style = TokenProvider.textStyles.headline.copy(fontSize = 30.sp, lineHeight = 36.sp),
                color = TokenProvider.colors.text,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
            Text(
                text =
                    stringResource(
                        when (step) {
                            0 -> Res.string.onboarding_welcome_body
                            1 -> Res.string.onboarding_offline_body
                            else -> Res.string.onboarding_favorites_body
                        },
                    ),
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = TokenProvider.spacings.horizontalSpacing,
                        vertical = TokenProvider.spacings.lg,
                    ),
            verticalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
        ) {
            Button(
                properties =
                    ButtonProperties(
                        label =
                            stringResource(
                                if (isLast) Res.string.onboarding_start else Res.string.onboarding_next,
                            ),
                        size = ButtonProperties.Size.Large,
                    ),
                onClick = { if (isLast) onDone() else step += 1 },
            )
            if (isLast) {
                PlayroomGhostButton(
                    label = stringResource(Res.string.onboarding_register),
                    onClick = onRegister,
                    icon = Icons.PersonAdd,
                )
            }
        }
    }
}

@Composable
private fun OnboardingCard(
    emoji: String,
    tileColor: Color,
    label: String,
    modifier: Modifier = Modifier,
    rotation: Float = 0f,
) {
    Column(
        modifier =
            modifier
                .rotate(rotation)
                .width(130.dp)
                .height(160.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(TokenProvider.borderRadius.lg),
                    ambientColor = Color(0x1F392E00),
                    spotColor = Color(0x1F392E00),
                )
                .clip(RoundedCornerShape(TokenProvider.borderRadius.lg))
                .background(TokenProvider.colors.bgSurface)
                .padding(TokenProvider.spacings.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(TokenProvider.borderRadius.md))
                    .background(tileColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = emoji, fontSize = 48.sp)
        }
        Spacer(modifier = Modifier.height(TokenProvider.spacings.xs))
        Text(
            text = label,
            style =
                TokenProvider.textStyles.bodyStrong.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                ),
            color = TokenProvider.colors.text,
        )
    }
}

@Composable
private fun FloatingBadge(
    icon: Icons,
    tint: Color,
    shadowColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(60.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    ambientColor = shadowColor,
                    spotColor = shadowColor,
                )
                .clip(CircleShape)
                .background(TokenProvider.colors.bgSurface),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon.icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(30.dp),
        )
    }
}

@Composable
private fun WelcomeArt() {
    Box(contentAlignment = Alignment.Center) {
        OnboardingCard(
            emoji = "🐾",
            tileColor = Color(0xFFFFE0B2),
            label = stringResource(Res.string.onboarding_card_animals),
            modifier = Modifier.offset(x = (-86).dp, y = 10.dp),
            rotation = -8f,
        )
        OnboardingCard(
            emoji = "🎨",
            tileColor = Color(0xFFF8BBD0),
            label = stringResource(Res.string.onboarding_card_colors),
            modifier = Modifier.offset(x = 86.dp, y = 10.dp),
            rotation = 8f,
        )
        OnboardingCard(
            emoji = "📖",
            tileColor = Color(0xFFBBDEFB),
            label = stringResource(Res.string.onboarding_card_first_book),
        )
    }
}

@Composable
private fun OfflineArt() {
    Box(contentAlignment = Alignment.Center) {
        OnboardingCard(
            emoji = "📖",
            tileColor = Color(0xFFC8E6C9),
            label = stringResource(Res.string.onboarding_card_on_device),
        )
        FloatingBadge(
            icon = Icons.WifiOff,
            tint = Color(0xFF006B1B),
            shadowColor = Color(0x40006B1B),
            modifier = Modifier.offset(x = 70.dp, y = (-70).dp),
        )
    }
}

@Composable
private fun FavoritesArt() {
    Box(contentAlignment = Alignment.Center) {
        OnboardingCard(
            emoji = "🌙",
            tileColor = Color(0xFFFFE0B2),
            label = stringResource(Res.string.onboarding_card_bedtime),
        )
        FloatingBadge(
            icon = Icons.HeartFilled,
            tint = TokenProvider.colors.favorite,
            shadowColor = Color(0x40E53935),
            modifier = Modifier.offset(x = 70.dp, y = (-70).dp),
        )
    }
}
