package org.evolutionsoftware.bookly.design

import bookly.design.generated.resources.Res
import bookly.design.generated.resources.ic_arrow_left
import bookly.design.generated.resources.ic_close
import bookly.design.generated.resources.ic_eye
import bookly.design.generated.resources.ic_eye_off
import bookly.design.generated.resources.ic_facebook
import bookly.design.generated.resources.ic_google
import bookly.design.generated.resources.ic_pause
import bookly.design.generated.resources.ic_play
import bookly.design.generated.resources.ic_settings
import org.jetbrains.compose.resources.DrawableResource

enum class Icons(
    val icon: DrawableResource,
) {
    ArrowLeft(Res.drawable.ic_arrow_left),
    Close(Res.drawable.ic_close),
    Eye(Res.drawable.ic_eye),
    EyeOff(Res.drawable.ic_eye_off),
    Facebook(Res.drawable.ic_facebook),
    Google(Res.drawable.ic_google),
    Pause(Res.drawable.ic_pause),
    Play(Res.drawable.ic_play),
    Settings(Res.drawable.ic_settings),
}
