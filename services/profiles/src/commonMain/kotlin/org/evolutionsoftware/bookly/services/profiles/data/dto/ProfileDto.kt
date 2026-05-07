package org.evolutionsoftware.bookly.services.profiles.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: Int,
    @SerialName("user_id")
    val userId: String,
    val name: String,
    @SerialName("date_of_birth")
    val dateOfBirth: String? = null,
    val gender: Boolean? = null,
    @SerialName("photo_url")
    val photoUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
)
