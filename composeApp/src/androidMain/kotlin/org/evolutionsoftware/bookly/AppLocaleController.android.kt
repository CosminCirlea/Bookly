package org.evolutionsoftware.bookly

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.lang.ref.WeakReference
import java.util.Locale

internal actual object AppLocaleController {
    private const val PREFERENCES_NAME = "bookly_locale"
    private const val LANGUAGE_TAG_KEY = "language_tag"

    private var activityReference = WeakReference<Activity>(null)

    actual fun currentLanguageTag(): String {
        val context = activityReference.get()?.applicationContext
        val storedLanguage = context?.languagePreferences()?.getString(LANGUAGE_TAG_KEY, null)
        return AppLanguage.fromLanguageTag(storedLanguage ?: Locale.getDefault().language).languageTag
    }

    actual fun setLanguageTag(languageTag: String) {
        val activity = activityReference.get() ?: return
        val supportedLanguage = AppLanguage.fromLanguageTag(languageTag)
        activity.applicationContext
            .languagePreferences()
            .edit()
            .putString(LANGUAGE_TAG_KEY, supportedLanguage.languageTag)
            .apply()
        activity.recreate()
    }

    fun attach(activity: Activity) {
        activityReference = WeakReference(activity)
    }

    fun localizedContext(context: Context): Context {
        val storedLanguage = context.languagePreferences().getString(LANGUAGE_TAG_KEY, null)
        val language = AppLanguage.fromLanguageTag(storedLanguage ?: Locale.getDefault().language)
        val locale = Locale.forLanguageTag(language.languageTag)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun Context.languagePreferences() =
        getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
}
