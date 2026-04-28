package org.evolutionsoftware.bookly.services.profiles.domain.model

data class ParentProfile(
    val id: String,
    val displayName: String,
) {
    val initials: String
        get() =
            displayName
                .split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .joinToString(separator = "") { it.first().uppercase() }
                .ifBlank { "G" }
}
