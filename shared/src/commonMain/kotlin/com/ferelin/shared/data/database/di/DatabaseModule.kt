package com.ferelin.shared.data.database.di

import com.ferelin.novaposhtanews.NovaPoshtaNewsDatabase
import com.ferelin.shared.data.database.dao.NewsMdDao
import com.ferelin.shared.data.database.dao.NewsMdDaoImpl
import com.ferelin.shared.utils.NAMED_DISPATCHER_DEFAULT
import com.ferelin.shared.utils.NAMED_DISPATCHER_IO
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal val databaseModule = module {
    single { NovaPoshtaNewsDatabase(get()) }

    factory { get<NovaPoshtaNewsDatabase>().newsMdQueries }

    factory {
        NewsMdDaoImpl(
            queries = get(),
            ioDispatcher = get(qualifier = named(NAMED_DISPATCHER_IO)),
            defaultDispatcher = get(qualifier = named(NAMED_DISPATCHER_DEFAULT)),
        )
    } bind NewsMdDao::class
}
