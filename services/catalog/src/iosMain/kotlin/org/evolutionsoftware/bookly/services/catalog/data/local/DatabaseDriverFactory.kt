package org.evolutionsoftware.bookly.services.catalog.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(CatalogDatabase.Schema, CATALOG_DATABASE_NAME)
}
