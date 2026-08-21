package org.evolutionsoftware.bookly.features.auth.createprofile

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BirthdateTest {
    @Test
    fun `UTC date millis use the API ISO format`() {
        assertEquals("1970-01-01", 0L.toIsoBirthdate())
        assertEquals("1969-12-31", (-86_400_000L).toIsoBirthdate())
        assertEquals("2000-02-29", 951_782_400_000L.toIsoBirthdate())
        assertEquals("2024-02-29", 1_709_164_800_000L.toIsoBirthdate())
    }

    @Test
    fun `ISO birthdates convert back to UTC date millis`() {
        assertEquals(0L, "1970-01-01".toUtcBirthdateMillisOrNull())
        assertEquals(951_782_400_000L, "2000-02-29".toUtcBirthdateMillisOrNull())
        assertEquals(1_709_164_800_000L, "2024-02-29".toUtcBirthdateMillisOrNull())
    }

    @Test
    fun `invalid birthdates are rejected`() {
        assertNull("2024-02-30".toUtcBirthdateMillisOrNull())
        assertNull("not-a-date".toUtcBirthdateMillisOrNull())
    }
}
