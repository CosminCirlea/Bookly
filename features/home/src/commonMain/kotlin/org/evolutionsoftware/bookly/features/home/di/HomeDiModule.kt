package org.evolutionsoftware.bookly.features.home.di

import org.evolutionsoftware.bookly.features.home.HomeEffectProducer
import org.evolutionsoftware.bookly.features.home.HomeIntentProcessor
import org.evolutionsoftware.bookly.features.home.HomeStateMapper
import org.koin.dsl.module

object HomeDiModule {
    val module =
        module {
            factory { HomeIntentProcessor(get(), get(), get(), get(), get()) }
            factory { HomeStateMapper() }
            factory { HomeEffectProducer() }
        }
}
