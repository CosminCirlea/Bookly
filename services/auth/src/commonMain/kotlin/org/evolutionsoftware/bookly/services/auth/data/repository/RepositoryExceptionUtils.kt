package org.evolutionsoftware.bookly.services.auth.data.repository

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import org.evolutionsoftware.bookly.services.auth.domain.exception.AuthServiceException

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
        is AuthServiceException -> this
        is IOException ->
            AuthServiceException.NetworkError(
                message = "Network error: ${this.message}",
                cause = this,
            )
        else -> AuthServiceException.ServerError(message ?: "Unknown error")
    }

private fun HttpStatusCode.mapToDomain(): AuthServiceException =
    when (this) {
        HttpStatusCode.BadRequest -> AuthServiceException.ValidationError("Invalid request data")
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden,
        -> AuthServiceException.Unauthorized()
        HttpStatusCode.NotFound -> AuthServiceException.NotFound()
        else ->
            if (value in 500..599) {
                AuthServiceException.ServerError()
            } else {
                AuthServiceException.NetworkError("Request failed with status $value")
            }
    }
