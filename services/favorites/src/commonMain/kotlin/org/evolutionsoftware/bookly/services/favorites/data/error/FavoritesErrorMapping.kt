package org.evolutionsoftware.bookly.services.favorites.data.error

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import org.evolutionsoftware.bookly.services.favorites.domain.exception.FavoritesServiceException

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
        is FavoritesServiceException -> this
        is IOException ->
            FavoritesServiceException.NetworkError(
                message = "Network error: ${this.message}",
                cause = this,
            )
        else -> FavoritesServiceException.ServerError(message ?: "Unknown error")
    }

private fun HttpStatusCode.mapToDomain(): FavoritesServiceException =
    when (this) {
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden,
        -> FavoritesServiceException.Unauthorized()
        HttpStatusCode.NotFound -> FavoritesServiceException.NotFound()
        else ->
            if (value in 500..599) {
                FavoritesServiceException.ServerError()
            } else {
                FavoritesServiceException.NetworkError("Request failed with status $value")
            }
    }
