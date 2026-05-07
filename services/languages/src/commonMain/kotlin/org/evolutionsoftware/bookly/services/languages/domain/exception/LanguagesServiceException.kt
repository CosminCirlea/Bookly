package org.evolutionsoftware.bookly.services.languages.domain.exception

sealed class LanguagesServiceException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class NetworkError(message: String = "Network error", cause: Throwable? = null) :
        LanguagesServiceException(message, cause)

    class Unauthorized(message: String = "Unauthorized") : LanguagesServiceException(message)

    class ServerError(message: String = "Server error") : LanguagesServiceException(message)
}
