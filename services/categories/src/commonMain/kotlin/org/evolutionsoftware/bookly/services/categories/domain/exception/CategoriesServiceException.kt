package org.evolutionsoftware.bookly.services.categories.domain.exception

sealed class CategoriesServiceException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class NetworkError(message: String = "Network error", cause: Throwable? = null) :
        CategoriesServiceException(message, cause)

    class Unauthorized(message: String = "Unauthorized") : CategoriesServiceException(message)

    class ServerError(message: String = "Server error") : CategoriesServiceException(message)
}
