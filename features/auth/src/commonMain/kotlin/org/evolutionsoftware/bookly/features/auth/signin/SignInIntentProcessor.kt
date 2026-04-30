package org.evolutionsoftware.bookly.features.auth.signin

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_sign_in_validation_error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.features.auth.common.resolveDisplayName
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.LoginUseCase

internal class SignInIntentProcessor(
    private val loginUseCase: LoginUseCase,
) : IntentProcessor<SignInIntent, SignInAction> {
    override fun invoke(intent: SignInIntent): Flow<SignInAction> =
        when (intent) {
            SignInIntent.ForgotPasswordClicked -> flowOf(SignInAction.ForgotPasswordNavigationRequested)
            SignInIntent.PasswordVisibilityToggled -> flowOf(SignInAction.PasswordVisibilityToggled)
            SignInIntent.SignUpClicked -> flowOf(SignInAction.SignUpNavigationRequested)
            is SignInIntent.EmailOrPhoneChanged -> flowOf(SignInAction.EmailOrPhoneUpdated(intent.value))
            is SignInIntent.PasswordChanged -> flowOf(SignInAction.PasswordUpdated(intent.value))
            is SignInIntent.Submit ->
                flow {
                    val displayName = resolveDisplayName(intent.emailOrPhone)
                    if (!SignInValidators.isFormValid(intent.emailOrPhone, intent.password)) {
                        emit(SignInAction.ValidationFailed(Res.string.auth_sign_in_validation_error))
                        return@flow
                    }

                    emit(SignInAction.SubmissionStarted)
                    loginUseCase(displayName)
                    emit(SignInAction.SubmissionSucceeded)
                }
        }
}
