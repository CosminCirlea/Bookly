package org.evolutionsoftware.bookly.core.mvi.plugin

import org.evolutionsoftware.bookly.core.mvi.UserIntentAction

fun interface MviPlugin<Action : UserIntentAction> {
    fun onUserIntentAction(action: Action)
}
