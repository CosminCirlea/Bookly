package org.evolutionsoftware.bookly.services.auth.di

import org.evolutionsoftware.bookly.core.network.TokenRefresher
import org.evolutionsoftware.bookly.services.auth.data.repository.AuthRepositoryImpl
import org.evolutionsoftware.bookly.services.auth.domain.refresher.AuthTokenRefresher
import org.evolutionsoftware.bookly.services.auth.domain.repository.AuthRepository
import org.evolutionsoftware.bookly.services.auth.domain.usecase.LoginUseCase
import org.evolutionsoftware.bookly.services.auth.domain.usecase.LoginUseCaseImpl
import org.evolutionsoftware.bookly.services.auth.domain.usecase.LogoutUseCase
import org.evolutionsoftware.bookly.services.auth.domain.usecase.RegisterUseCase
import org.evolutionsoftware.bookly.services.auth.domain.usecase.RegisterUseCaseImpl
import org.koin.dsl.module

object AuthServiceDiModule {
    val module =
        module {
            single<AuthRepository> { AuthRepositoryImpl(get()) }
            single<TokenRefresher> { AuthTokenRefresher() }
            factory<LoginUseCase> { LoginUseCaseImpl(repository = get()) }
            factory<RegisterUseCase> { RegisterUseCaseImpl(repository = get()) }
            factory { LogoutUseCase(repository = get(), authTokenStore = get(), userSessionStore = get()) }
        }
}
