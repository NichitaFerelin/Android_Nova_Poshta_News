package com.ferelin.novaposhtanews.tests

import android.content.Context
import com.ferelin.novaposhtanews.novaPoshtaNewsAppModule
import io.ktor.client.*
import io.ktor.client.engine.*
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.check.checkKoinModules
import org.koin.test.verify.verify

class NovaPoshtaNewsAppModuleCheck {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkKoinRootModule() {
        novaPoshtaNewsAppModule.verify(
            extraTypes = listOf(
                Context::class,
                HttpClient::class,
                HttpClientEngine::class,
                HttpClientConfig::class,
            ),
        )
    }

    @Test
    fun dynamicCheckKoinRootModule() {
        checkKoinModules(listOf(novaPoshtaNewsAppModule))
    }
}
