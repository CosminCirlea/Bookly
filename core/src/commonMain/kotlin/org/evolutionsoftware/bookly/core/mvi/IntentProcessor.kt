package org.evolutionsoftware.bookly.core.mvi

import kotlinx.coroutines.flow.Flow

fun interface IntentProcessor<Intent : UserIntent, Action : UserIntentAction> {
    operator fun invoke(intent: Intent): Flow<Action>
}
