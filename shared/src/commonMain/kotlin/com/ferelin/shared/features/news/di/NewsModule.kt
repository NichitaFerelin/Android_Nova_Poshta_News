package com.ferelin.shared.features.news.di

import com.ferelin.shared.features.news.NewsViewModel
import com.ferelin.shared.utils.NAMED_DISPATCHER_DEFAULT
import com.ferelin.shared.utils.NAMED_DISPATCHER_MAIN
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val newsModule = module {
    factory {
        NewsViewModel(
            newsMdApi = get(),
            newsMdDao = get(),
            defaultDispatcher = get(named(NAMED_DISPATCHER_DEFAULT)),
            mainDispatcher = get(named(NAMED_DISPATCHER_MAIN)),
        )
    }
}
