package org.evolutionsoftware.bookly.features.auth.common

internal object PasswordValidators {
    fun isStrongEnough(password: String): Boolean = password.length >= 4

    fun doPasswordsMatch(
        password: String,
        confirmation: String,
    ): Boolean = password == confirmation
}
