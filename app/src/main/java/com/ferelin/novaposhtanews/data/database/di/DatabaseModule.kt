package com.ferelin.novaposhtanews.data.database.di

import com.ferelin.novaposhtanews.NovaPoshtaNewsDatabase
import com.ferelin.novaposhtanews.data.database.dao.NewsMdDao
import com.ferelin.novaposhtanews.data.database.dao.NewsMdDaoImpl
import com.ferelin.novaposhtanews.data.database.dao.NewsUaDao
import com.ferelin.novaposhtanews.data.database.dao.NewsUaDaoImpl
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_DEFAULT
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_IO
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single { NovaPoshtaNewsDatabase(get()) }

    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = NovaPoshtaNewsDatabase.Schema,
            context = androidContext(),
            name = "Nova Poshta News Database",
        )
    }

    factory { get<NovaPoshtaNewsDatabase>().newsMdQueries }
    factory { get<NovaPoshtaNewsDatabase>().newsUaQueries }

    factory {
        NewsMdDaoImpl(
            queries = get(),
            ioDispatcher = get(qualifier = named(NAMED_DISPATCHER_IO)),
            defaultDispatcher = get(qualifier = named(NAMED_DISPATCHER_DEFAULT)),
        )
    } bind NewsMdDao::class

    factory {
        NewsUaDaoImpl(
            queries = get(),
            ioDispatcher = get(qualifier = named(NAMED_DISPATCHER_IO)),
            defaultDispatcher = get(qualifier = named(NAMED_DISPATCHER_DEFAULT)),
        )
    } bind NewsUaDao::class
}
