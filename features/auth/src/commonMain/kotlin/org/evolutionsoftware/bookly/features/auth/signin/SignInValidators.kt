package org.evolutionsoftware.bookly.features.auth.signin

import org.evolutionsoftware.bookly.features.auth.common.isValidEmail

internal object SignInValidators {
    fun isFormValid(
        emailOrPhone: String,
        password: String,
    ): Boolean = isValidEmail(emailOrPhone) && password.length >= 4
}
