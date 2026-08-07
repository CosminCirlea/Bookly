package org.evolutionsoftware.bookly.features.auth.common

internal object PasswordValidators {
    const val MIN_LENGTH = 6

    fun isStrongEnough(password: String): Boolean = password.length >= MIN_LENGTH

    fun doPasswordsMatch(
        password: String,
        confirmation: String,
    ): Boolean = password == confirmation
}
