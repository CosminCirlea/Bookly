package org.evolutionsoftware.bookly.services.languages.data.error

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import org.evolutionsoftware.bookly.services.languages.domain.exception.LanguagesServiceException

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
        is LanguagesServiceException -> this
        is IOException ->
            LanguagesServiceException.NetworkError(
                message = "Network error: ${this.message}",
                cause = this,
            )
        else -> LanguagesServiceException.ServerError(message ?: "Unknown error")
    }

private fun HttpStatusCode.mapToDomain(): LanguagesServiceException =
    when (this) {
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden,
        -> LanguagesServiceException.Unauthorized()
        else ->
            if (value in 500..599) {
                LanguagesServiceException.ServerError()
            } else {
                LanguagesServiceException.NetworkError("Request failed with status $value")
            }
    }
