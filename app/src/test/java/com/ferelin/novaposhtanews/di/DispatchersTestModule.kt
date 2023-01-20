package com.ferelin.novaposhtanews.di

import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_DEFAULT
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_IO
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_MAIN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
internal val dispatchersTestModule = module {
    factory<CoroutineDispatcher> { UnconfinedTestDispatcher() } withOptions {
        named(NAMED_DISPATCHER_IO)
    }
    factory<CoroutineDispatcher> { UnconfinedTestDispatcher() } withOptions {
        named(NAMED_DISPATCHER_DEFAULT)
    }
    factory<CoroutineDispatcher> { UnconfinedTestDispatcher() } withOptions {
        named(NAMED_DISPATCHER_MAIN)
    }
}
