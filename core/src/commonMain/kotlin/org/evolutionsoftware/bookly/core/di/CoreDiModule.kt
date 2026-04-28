package org.evolutionsoftware.bookly.core.di

import org.evolutionsoftware.bookly.core.auth.CheckSessionUseCase
import org.evolutionsoftware.bookly.core.auth.ClearSessionUseCase
import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.CachedAuthTokenStore
import org.evolutionsoftware.bookly.core.network.DefaultUserSessionStore
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.koin.dsl.module

object CoreDiModule {
    val module =
        module {
            single<AuthTokenStore> { CachedAuthTokenStore() }
            single<UserSessionStore> { DefaultUserSessionStore() }
            single { CheckSessionUseCase(get(), get()) }
            single { ClearSessionUseCase(get(), get()) }
        }
}
