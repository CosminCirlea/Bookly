package org.evolutionsoftware.bookly.features.auth.signup

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_sign_up_validation_error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.RegisterUseCase

internal class SignUpIntentProcessor(
    private val registerUseCase: RegisterUseCase,
) : IntentProcessor<SignUpIntent, SignUpAction> {
    override fun invoke(intent: SignUpIntent): Flow<SignUpAction> =
        when (intent) {
            SignUpIntent.PasswordVisibilityToggled -> flowOf(SignUpAction.PasswordVisibilityToggled)
            SignUpIntent.SignInClicked -> flowOf(SignUpAction.SignInNavigationRequested)
            is SignUpIntent.DisplayNameChanged -> flowOf(SignUpAction.DisplayNameUpdated(intent.value))
            is SignUpIntent.EmailOrPhoneChanged -> flowOf(SignUpAction.EmailOrPhoneUpdated(intent.value))
            is SignUpIntent.PasswordChanged -> flowOf(SignUpAction.PasswordUpdated(intent.value))
            is SignUpIntent.Submit ->
                flow {
                    if (!SignUpValidators.isFormValid(intent.displayName, intent.emailOrPhone, intent.password)) {
                        emit(SignUpAction.ValidationFailed(Res.string.auth_sign_up_validation_error))
                        return@flow
                    }

                    emit(SignUpAction.SubmissionStarted)
                    registerUseCase(intent.displayName.trim())
                    emit(SignUpAction.SubmissionSucceeded)
                }
        }
}
