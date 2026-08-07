package org.evolutionsoftware.bookly.services.profiles.domain.exception

sealed class ProfilesServiceException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class NetworkError(message: String = "Network error", cause: Throwable? = null) :
        ProfilesServiceException(message, cause)

    class Unauthorized(message: String = "Unauthorized") : ProfilesServiceException(message)

    class NotFound(message: String = "Not found") : ProfilesServiceException(message)

    class ServerError(message: String = "Server error") : ProfilesServiceException(message)
}
