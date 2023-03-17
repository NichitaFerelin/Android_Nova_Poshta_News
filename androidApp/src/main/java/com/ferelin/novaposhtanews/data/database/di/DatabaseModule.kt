package com.ferelin.novaposhtanews.data.database.di

import com.ferelin.novaposhtanews.NovaPoshtaNewsDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseAndroidModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = NovaPoshtaNewsDatabase.Schema,
            context = androidContext(),
            name = "Nova Poshta News Database",
        )
    }
}
