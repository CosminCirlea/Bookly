package org.evolutionsoftware.bookly.core.di

import io.ktor.client.HttpClient
import org.evolutionsoftware.bookly.core.auth.CheckSessionUseCase
import org.evolutionsoftware.bookly.core.auth.ClearSessionUseCase
import org.evolutionsoftware.bookly.core.network.AuthTokenStore
import org.evolutionsoftware.bookly.core.network.HttpClientFactory
import org.evolutionsoftware.bookly.core.network.NetworkConfig
import org.evolutionsoftware.bookly.core.network.NoopTokenRefresher
import org.evolutionsoftware.bookly.core.network.PersistentAuthTokenStore
import org.evolutionsoftware.bookly.core.network.PersistentUserSessionStore
import org.evolutionsoftware.bookly.core.network.TokenRefresher
import org.evolutionsoftware.bookly.core.network.UserSessionStore
import org.koin.dsl.module

object CoreDiModule {
    val module =
        module {
            single<AuthTokenStore> { PersistentAuthTokenStore() }
            single<UserSessionStore> { PersistentUserSessionStore() }
            single { NetworkConfig() }
            single<HttpClient> {
                HttpClientFactory(
                    config = get(),
                    tokenStore = get(),
                    tokenRefresher = getOrNull<TokenRefresher>() ?: NoopTokenRefresher,
                ).create()
            }
            single { CheckSessionUseCase(get(), get()) }
            single { ClearSessionUseCase(get(), get()) }
        }
}
