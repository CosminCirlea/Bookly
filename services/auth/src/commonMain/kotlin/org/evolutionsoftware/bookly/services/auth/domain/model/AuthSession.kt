package org.evolutionsoftware.bookly.services.auth.domain.model

data class AuthUser(
    val id: String,
    val email: String,
    val role: String,
)

data class AuthSession(
    val user: AuthUser,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
)
