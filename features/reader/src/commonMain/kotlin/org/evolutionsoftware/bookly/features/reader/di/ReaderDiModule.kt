package org.evolutionsoftware.bookly.features.reader.di

import org.evolutionsoftware.bookly.features.reader.ReaderEffectProducer
import org.evolutionsoftware.bookly.features.reader.ReaderIntentProcessor
import org.evolutionsoftware.bookly.features.reader.ReaderStateMapper
import org.koin.dsl.module

object ReaderDiModule {
    val module =
        module {
            factory { ReaderIntentProcessor(get()) }
            factory { ReaderStateMapper() }
            factory { ReaderEffectProducer() }
        }
}
