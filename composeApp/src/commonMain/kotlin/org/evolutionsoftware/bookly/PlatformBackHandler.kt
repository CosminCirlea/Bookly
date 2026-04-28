package org.evolutionsoftware.bookly

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformBackHandler(onBack: () -> Unit)
