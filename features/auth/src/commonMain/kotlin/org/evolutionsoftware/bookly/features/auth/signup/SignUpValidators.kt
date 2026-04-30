package org.evolutionsoftware.bookly.features.auth.signup

internal object SignUpValidators {
    fun isFormValid(
        displayName: String,
        emailOrPhone: String,
        password: String,
    ): Boolean = displayName.isNotBlank() && emailOrPhone.isNotBlank() && password.length >= 4
}
