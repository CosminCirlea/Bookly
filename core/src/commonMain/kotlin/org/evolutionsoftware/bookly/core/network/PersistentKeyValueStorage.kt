package org.evolutionsoftware.bookly.core.network

internal expect class PersistentKeyValueStorage() {
    fun getString(key: String): String?
    fun setString(key: String, value: String)
    fun remove(key: String)
}
