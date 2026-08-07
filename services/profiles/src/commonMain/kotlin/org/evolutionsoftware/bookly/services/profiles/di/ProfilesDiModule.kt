package org.evolutionsoftware.bookly.services.profiles.di

import org.evolutionsoftware.bookly.services.profiles.data.api.ProfilesAPI
import org.evolutionsoftware.bookly.services.profiles.data.repository.ProfileRepositoryImpl
import org.evolutionsoftware.bookly.services.profiles.domain.repository.ProfileRepository
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.CreateProfileUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.GetCurrentProfileUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.LoginUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.LogoutUseCase
import org.evolutionsoftware.bookly.services.profiles.domain.usecase.RegisterUseCase
import org.koin.dsl.module

object ProfilesDiModule {
    val module =
        module {
            single { ProfilesAPI(get()) }
            single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get()) }
            single { GetCurrentProfileUseCase(get()) }
            single { CreateProfileUseCase(get()) }
            single { LoginUseCase(get()) }
            single { RegisterUseCase(get()) }
            single { LogoutUseCase(get()) }
        }
}
