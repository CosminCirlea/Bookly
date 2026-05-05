package org.evolutionsoftware.bookly.features.auth.signup

import org.evolutionsoftware.bookly.features.auth.common.PasswordValidators

internal object SignUpValidators {
    fun isFormValid(
        emailOrPhone: String,
        password: String,
        confirmPassword: String,
    ): Boolean =
        emailOrPhone.isNotBlank() &&
            PasswordValidators.isStrongEnough(password) &&
            PasswordValidators.doPasswordsMatch(password, confirmPassword)
}
