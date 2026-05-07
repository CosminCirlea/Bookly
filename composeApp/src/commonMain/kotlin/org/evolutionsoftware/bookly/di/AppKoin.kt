package org.evolutionsoftware.bookly.di

import org.evolutionsoftware.bookly.core.di.CoreDiModule
import org.evolutionsoftware.bookly.features.auth.di.AuthDiModule
import org.evolutionsoftware.bookly.features.home.di.HomeDiModule
import org.evolutionsoftware.bookly.features.reader.di.ReaderDiModule
import org.evolutionsoftware.bookly.features.settings.di.SettingsDiModule
import org.evolutionsoftware.bookly.services.auth.di.AuthServiceDiModule
import org.evolutionsoftware.bookly.services.catalog.di.CatalogDiModule
import org.evolutionsoftware.bookly.services.categories.di.CategoriesDiModule
import org.evolutionsoftware.bookly.services.favorites.di.FavoritesDiModule
import org.evolutionsoftware.bookly.services.languages.di.LanguagesDiModule
import org.evolutionsoftware.bookly.services.profiles.di.ProfilesDiModule
import org.koin.core.context.startKoin

object AppKoin {
    private var started = false

    fun start() {
        if (started) return
        started = true
        startKoin {
            modules(
                CoreDiModule.module,
                AuthServiceDiModule.module,
                CatalogDiModule.module,
                CategoriesDiModule.module,
                FavoritesDiModule.module,
                LanguagesDiModule.module,
                ProfilesDiModule.module,
                AuthDiModule.module,
                HomeDiModule.module,
                ReaderDiModule.module,
                SettingsDiModule.module,
            )
        }
    }
}
