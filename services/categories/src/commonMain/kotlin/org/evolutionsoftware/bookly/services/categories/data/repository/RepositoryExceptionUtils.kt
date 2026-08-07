package org.evolutionsoftware.bookly.services.categories.data.repository

import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import org.evolutionsoftware.bookly.services.categories.domain.exception.CategoriesServiceException

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
        is CategoriesServiceException -> this
        is IOException ->
            CategoriesServiceException.NetworkError(
                message = "Network error: ${this.message}",
                cause = this,
            )
        else -> CategoriesServiceException.ServerError(message ?: "Unknown error")
    }

private fun HttpStatusCode.mapToDomain(): CategoriesServiceException =
    when (this) {
        HttpStatusCode.Unauthorized,
        HttpStatusCode.Forbidden,
        -> CategoriesServiceException.Unauthorized()
        else ->
            if (value in 500..599) {
                CategoriesServiceException.ServerError()
            } else {
                CategoriesServiceException.NetworkError("Request failed with status $value")
            }
    }
