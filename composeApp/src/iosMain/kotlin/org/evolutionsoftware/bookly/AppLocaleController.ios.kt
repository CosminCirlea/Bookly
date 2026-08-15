package org.evolutionsoftware.bookly

import platform.Foundation.*

internal actual object AppLocaleController {
    private const val LANGUAGE_TAG_KEY = "bookly.language_tag"
    private const val APPLE_LANGUAGES_KEY = "AppleLanguages"

    actual fun currentLanguageTag(): String {
        val storedLanguage = NSUserDefaults.standardUserDefaults.stringForKey(LANGUAGE_TAG_KEY)
        val systemLanguage = NSLocale.preferredLanguages.firstOrNull() as? String
        return AppLanguage.fromLanguageTag(storedLanguage ?: systemLanguage.orEmpty()).languageTag
    }

    actual fun setLanguageTag(languageTag: String) {
        val supportedLanguage = AppLanguage.fromLanguageTag(languageTag).languageTag
        NSUserDefaults.standardUserDefaults.setObject(supportedLanguage, forKey = LANGUAGE_TAG_KEY)
        NSUserDefaults.standardUserDefaults.setObject(listOf(supportedLanguage), forKey = APPLE_LANGUAGES_KEY)
    }
}
