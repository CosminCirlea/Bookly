package org.evolutionsoftware.bookly.services.catalog.di

import org.evolutionsoftware.bookly.services.catalog.data.repository.CatalogRepositoryImpl
import org.evolutionsoftware.bookly.services.catalog.domain.repository.CatalogRepository
import org.evolutionsoftware.bookly.services.catalog.domain.usecase.GetBookDetailsUseCase
import org.evolutionsoftware.bookly.services.catalog.domain.usecase.GetBooksUseCase
import org.koin.dsl.module

object CatalogDiModule {
    val module =
        module {
            single<CatalogRepository> { CatalogRepositoryImpl(get()) }
            factory { GetBooksUseCase(get()) }
            factory { GetBookDetailsUseCase(get()) }
        }
}
