package org.evolutionsoftware.bookly.core.network

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: String,
    val displayName: String,
)
