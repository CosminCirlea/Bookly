package org.evolutionsoftware.bookly.features.auth.signin

import bookly.features.auth.generated.resources.Res
import bookly.features.auth.generated.resources.auth_sign_in_validation_error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.evolutionsoftware.bookly.core.mvi.IntentProcessor
import org.evolutionsoftware.bookly.core.network.AuthToken
import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.UserSession
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.evolutionsoftware.bookly.core.usecase.utils.Result
import org.evolutionsoftware.bookly.services.auth.domain.model.AuthSession
import org.evolutionsoftware.bookly.services.auth.domain.usecase.LoginUseCase

internal class SignInIntentProcessor(
    private val loginUseCase: LoginUseCase,
    private val authTokenStore: AuthTokenStore,
    private val userSessionStore: UserSessionStore,
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
                    if (!SignInValidators.isFormValid(intent.emailOrPhone, intent.password)) {
                        emit(SignInAction.ValidationFailed(Res.string.auth_sign_in_validation_error))
                        return@flow
                    }

                    emit(SignInAction.SubmissionStarted)

                    val result = loginUseCase(
                        email = intent.emailOrPhone.trim(),
                        password = intent.password,
                    )

                    when (result) {
                        is Result.Success -> {
                            storeSession(result.data)
                            emit(SignInAction.SubmissionSucceeded)
                        }
                        is Result.Error -> emit(SignInAction.SubmissionFailed)
                    }
                }
        }

    private suspend fun storeSession(session: AuthSession) {
        authTokenStore.write(
            AuthToken(
                accessToken = session.accessToken,
                refreshToken = session.refreshToken,
            ),
        )
        userSessionStore.write(
            UserSession(
                userId = session.user.id,
                displayName = session.user.email,
            ),
        )
    }
}
