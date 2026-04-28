package org.evolutionsoftware.bookly.core.logging

class Logger private constructor(
    private val tag: String,
) {
    fun d(message: String) {
        println("D/$tag: $message")
    }

    fun e(
        message: String,
        throwable: Throwable,
    ) {
        println("E/$tag: $message\n${throwable.message}")
    }

    companion object {
        fun withTag(tag: String): Logger = Logger(tag)
    }
}
