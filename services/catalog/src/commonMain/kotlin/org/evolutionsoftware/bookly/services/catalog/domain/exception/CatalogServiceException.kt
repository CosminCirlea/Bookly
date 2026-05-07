package org.evolutionsoftware.bookly.services.catalog.domain.exception

sealed class CatalogServiceException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class NetworkError(message: String = "Network error", cause: Throwable? = null) :
        CatalogServiceException(message, cause)

    class Unauthorized(message: String = "Unauthorized") : CatalogServiceException(message)

    class NotFound(message: String = "Not found") : CatalogServiceException(message)

    class ServerError(message: String = "Server error") : CatalogServiceException(message)
}
