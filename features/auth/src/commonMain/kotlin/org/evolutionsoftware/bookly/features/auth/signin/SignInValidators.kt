package org.evolutionsoftware.bookly.features.auth.signin

import org.evolutionsoftware.bookly.features.auth.common.resolveDisplayName

internal object SignInValidators {
    fun isFormValid(
        emailOrPhone: String,
        password: String,
    ): Boolean = resolveDisplayName(emailOrPhone).isNotBlank() && password.length >= 4
}
