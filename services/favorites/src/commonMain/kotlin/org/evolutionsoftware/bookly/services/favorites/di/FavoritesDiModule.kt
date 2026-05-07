package org.evolutionsoftware.bookly.services.favorites.di

import org.evolutionsoftware.bookly.services.favorites.data.repository.FavoritesRepositoryImpl
import org.evolutionsoftware.bookly.services.favorites.domain.repository.FavoritesRepository
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.AddFavoriteUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.AddFavoriteUseCaseImpl
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.GetFavoritesUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.GetFavoritesUseCaseImpl
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.RemoveFavoriteUseCase
import org.evolutionsoftware.bookly.services.favorites.domain.usecase.RemoveFavoriteUseCaseImpl
import org.koin.dsl.module

object FavoritesDiModule {
    val module =
        module {
            single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
            factory<GetFavoritesUseCase> { GetFavoritesUseCaseImpl(repository = get()) }
            factory<AddFavoriteUseCase> { AddFavoriteUseCaseImpl(repository = get()) }
            factory<RemoveFavoriteUseCase> { RemoveFavoriteUseCaseImpl(repository = get()) }
        }
}
