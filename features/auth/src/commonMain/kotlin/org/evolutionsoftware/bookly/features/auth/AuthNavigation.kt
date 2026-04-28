package org.evolutionsoftware.bookly.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

sealed interface AuthDestination {
    data object SignIn : AuthDestination

    data object SignUp : AuthDestination

    data object ChangePassword : AuthDestination

    data object ResetPassword : AuthDestination
}

internal class AuthNavigator(
    startDestination: AuthDestination,
    private val onExit: () -> Unit,
) {
    private val backStack = mutableStateListOf(startDestination)

    var currentDestination by mutableStateOf(startDestination)
        private set

    fun navigateTo(destination: AuthDestination) {
        if (backStack.lastOrNull() != destination) {
            backStack.add(destination)
        }
        currentDestination = backStack.last()
    }

    fun resetTo(destination: AuthDestination) {
        backStack.clear()
        backStack.add(destination)
        currentDestination = destination
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeLast()
            currentDestination = backStack.last()
        } else {
            onExit()
        }
    }
}

@Composable
internal fun rememberAuthNavigator(
    startDestination: AuthDestination,
    onExit: () -> Unit,
): AuthNavigator {
    val navigator = remember(onExit) { AuthNavigator(startDestination, onExit) }

    LaunchedEffect(startDestination) {
        navigator.resetTo(startDestination)
    }

    return navigator
}
