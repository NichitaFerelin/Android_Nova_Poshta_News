package com.ferelin.novaposhtanews.data.database.di

import com.ferelin.novaposhtanews.NovaPoshtaNewsDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        NovaPoshtaNewsDatabase(
            driver = AndroidSqliteDriver(
                schema = NovaPoshtaNewsDatabase.Schema,
                context = androidContext(),
                name = "Nova Poshta News Database",
            ),
        )
    }
}
