package org.evolutionsoftware.bookly

import platform.Foundation.NSUserDefaults

internal actual object AppOnboardingController {
    private const val COMPLETED_KEY = "bookly.onboarding_completed"

    actual fun isCompleted(): Boolean =
        NSUserDefaults.standardUserDefaults.boolForKey(COMPLETED_KEY)

    actual fun markCompleted() {
        NSUserDefaults.standardUserDefaults.setBool(true, forKey = COMPLETED_KEY)
    }
}
