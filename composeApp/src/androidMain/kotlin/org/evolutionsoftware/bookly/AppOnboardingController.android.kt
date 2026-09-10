package org.evolutionsoftware.bookly

import android.content.Context
import java.lang.ref.WeakReference

internal actual object AppOnboardingController {
    private const val PREFERENCES_NAME = "bookly_onboarding"
    private const val COMPLETED_KEY = "completed"

    private var contextReference = WeakReference<Context>(null)

    actual fun isCompleted(): Boolean =
        contextReference
            .get()
            ?.onboardingPreferences()
            ?.getBoolean(COMPLETED_KEY, false)
            ?: false

    actual fun markCompleted() {
        contextReference
            .get()
            ?.onboardingPreferences()
            ?.edit()
            ?.putBoolean(COMPLETED_KEY, true)
            ?.apply()
    }

    fun attach(context: Context) {
        contextReference = WeakReference(context.applicationContext)
    }

    private fun Context.onboardingPreferences() =
        getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
}
