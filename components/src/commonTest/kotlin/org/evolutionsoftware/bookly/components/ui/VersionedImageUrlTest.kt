package org.evolutionsoftware.bookly.components.ui

import kotlin.test.Test
import kotlin.test.assertEquals

class VersionedImageUrlTest {
    @Test
    fun `last update changes the image cache URL`() {
        assertEquals(
            "https://cdn.example/page.webp?bookly_last_updated=20260814T101530000Z",
            versionedImageUrl(
                url = "https://cdn.example/page.webp",
                lastUpdated = "2026-08-14T10:15:30.000Z",
            ),
        )
    }

    @Test
    fun `missing last update leaves the original URL unchanged`() {
        assertEquals(
            "https://cdn.example/page.webp",
            versionedImageUrl("https://cdn.example/page.webp", null),
        )
    }

    @Test
    fun `version is added before existing fragments`() {
        assertEquals(
            "https://cdn.example/page.webp?size=large&bookly_last_updated=42#page",
            versionedImageUrl("https://cdn.example/page.webp?size=large#page", "42"),
        )
    }
}
