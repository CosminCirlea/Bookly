package org.evolutionsoftware.bookly.di

import org.evolutionsoftware.bookly.core.di.CoreDiModule
import org.evolutionsoftware.bookly.features.home.di.HomeDiModule
import org.evolutionsoftware.bookly.features.reader.di.ReaderDiModule
import org.evolutionsoftware.bookly.features.settings.di.SettingsDiModule
import org.evolutionsoftware.bookly.services.catalog.di.CatalogDiModule
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
                CatalogDiModule.module,
                ProfilesDiModule.module,
                HomeDiModule.module,
                ReaderDiModule.module,
                SettingsDiModule.module,
            )
        }
    }
}
