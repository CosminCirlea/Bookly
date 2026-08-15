package org.evolutionsoftware.bookly

import kotlin.test.Test
import kotlin.test.assertEquals

class AppLanguageTest {
    @Test
    fun `Romanian region tags select Romanian`() {
        assertEquals(AppLanguage.Romanian, AppLanguage.fromLanguageTag("ro-RO"))
    }

    @Test
    fun `unsupported and empty tags fall back to English`() {
        assertEquals(AppLanguage.English, AppLanguage.fromLanguageTag("fr"))
        assertEquals(AppLanguage.English, AppLanguage.fromLanguageTag(""))
    }
}
