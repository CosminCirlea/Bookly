package org.evolutionsoftware.bookly.features.reader

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReaderStateMapperTest {
    private val mapper = ReaderStateMapper()

    @Test
    fun `favorite control availability follows the current profile`() {
        val available =
            mapper(
                ReaderAction.FavoriteAvailabilityLoaded(canFavorite = true),
                ReaderViewState(),
            )

        assertTrue(available.canFavorite)
        assertFalse(mapper(ReaderAction.LoadingStarted, available).canFavorite)
    }
}
