package org.evolutionsoftware.bookly.features.auth.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.onboarding_continue_guest
import bookly.features.auth.generated.resources.onboarding_create_account
import bookly.features.auth.generated.resources.onboarding_discover
import bookly.features.auth.generated.resources.onboarding_discover_body
import bookly.features.auth.generated.resources.onboarding_discover_title
import bookly.features.auth.generated.resources.onboarding_log_in
import bookly.features.auth.generated.resources.onboarding_offline
import bookly.features.auth.generated.resources.onboarding_offline_body
import bookly.features.auth.generated.resources.onboarding_offline_title
import bookly.features.auth.generated.resources.onboarding_skip
import bookly.features.auth.generated.resources.onboarding_final
import bookly.features.auth.generated.resources.onboarding_started_title
import bookly.features.auth.generated.resources.onboarding_welcome
import bookly.features.auth.generated.resources.onboarding_welcome_body
import bookly.features.auth.generated.resources.onboarding_welcome_title
import kotlinx.coroutines.launch
import org.evolutionsoftware.bookly.design.Icons
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue

private val OnboardingOrange = Color(0xFFF57C16)
private val OnboardingOrangePressed = Color(0xFFD95D08)
private val OnboardingSelectedIndicatorSize = 14.dp

private data class OnboardingPage(
    val title: StringResource,
    val body: StringResource?,
    val artwork: DrawableResource,
    val showsActions: Boolean = false,
)

@Composable
fun OnboardingRoute(
    onDone: () -> Unit,
    onRegister: () -> Unit,
    onLogin: () -> Unit,
) {
    val pages =
        listOf(
            OnboardingPage(
                title = Res.string.onboarding_welcome_title,
                body = Res.string.onboarding_welcome_body,
                artwork = Res.drawable.onboarding_welcome,
            ),
            OnboardingPage(
                title = Res.string.onboarding_discover_title,
                body = Res.string.onboarding_discover_body,
                artwork = Res.drawable.onboarding_discover,
            ),
            OnboardingPage(
                title = Res.string.onboarding_offline_title,
                body = Res.string.onboarding_offline_body,
                artwork = Res.drawable.onboarding_offline,
            ),
            OnboardingPage(
                title = Res.string.onboarding_started_title,
                body = null,
                artwork = Res.drawable.onboarding_final,
                showsActions = true,
            ),
        )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()
    val pageIndicatorInnerPadding = TokenProvider.spacings.sm
    val pageIndicatorHeight =
        OnboardingSelectedIndicatorSize +
            pageIndicatorInnerPadding * 2 +
            TokenProvider.spacings.sm +
            TokenProvider.spacings.lg

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val pageOffset =
                (
                    (pagerState.currentPage - page) +
                        pagerState.currentPageOffsetFraction
                ).coerceIn(-1f, 1f)
            OnboardingPageContent(
                page = pages[page],
                pageOffset = pageOffset,
                pageIndicatorHeight = pageIndicatorHeight,
                onDone = onDone,
                onRegister = onRegister,
                onLogin = onLogin,
            )
        }

        PageIndicator(
            pageCount = pages.size,
            currentPage = pagerState.currentPage.coerceIn(pages.indices),
            contentPadding = pageIndicatorInnerPadding,
            onPageSelected = { selectedPage ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(selectedPage)
                }
            },
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(
                        top = TokenProvider.spacings.sm,
                        bottom = TokenProvider.spacings.lg,
                    ),
        )
    }
}

