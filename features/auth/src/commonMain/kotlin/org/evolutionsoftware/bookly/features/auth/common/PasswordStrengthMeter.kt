package org.evolutionsoftware.bookly.features.auth.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_password_strength_good
import bookly.features.auth.generated.resources.auth_password_strength_great
import bookly.features.auth.generated.resources.auth_password_strength_none
import bookly.features.auth.generated.resources.auth_password_strength_okay
import bookly.features.auth.generated.resources.auth_password_strength_weak
import org.evolutionsoftware.bookly.design.theme.TokenProvider
import org.jetbrains.compose.resources.stringResource

private const val BAR_COUNT = 4

/**
 * Weak to strong reads as danger -> accent -> success. The number of filled bars
 * separates "good" from "great", so both top tiers share the success tone rather
 * than introducing a fourth hue.
 */
@Composable
private fun scoreColor(score: Int): Color =
    when (score) {
        0 -> TokenProvider.colors.border
        1 -> TokenProvider.colors.textDanger
        2 -> TokenProvider.colors.warning
        else -> TokenProvider.colors.success
    }

/**
 * Scores a password from 0 to 4, one point each for reaching six characters,
 * reaching ten, containing an uppercase letter, and containing a digit or symbol.
 */
internal fun passwordStrengthScore(password: String): Int {
    var score = 0
    if (password.length >= 6) score++
    if (password.length >= 10) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isDigit() || !it.isLetterOrDigit() }) score++
    return score
}

@Composable
internal fun PasswordStrengthMeter(
    password: String,
    modifier: Modifier = Modifier,
) {
    val score = passwordStrengthScore(password)
    val scoreColor = scoreColor(score)
    val emptyColor = TokenProvider.colors.border
    val label =
        stringResource(
            when (score) {
                1 -> Res.string.auth_password_strength_weak
                2 -> Res.string.auth_password_strength_okay
                3 -> Res.string.auth_password_strength_good
                4 -> Res.string.auth_password_strength_great
                else -> Res.string.auth_password_strength_none
            },
        )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            repeat(BAR_COUNT) { index ->
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(percent = 50))
                            .background(if (index < score) scoreColor else emptyColor),
                )
            }
        }

        Text(
            text = label,
            color = scoreColor,
            style = TokenProvider.textStyles.captionStrong,
        )
    }
}
