package org.evolutionsoftware.bookly.services.catalog.data.local

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}

/**
 * Filename of the catalog cache.
 *
 * The cache holds nothing that cannot be re-downloaded, so it ships without SQL
 * migrations: a schema change bumps this name instead, and the next launch rebuilds
 * the cache from the network. Bump it whenever `BookEntity.sq` changes shape.
 */
internal const val CATALOG_DATABASE_NAME = "catalog_v3.db"
