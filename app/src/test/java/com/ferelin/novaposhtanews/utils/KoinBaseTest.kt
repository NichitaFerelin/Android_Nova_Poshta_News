package com.ferelin.novaposhtanews.utils

import com.ferelin.novaposhtanews.data.database.di.databaseModule
import com.ferelin.novaposhtanews.data.datastore.di.datastoreModule
import com.ferelin.novaposhtanews.data.remote.di.remoteModule
import com.ferelin.novaposhtanews.di.dispatchersTestModule
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.AutoCloseKoinTest

internal abstract class KoinBaseTest : AutoCloseKoinTest() {

    abstract val koinTestModules: List<Module>

    @Before
    fun before() {
        stopKoin()
        startKoin {
            allowOverride(true)
            modules(
                listOf(
                    dispatchersTestModule,
                    remoteModule,
                    databaseModule,
                    datastoreModule,
                ) + koinTestModules,
            )
        }
    }
}
