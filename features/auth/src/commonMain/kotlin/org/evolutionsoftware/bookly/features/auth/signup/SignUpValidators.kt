package org.evolutionsoftware.bookly.features.auth.signup

import org.evolutionsoftware.bookly.features.auth.common.PasswordValidators
import org.evolutionsoftware.bookly.features.auth.common.isValidEmail

internal object SignUpValidators {
    fun isFormValid(
        emailOrPhone: String,
        password: String,
        confirmPassword: String,
    ): Boolean =
        isValidEmail(emailOrPhone) &&
            PasswordValidators.isStrongEnough(password) &&
            PasswordValidators.doPasswordsMatch(password, confirmPassword)
}
