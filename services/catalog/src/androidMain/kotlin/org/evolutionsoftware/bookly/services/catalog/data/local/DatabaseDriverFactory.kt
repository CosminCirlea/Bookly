package org.evolutionsoftware.bookly.services.catalog.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.evolutionsoftware.bookly.core.CoreContext

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(CatalogDatabase.Schema, CoreContext.appContext, CATALOG_DATABASE_NAME)
}
