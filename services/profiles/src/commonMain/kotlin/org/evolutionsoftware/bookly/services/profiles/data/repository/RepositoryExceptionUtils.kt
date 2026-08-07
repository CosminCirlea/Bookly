package org.evolutionsoftware.bookly.services.profiles.data.repository

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import org.evolutionsoftware.bookly.services.profiles.domain.exception.ProfilesServiceException

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
        is ProfilesServiceException -> this
        is IOException ->
            ProfilesServiceException.NetworkError(
                message = "Network error: ${this.message}",
                cause = this,
            )
        else -> ProfilesServiceException.ServerError(message ?: "Unknown error")
    }

private fun HttpStatusCode.mapToDomain(): ProfilesServiceException =
    when (this) {
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden,
        -> ProfilesServiceException.Unauthorized()
        HttpStatusCode.NotFound -> ProfilesServiceException.NotFound()
        else ->
            if (value in 500..599) {
                ProfilesServiceException.ServerError()
            } else {
                ProfilesServiceException.NetworkError("Request failed with status $value")
            }
    }
