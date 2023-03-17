package com.example.desktopapp.data.database.di

import com.ferelin.novaposhtanews.NovaPoshtaNewsDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.dsl.module

val databaseDesktopModule = module {
    single<SqlDriver> {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
            NovaPoshtaNewsDatabase.Schema.create(it)
        }
    }
}
