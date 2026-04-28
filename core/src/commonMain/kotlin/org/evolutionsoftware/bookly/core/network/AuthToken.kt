package org.evolutionsoftware.bookly.core.network

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val accessToken: String,
    val refreshToken: String? = null,
)
