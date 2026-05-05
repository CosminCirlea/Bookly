package org.evolutionsoftware.bookly.features.settings.di

import org.evolutionsoftware.bookly.features.settings.SettingsEffectProducer
import org.evolutionsoftware.bookly.features.settings.SettingsIntentProcessor
import org.evolutionsoftware.bookly.features.settings.SettingsStateMapper
import org.koin.dsl.module

object SettingsDiModule {
    val module =
        module {
            factory { SettingsIntentProcessor(get(), get(), get()) }
            factory { SettingsStateMapper() }
            factory { SettingsEffectProducer() }
        }
}
