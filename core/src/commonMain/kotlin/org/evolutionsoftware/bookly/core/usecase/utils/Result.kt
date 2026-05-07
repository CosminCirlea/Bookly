package org.evolutionsoftware.bookly.core.usecase.utils

sealed class Result<out T, out E> {
    data class Success<T>(val data: T) : Result<T, Nothing>()
    data class Error<E>(val error: E) : Result<Nothing, E>()
}
