package org.evolutionsoftware.bookly.features.auth

import androidx.compose.runtime.Composable

@Composable
internal expect fun PlatformBackHandler(onBack: () -> Unit)
