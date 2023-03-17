package com.example.desktopapp.utils.dispatchers.di

import com.ferelin.shared.utils.NAMED_DISPATCHER_DEFAULT
import com.ferelin.shared.utils.NAMED_DISPATCHER_IO
import com.ferelin.shared.utils.NAMED_DISPATCHER_MAIN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val dispatchersDesktopModule = module {
    factory<CoroutineDispatcher> { newSingleThreadContext("Desktop Thread 3") } withOptions { named(NAMED_DISPATCHER_IO) }
    factory<CoroutineDispatcher> { newSingleThreadContext("Desktop Thread 2") } withOptions { named(NAMED_DISPATCHER_DEFAULT) }
    factory<CoroutineDispatcher> { newSingleThreadContext("Desktop Thread 1") } withOptions { named(NAMED_DISPATCHER_MAIN) }
}
