package com.ferelin.shared.utils.di

import com.ferelin.shared.data.database.di.databaseModule
import com.ferelin.shared.data.remote.di.remoteModule
import com.ferelin.shared.features.news.di.newsModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
) = startKoin {
    allowOverride(override = false)
    appDeclaration()
    modules(
        remoteModule,
        databaseModule,
        newsModule
    )
}
