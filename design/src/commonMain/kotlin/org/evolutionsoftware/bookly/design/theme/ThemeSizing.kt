package org.evolutionsoftware.bookly.design.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ThemeSizing(
    val toolbarHeight: Dp,
    val bookCardHeight: Dp,
    val detailCardHeight: Dp,
    val iconButtonSize: Dp = 48.dp,
)
