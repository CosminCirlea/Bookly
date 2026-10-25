package org.evolutionsoftware.bookly.design.components

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow

/**
 * Publishes edits made inside [state] to hoisted state such as an MVI view
 * model, without ever writing back into the field.
 */
@Composable
fun TextFieldChangeEffect(
    state: TextFieldState,
    onValueChange: (String) -> Unit,
) {
    // Keyed on the state alone so a new lambda instance per recomposition does
    // not restart collection and skip an edit.
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }
            .collect { currentOnValueChange(it) }
    }
}
