package org.evolutionsoftware.bookly.services.profiles.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import org.evolutionsoftware.bookly.services.profiles.data.dto.ProfileDto
import org.evolutionsoftware.bookly.services.profiles.data.error.requireSuccess

class ProfilesAPI(
    private val httpClient: HttpClient,
) {
    suspend fun getProfilesByUserId(userId: String): List<ProfileDto> =
        httpClient
            .get("$PROFILES_USERS_PATH/$userId")
            .requireSuccess()
            .body()

    suspend fun createProfile(name: String, dateOfBirth: String, gender: Boolean): ProfileDto =
        httpClient
            .post(PROFILES_PATH) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("name", name)
                            append("date_of_birth", dateOfBirth)
                            append("gender", gender.toString())
                        },
                    ),
                )
            }
            .requireSuccess()
            .body()

    private companion object {
        private const val PROFILES_USERS_PATH = "api/profiles/users"
        private const val PROFILES_PATH = "api/profiles"
    }
}
