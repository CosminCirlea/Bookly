package org.evolutionsoftware.bookly

internal expect object AppOnboardingController {
    fun isCompleted(): Boolean

    fun markCompleted()
}
