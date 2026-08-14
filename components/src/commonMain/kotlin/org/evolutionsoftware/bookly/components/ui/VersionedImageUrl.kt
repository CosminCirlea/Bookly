package org.evolutionsoftware.bookly.components.ui

fun versionedImageUrl(
    url: String,
    lastUpdated: String?,
): String {
    val version = lastUpdated?.filter { it.isLetterOrDigit() }.orEmpty()
    if (version.isBlank()) return url

    val fragmentIndex = url.indexOf('#').takeIf { it >= 0 } ?: url.length
    val baseUrl = url.substring(0, fragmentIndex)
    val fragment = url.substring(fragmentIndex)
    val separator = if ('?' in baseUrl) '&' else '?'
    return "$baseUrl${separator}bookly_last_updated=$version$fragment"
}
