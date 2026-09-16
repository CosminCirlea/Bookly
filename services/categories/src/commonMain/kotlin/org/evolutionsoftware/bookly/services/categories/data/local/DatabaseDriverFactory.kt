package org.evolutionsoftware.bookly.services.categories.data.local

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}

internal const val CATEGORIES_DATABASE_NAME = "categories_v1.db"
