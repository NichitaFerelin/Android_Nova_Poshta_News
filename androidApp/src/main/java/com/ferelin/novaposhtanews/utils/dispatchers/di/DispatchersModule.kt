package com.ferelin.novaposhtanews.utils.dispatchers.di

import com.ferelin.shared.utils.NAMED_DISPATCHER_DEFAULT
import com.ferelin.shared.utils.NAMED_DISPATCHER_IO
import com.ferelin.shared.utils.NAMED_DISPATCHER_MAIN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val dispatchersAndroidModule = module {
    factory { Dispatchers.IO } withOptions { named(NAMED_DISPATCHER_IO) }
    factory { Dispatchers.Default } withOptions { named(NAMED_DISPATCHER_DEFAULT) }
    factory<CoroutineDispatcher> { Dispatchers.Main } withOptions { named(NAMED_DISPATCHER_MAIN) }
}
