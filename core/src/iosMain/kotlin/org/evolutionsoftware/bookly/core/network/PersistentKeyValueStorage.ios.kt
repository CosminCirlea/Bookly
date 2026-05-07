package org.evolutionsoftware.bookly.core.network

import platform.Foundation.NSUserDefaults

internal actual class PersistentKeyValueStorage actual constructor() {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getString(key: String): String? = defaults.stringForKey(key)

    actual fun setString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }
}
