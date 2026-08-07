package org.evolutionsoftware.bookly.design.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import bookly.design.generated.resources.Res
import org.evolutionsoftware.bookly.design.components.properties.FeedbackProperties
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Feedback(
    properties: FeedbackProperties,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    lottieResource: suspend () -> ByteArray = { Res.readBytes("files/loading_lottie.lottie") },
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(TokenProvider.colors.bgBase)
                .padding(horizontal = TokenProvider.spacings.horizontalSpacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.padding(bottom = TokenProvider.spacings.lg)) {
            IllustrationFromVariant(properties = properties, lottieResource = lottieResource)
        }

        Text(
            text = title,
            style = TokenProvider.textStyles.title,
            color = TokenProvider.colors.text,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = TokenProvider.spacings.sm),
        )

        if (description != null) {
            Text(
                text = description,
                style = TokenProvider.textStyles.body,
                color = TokenProvider.colors.textMuted,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .widthIn(max = 300.dp),
            )
        }

        Spacer(modifier = Modifier.height(TokenProvider.spacings.xl))

        properties.mainAction?.let { action ->
            Button(
                properties = action.toButtonProperties(),
                onClick = action.onClick,
                modifier = Modifier.widthIn(max = 280.dp),
            )
        }

        properties.secondaryAction?.let { action ->
            Spacer(Modifier.height(TokenProvider.spacings.md))
            Button(
                properties = action.toButtonProperties(),
                onClick = action.onClick,
                modifier = Modifier.widthIn(max = 280.dp),
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun IllustrationFromVariant(
    properties: FeedbackProperties,
    lottieResource: suspend () -> ByteArray,
) = when (properties) {
    is FeedbackProperties.Loading -> FeedbackLoadingIllustration(lottieResource)
    is FeedbackProperties.Custom ->
        properties.icon?.let {
            IllustrationHolder(
                icon = it,
                iconTint = properties.iconTint,
                modifier = properties.iconModifier,
            )
        }

    is FeedbackProperties.Error,
    is FeedbackProperties.Empty ->
        properties.icon?.let {
            IllustrationHolder(
                icon = it,
                iconTint = properties.iconTint,
                modifier = FeedbackIconModifier(properties.iconBackgroundTint),
            )
        }
}

@Composable
private fun IllustrationHolder(
    icon: org.evolutionsoftware.bookly.design.Icons,
    iconTint: (@Composable () -> Color)?,
    modifier: Modifier? = null,
) {
    val iconModifier = modifier ?: Modifier.size(96.dp)

    Icon(
        modifier = iconModifier,
        painter = painterResource(icon.icon),
        contentDescription = icon.name,
        tint = iconTint?.invoke() ?: LocalContentColor.current,
    )
}

@Composable
private fun FeedbackIconModifier(iconBackgroundTint: (@Composable () -> Color)?): Modifier {
    val tint = iconBackgroundTint?.invoke() ?: TokenProvider.colors.bgAccent
    return Modifier
        .size(96.dp)
        .background(tint.copy(alpha = 0.35f), CircleShape)
        .padding(12.dp)
        .background(tint, CircleShape)
        .padding(12.dp)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun FeedbackLoadingIllustration(lottieResource: suspend () -> ByteArray) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.DotLottie(lottieResource())
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE,
    )

    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter =
                rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                ),
            contentDescription = "Loading",
            modifier = Modifier.fillMaxSize(),
        )
    }
}
