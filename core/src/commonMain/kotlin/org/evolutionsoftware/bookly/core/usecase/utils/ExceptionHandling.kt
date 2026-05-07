package org.evolutionsoftware.bookly.core.usecase.utils

import kotlinx.coroutines.CancellationException
import org.evolutionsoftware.bookly.core.logging.Logger

inline fun <T, E> withExceptionHandling(
    crossinline errorMapper: (Exception) -> E,
    block: () -> T,
): Result<T, E> =
    try {
        Result.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Logger.withTag("UseCase").e("Error in use case: ${e.message}", e)
        Result.Error(errorMapper(e))
    }
