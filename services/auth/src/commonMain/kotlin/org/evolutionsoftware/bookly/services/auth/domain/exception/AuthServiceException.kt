package org.evolutionsoftware.bookly.services.auth.domain.exception

sealed class AuthServiceException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class ValidationError(
        message: String,
        val fieldErrors: Map<String, List<String>> = emptyMap(),
    ) : AuthServiceException(message)

    class NetworkError(
        message: String = "Network error",
        cause: Throwable? = null,
    ) : AuthServiceException(message, cause)

    class Unauthorized(
        message: String = "Unauthorized",
        cause: Throwable? = null,
    ) : AuthServiceException(message, cause)

    class NotFound(
        message: String = "Not found",
        cause: Throwable? = null,
    ) : AuthServiceException(message, cause)

    class ServerError(
        message: String = "Server error",
        cause: Throwable? = null,
    ) : AuthServiceException(message, cause)
}
