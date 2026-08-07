package org.evolutionsoftware.bookly.services.catalog.data.error

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import org.evolutionsoftware.bookly.services.catalog.domain.exception.CatalogServiceException

/**
 * Translation of transport failures into the domain's error vocabulary. Lives here
 * rather than beside the repository so the API client does not have to depend on it.
 */
internal inline fun <T> withExceptionWrapping(block: () -> T): T =
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        throw e.mapToDomainException()
    }

internal fun HttpResponse.requireSuccess(): HttpResponse {
    if (!status.isSuccess()) {
        throw status.mapToDomain()
    }
    return this
}

private fun Throwable.mapToDomainException(): Throwable =
    when (this) {
        is CatalogServiceException -> this
        is IOException ->
            CatalogServiceException.NetworkError(
                message = "Network error: ${this.message}",
                cause = this,
            )
        else -> CatalogServiceException.ServerError(message ?: "Unknown error")
    }

private fun HttpStatusCode.mapToDomain(): CatalogServiceException =
    when (this) {
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden,
        -> CatalogServiceException.Unauthorized()
        HttpStatusCode.NotFound -> CatalogServiceException.NotFound()
        else ->
            if (value in 500..599) {
                CatalogServiceException.ServerError()
            } else {
                CatalogServiceException.NetworkError("Request failed with status $value")
            }
    }