@Composable
private fun OnboardingTopBar(
    skipLabel: String,
    onSkip: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = TokenProvider.spacings.horizontalSpacing,
                    vertical = TokenProvider.spacings.xs,
                ),
        horizontalArrangement = Arrangement.End,
    ) {
        Row(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(TokenProvider.colors.bgElevated)
                    .clickable(onClick = onSkip)
                    .padding(
                        start = TokenProvider.spacings.md,
                        end = TokenProvider.spacings.sm,
                        top = TokenProvider.spacings.xs,
                        bottom = TokenProvider.spacings.xs,
                    ),
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = skipLabel,
                style =
                    TokenProvider.textStyles.bodyStrong.copy(
                        fontSize = TokenProvider.fontSizes.caption,
                    ),
                color = TokenProvider.colors.text,
            )
            Icon(
                painter = painterResource(Icons.Close.icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = TokenProvider.colors.text,
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    pageOffset: Float,
    pageIndicatorHeight: Dp,
    onDone: () -> Unit,
    onRegister: () -> Unit,
    onLogin: () -> Unit,
) {
    val title = stringResource(page.title)
    val contentPadding =
        Modifier
            .navigationBarsPadding()
            .padding(bottom = pageIndicatorHeight)
    val pageVisibility = (1f - pageOffset.absoluteValue).coerceIn(0f, 1f)
    val idleTransition = rememberInfiniteTransition(label = "onboardingArtworkIdle")
    val idleProgress by
        idleTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 2_800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "onboardingArtworkIdleProgress",
        )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .clipToBounds(),
    ) {
        Image(
            painter = painterResource(page.artwork),
            contentDescription = title,
            modifier =
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val entranceScale = 1f + (0.08f * (1f - pageVisibility))
                        val idleScale = 1f + (0.008f * idleProgress * pageVisibility)
                        val scale = entranceScale * idleScale
                        alpha = 0.38f + (0.62f * pageVisibility)
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin(0.5f, 1f)
                    },
            alignment = Alignment.BottomCenter,
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .statusBarsPadding(),
        ) {
            OnboardingTopBar(
                skipLabel = stringResource(Res.string.onboarding_skip),
                onSkip = onDone,
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = TokenProvider.spacings.horizontalSpacing,
                            end = TokenProvider.spacings.horizontalSpacing,
                            top = TokenProvider.spacings.xs,
                        )
                        .graphicsLayer {
                            alpha = 0.4f + (0.6f * pageVisibility)
                            translationX = pageOffset * -28f
                            translationY = (1f - pageVisibility) * 12f
                        },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OnboardingTitle(title = title)
                Spacer(modifier = Modifier.height(TokenProvider.spacings.xs))
                page.body?.let {
                    Text(
                        text = stringResource(it),
                        style =
                            TokenProvider.textStyles.bodyStrong.copy(
                                fontSize = 18.sp,
                                lineHeight = 25.sp,
                            ),
                        color = TokenProvider.colors.text,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        if (page.showsActions) {
            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .then(contentPadding)
                        .padding(
                            horizontal = TokenProvider.spacings.horizontalSpacing,
                        )
                        .graphicsLayer {
                            alpha = 0.3f + (0.7f * pageVisibility)
                            translationY = (1f - pageVisibility) * 30f
                        },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OnboardingActionButton(
                    label = stringResource(Res.string.onboarding_create_account),
                    onClick = onRegister,
                    isPrimary = true,
                )
                Spacer(modifier = Modifier.height(TokenProvider.spacings.sm))
                OnboardingActionButton(
                    label = stringResource(Res.string.onboarding_log_in),
                    onClick = onLogin,
                    isPrimary = false,
                )
                Text(
                    text = stringResource(Res.string.onboarding_continue_guest),
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .clickable(onClick = onDone)
                            .padding(
                                start = TokenProvider.spacings.md,
                                end = TokenProvider.spacings.md,
                                top = TokenProvider.spacings.md,
                                bottom = TokenProvider.spacings.sm,
                            ),
                    style =
                        TokenProvider.textStyles.bodyStrong.copy(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    color = TokenProvider.colors.text,
                )
            }
        }
    }
}

@Composable
private fun OnboardingTitle(title: String) {
    Text(
        text = title,
        style =
            TokenProvider.textStyles.headline.copy(
                fontSize = 36.sp,
                lineHeight = 39.sp,
            ),
        color = OnboardingOrange,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    contentPadding: Dp,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                Modifier
                    .background(
                        color = OnboardingOrange.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(TokenProvider.borderRadius.pill),
                    )
                    .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(TokenProvider.spacings.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pageCount) { page ->
                Box(
                    modifier =
                        Modifier
                            .size(if (page == currentPage) OnboardingSelectedIndicatorSize else 12.dp)
                            .clip(CircleShape)
                            .background(
                                if (page == currentPage) {
                                    OnboardingOrange
                                } else {
                                    Color.White
                                },
                            )
                            .clickable { onPageSelected(page) },
                )
            }
        }
    }
}

@Composable
private fun OnboardingActionButton(
    label: String,
    onClick: () -> Unit,
    isPrimary: Boolean,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "onboardingActionScale",
    )
    val shape = RoundedCornerShape(TokenProvider.borderRadius.pill)
    val backgroundColor =
        if (isPrimary) {
            if (isPressed) OnboardingOrangePressed else OnboardingOrange
        } else {
            Color.Transparent
        }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(58.dp)
                .scale(scale)
                .clip(shape)
                .background(backgroundColor)
                .then(
                    if (isPrimary) {
                        Modifier
                    } else {
                        Modifier.border(
                            border = BorderStroke(2.dp, OnboardingOrange),
                            shape = shape,
                        )
                    },
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style =
                TokenProvider.textStyles.button.copy(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                ),
            color = if (isPrimary) Color.White else TokenProvider.colors.text,
        )
    }
}
