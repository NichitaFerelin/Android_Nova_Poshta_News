package com.ferelin.novaposhtanews.tests

import android.content.Context
import com.ferelin.novaposhtanews.NovaPoshtaNewsDatabase
import com.ferelin.novaposhtanews.novaPoshtaNewsAppModule
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.*
import io.ktor.client.engine.*
import io.mockk.mockk
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.test.check.checkModules
import org.koin.test.verify.verify

internal class NovaPoshtaNewsAppModuleCheck {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkKoinRootModule() {
        novaPoshtaNewsAppModule.verify(
            extraTypes = listOf(
                Context::class,
                HttpClient::class,
                HttpClientEngine::class,
                HttpClientConfig::class,
                NovaPoshtaNewsDatabase::class,
                AndroidSqliteDriver::class,
            ),
        )
    }

    @Test
    fun dynamicCheckKoinRootModule() {
        startKoin {
            androidContext(mockk())
            modules(listOf(novaPoshtaNewsAppModule))
        }.checkModules()
    }
}
