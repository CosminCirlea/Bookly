package org.evolutionsoftware.bookly.features.auth

import kotlin.test.Test
import kotlin.test.assertEquals

class AuthNavigatorTest {
    @Test
    fun `leaving onboarding for authentication removes onboarding from back history`() {
        listOf(AuthDestination.SignIn, AuthDestination.SignUp).forEach { destination ->
            var exitCount = 0
            val navigator = AuthNavigator(AuthDestination.Onboarding) { exitCount++ }

            navigator.navigateTo(destination)
            navigator.navigateBack()

            assertEquals(1, exitCount, "Back from $destination should exit the flow")
            assertEquals(destination, navigator.currentDestination)
        }
    }

    @Test
    fun `nested authentication navigation cannot return to onboarding`() {
        var exitCount = 0
        val navigator = AuthNavigator(AuthDestination.Onboarding) { exitCount++ }

        navigator.navigateTo(AuthDestination.SignIn)
        navigator.navigateTo(AuthDestination.ForgotPassword)
        navigator.navigateTo(AuthDestination.ResetPassword)

        navigator.navigateBack()
        assertEquals(AuthDestination.ForgotPassword, navigator.currentDestination)
        navigator.navigateBack()
        assertEquals(AuthDestination.SignIn, navigator.currentDestination)
        assertEquals(0, exitCount)
        navigator.navigateBack()
        assertEquals(1, exitCount)
        assertEquals(AuthDestination.SignIn, navigator.currentDestination)
    }

    @Test
    fun `leaving onboarding opened inside authentication clears the old flow`() {
        var exitCount = 0
        val navigator = AuthNavigator(AuthDestination.SignIn) { exitCount++ }

        navigator.navigateTo(AuthDestination.Onboarding)
        navigator.navigateTo(AuthDestination.SignUp)
        navigator.navigateBack()

        assertEquals(1, exitCount)
        assertEquals(AuthDestination.SignUp, navigator.currentDestination)
    }

    @Test
    fun `ordinary authentication navigation keeps its back history`() {
        var exitCount = 0
        val navigator = AuthNavigator(AuthDestination.SignIn) { exitCount++ }

        navigator.navigateTo(AuthDestination.SignUp)
        navigator.navigateBack()

        assertEquals(AuthDestination.SignIn, navigator.currentDestination)
        assertEquals(0, exitCount)
        navigator.navigateBack()
        assertEquals(1, exitCount)
    }

    @Test
    fun `repeated navigation does not add duplicate destinations`() {
        var exitCount = 0
        val navigator = AuthNavigator(AuthDestination.Onboarding) { exitCount++ }

        navigator.navigateTo(AuthDestination.Onboarding)
        navigator.navigateBack()

        assertEquals(1, exitCount)
    }

    @Test
    fun `resetting authentication removes prior destinations`() {
        var exitCount = 0
        val navigator = AuthNavigator(AuthDestination.Onboarding) { exitCount++ }

        navigator.navigateTo(AuthDestination.SignUp)
        navigator.resetTo(AuthDestination.SignIn)
        navigator.navigateBack()

        assertEquals(1, exitCount)
        assertEquals(AuthDestination.SignIn, navigator.currentDestination)
    }
}
