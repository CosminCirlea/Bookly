package org.evolutionsoftware.bookly.services.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class RefreshTokenRequestDto(
    @SerialName("refresh_token")
    val refreshToken: String,
)

@Serializable
data class AuthResponseDto(
    val user: AuthUserDto,
    val session: AuthSessionDto,
)

@Serializable
data class AuthUserDto(
    val id: String,
    val email: String,
    val role: String = "user",
)

@Serializable
data class AuthSessionDto(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("token_type")
    val tokenType: String = "Bearer",
)
