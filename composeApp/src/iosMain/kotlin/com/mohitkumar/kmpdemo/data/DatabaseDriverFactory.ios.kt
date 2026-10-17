package com.mohitkumar.kmpdemo.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.mohitkumar.kmpdemo.db.AppDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(AppDatabase.Schema, "posts.db")
}
