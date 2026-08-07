package org.evolutionsoftware.bookly.services.categories.di

import org.evolutionsoftware.bookly.services.categories.data.api.CategoriesAPI
import org.evolutionsoftware.bookly.services.categories.data.repository.CategoriesRepositoryImpl
import org.evolutionsoftware.bookly.services.categories.domain.repository.CategoriesRepository
import org.evolutionsoftware.bookly.services.categories.domain.usecase.GetCategoriesUseCase
import org.evolutionsoftware.bookly.services.categories.domain.usecase.GetCategoriesUseCaseImpl
import org.koin.dsl.module

object CategoriesDiModule {
    val module =
        module {
            single { CategoriesAPI(get()) }
            single<CategoriesRepository> { CategoriesRepositoryImpl(get()) }
            factory<GetCategoriesUseCase> { GetCategoriesUseCaseImpl(repository = get()) }
        }
}
