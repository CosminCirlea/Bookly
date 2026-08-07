package org.evolutionsoftware.bookly.features.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_password_match_error
import bookly.features.auth.generated.resources.auth_sign_in_validation_error
import org.evolutionsoftware.bookly.design.theme.bookly.BooklyTheme
import org.evolutionsoftware.bookly.features.auth.changepassword.ChangePasswordViewState
import org.evolutionsoftware.bookly.features.auth.common.PasswordEditorScreen
import org.evolutionsoftware.bookly.features.auth.common.PasswordStrengthMeter
import org.evolutionsoftware.bookly.features.auth.createprofile.CreateProfileContent
import org.evolutionsoftware.bookly.features.auth.createprofile.CreateProfileViewState
import org.evolutionsoftware.bookly.features.auth.forgotpassword.ForgotPasswordRoute
import org.evolutionsoftware.bookly.features.auth.onboarding.OnboardingRoute
import org.evolutionsoftware.bookly.features.auth.resetpassword.ResetPasswordViewState
import org.evolutionsoftware.bookly.features.auth.signin.SignInContent
import org.evolutionsoftware.bookly.features.auth.signin.SignInViewState
import org.evolutionsoftware.bookly.features.auth.signup.SignUpContent
import org.evolutionsoftware.bookly.features.auth.signup.SignUpViewState

// === Sign in ==============================================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignInEmptyPreview() {
    BooklyTheme {
        SignInContent(
            viewState = SignInViewState(),
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignInFilledPreview() {
    BooklyTheme {
        SignInContent(
            viewState =
                SignInViewState(
                    emailOrPhone = "parent@email.com",
                    password = "hunter2!",
                    isFormValid = true,
                ),
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignInLoadingPreview() {
    BooklyTheme {
        SignInContent(
            viewState =
                SignInViewState(
                    emailOrPhone = "parent@email.com",
                    password = "hunter2!",
                    isFormValid = true,
                    isLoading = true,
                ),
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignInErrorPreview() {
    BooklyTheme {
        SignInContent(
            viewState =
                SignInViewState(
                    emailOrPhone = "parent@email.com",
                    errorMessage = Res.string.auth_sign_in_validation_error,
                ),
            onIntent = {},
            onBack = {},
        )
    }
}

// === Sign up ==============================================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpEmptyPreview() {
    BooklyTheme {
        SignUpContent(
            viewState = SignUpViewState(),
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpStrongPasswordPreview() {
    BooklyTheme {
        SignUpContent(
            viewState =
                SignUpViewState(
                    emailOrPhone = "parent@email.com",
                    password = "Bookly2026!",
                    confirmPassword = "Bookly2026!",
                    isFormValid = true,
                ),
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpMismatchPreview() {
    BooklyTheme {
        SignUpContent(
            viewState =
                SignUpViewState(
                    emailOrPhone = "parent@email.com",
                    password = "weakpw",
                    confirmPassword = "different",
                    errorMessage = Res.string.auth_password_match_error,
                ),
            onIntent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, name = "Password strength - all scores")
@Composable
private fun PasswordStrengthMeterPreview() {
    BooklyTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            listOf("", "abcdef", "abcdefghij", "Abcdefghij", "Abcdefghij1").forEach { candidate ->
                PasswordStrengthMeter(password = candidate)
            }
        }
    }
}

// === Forgot password ======================================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ForgotPasswordPreview() {
    BooklyTheme {
        ForgotPasswordRoute(
            onBack = {},
            onLinkSent = {},
        )
    }
}

// === Reset / new password =================================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ResetPasswordPreview() {
    val state =
        ResetPasswordViewState(
            newPassword = "Bookly2026",
            confirmPassword = "Bookly2026",
        )
    BooklyTheme {
        PasswordEditorScreen(
            screenTitle = "New Password",
            description = "Create a new secure password to access your playroom.",
            submitLabel = "Update Password",
            includeCurrentPassword = false,
            currentPasswordLabel = "Current Password",
            currentPasswordPlaceholder = "Enter current password",
            newPasswordLabel = "New Password",
            newPasswordPlaceholder = "Enter new password",
            confirmPasswordLabel = "Confirm New Password",
            confirmPasswordPlaceholder = "Confirm new password",
            errorMessage = null,
            isLoading = false,
            isSubmitEnabled = true,
            showPasswordStrength = true,
            currentPassword = "",
            newPassword = state.newPassword,
            confirmPassword = state.confirmPassword,
            isNewPasswordVisible = false,
            isConfirmPasswordVisible = false,
            onBack = {},
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmPasswordChange = {},
            onNewPasswordVisibilityToggle = {},
            onConfirmPasswordVisibilityToggle = {},
            onSubmit = {},
        )
    }
}

// === Change password ======================================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChangePasswordPreview() {
    val state =
        ChangePasswordViewState(
            currentPassword = "oldpassword",
            newPassword = "Bookly2026",
            confirmPassword = "Bookly2026",
        )
    BooklyTheme {
        PasswordEditorScreen(
            screenTitle = "Change Password",
            description = null,
            submitLabel = "Update Password",
            includeCurrentPassword = true,
            currentPasswordLabel = "Current Password",
            currentPasswordPlaceholder = "Enter current password",
            newPasswordLabel = "New Password",
            newPasswordPlaceholder = "Enter new password",
            confirmPasswordLabel = "Confirm New Password",
            confirmPasswordPlaceholder = "Confirm new password",
            errorMessage = null,
            isLoading = false,
            isSubmitEnabled = true,
            showPasswordStrength = false,
            currentPassword = state.currentPassword,
            newPassword = state.newPassword,
            confirmPassword = state.confirmPassword,
            isNewPasswordVisible = false,
            isConfirmPasswordVisible = false,
            onBack = {},
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmPasswordChange = {},
            onNewPasswordVisibilityToggle = {},
            onConfirmPasswordVisibilityToggle = {},
            onSubmit = {},
        )
    }
}

// === Onboarding ===========================================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnboardingPreview() {
    BooklyTheme {
        OnboardingRoute(
            onDone = {},
            onRegister = {},
        )
    }
}

// === Create child profile =================================================

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateProfileEmptyPreview() {
    BooklyTheme {
        CreateProfileContent(
            viewState = CreateProfileViewState(),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateProfileFilledPreview() {
    BooklyTheme {
        CreateProfileContent(
            viewState =
                CreateProfileViewState(
                    name = "Mia",
                    dateOfBirth = "2021-04-18",
                    isMale = false,
                    selectedAvatar = 2,
                ),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateProfileNameErrorPreview() {
    BooklyTheme {
        CreateProfileContent(
            viewState = CreateProfileViewState(nameError = true),
            onIntent = {},
        )
    }
}
