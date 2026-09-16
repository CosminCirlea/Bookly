package org.evolutionsoftware.bookly.services.categories.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.evolutionsoftware.bookly.core.CoreContext

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(CategoriesDatabase.Schema, CoreContext.appContext, CATEGORIES_DATABASE_NAME)
}
