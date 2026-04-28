package org.evolutionsoftware.bookly.design

import bookly.design.generated.resources.Res
import bookly.design.generated.resources.ic_arrow_left
import bookly.design.generated.resources.ic_close
import bookly.design.generated.resources.ic_settings
import org.jetbrains.compose.resources.DrawableResource

enum class Icons(
    val icon: DrawableResource,
) {
    ArrowLeft(Res.drawable.ic_arrow_left),
    Close(Res.drawable.ic_close),
    Settings(Res.drawable.ic_settings),
}
