package org.evolutionsoftware.bookly.services.categories.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(CategoriesDatabase.Schema, CATEGORIES_DATABASE_NAME)
}
