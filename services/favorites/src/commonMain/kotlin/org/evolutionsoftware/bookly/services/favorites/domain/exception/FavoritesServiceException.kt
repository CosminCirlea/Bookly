package org.evolutionsoftware.bookly.services.favorites.domain.exception

sealed class FavoritesServiceException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class NetworkError(message: String = "Network error", cause: Throwable? = null) :
        FavoritesServiceException(message, cause)

    class Unauthorized(message: String = "Unauthorized") : FavoritesServiceException(message)

    class NotFound(message: String = "Not found") : FavoritesServiceException(message)

    class ServerError(message: String = "Server error") : FavoritesServiceException(message)
}
