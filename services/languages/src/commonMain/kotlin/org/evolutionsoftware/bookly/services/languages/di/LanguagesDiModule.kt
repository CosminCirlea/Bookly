package org.evolutionsoftware.bookly.services.languages.di

import org.evolutionsoftware.bookly.services.languages.data.api.LanguagesAPI
import org.evolutionsoftware.bookly.services.languages.data.repository.LanguagesRepositoryImpl
import org.evolutionsoftware.bookly.services.languages.domain.repository.LanguagesRepository
import org.evolutionsoftware.bookly.services.languages.domain.usecase.GetLanguagesUseCase
import org.evolutionsoftware.bookly.services.languages.domain.usecase.GetLanguagesUseCaseImpl
import org.koin.dsl.module

object LanguagesDiModule {
    val module =
        module {
            single { LanguagesAPI(get()) }
            single<LanguagesRepository> { LanguagesRepositoryImpl(get()) }
            factory<GetLanguagesUseCase> { GetLanguagesUseCaseImpl(repository = get()) }
        }
}
