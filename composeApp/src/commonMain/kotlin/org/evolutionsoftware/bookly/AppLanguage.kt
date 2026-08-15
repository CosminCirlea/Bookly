package org.evolutionsoftware.bookly

internal enum class AppLanguage(
    val languageTag: String,
) {
    English("en"),
    Romanian("ro"),
    ;

    companion object {
        fun fromLanguageTag(languageTag: String): AppLanguage =
            if (languageTag.substringBefore('-').lowercase() == Romanian.languageTag) {
                Romanian
            } else {
                English
            }
    }
}

internal expect object AppLocaleController {
    fun currentLanguageTag(): String

    fun setLanguageTag(languageTag: String)
}
