package org.evolutionsoftware.bookly.services.catalog.domain.model

/**
 * How much network traffic a catalog read is allowed to spend.
 *
 * Bookly is offline-first: the cache is the source of truth for rendering, and the
 * network is only consulted when it can tell us something new.
 */
enum class CatalogRefresh {
    /**
     * Never touch the network. Whatever is on disk is what the caller gets.
     */
    CacheOnly,

    /**
     * The default. Serves the cache and spends network only where it earns its keep:
     * the book list is revalidated once per app session, and a book's pages are
     * re-downloaded only when the list reports a new content revision for it.
     */
    Automatic,

    /**
     * Always re-fetch, for an explicit user action such as retry or pull-to-refresh.
     * Falls back to the cache if the request fails.
     */
    Force,
}
