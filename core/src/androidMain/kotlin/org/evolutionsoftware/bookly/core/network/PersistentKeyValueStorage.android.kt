package org.evolutionsoftware.bookly.core.network

import android.content.Context
import org.evolutionsoftware.bookly.core.CoreContext

internal actual class PersistentKeyValueStorage actual constructor() {
    private val prefs by lazy {
        CoreContext.appContext.getSharedPreferences("bookly_auth", Context.MODE_PRIVATE)
    }

    actual fun getString(key: String): String? = prefs.getString(key, null)

    actual fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}
