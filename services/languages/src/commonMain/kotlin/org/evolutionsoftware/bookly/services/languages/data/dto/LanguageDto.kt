package org.evolutionsoftware.bookly.services.languages.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageDto(
    val id: Int,
    val name: String,
    @SerialName("country_code")
    val countryCode: String,
)
