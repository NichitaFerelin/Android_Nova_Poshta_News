package com.ferelin.novaposhtanews.tests

import android.content.Context
import com.ferelin.novaposhtanews.A
import com.ferelin.novaposhtanews.B
import com.ferelin.novaposhtanews.MeaningOfLifeKoin
import com.ferelin.novaposhtanews.novaPoshtaNewsAppModule
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
                MeaningOfLifeKoin::class,
                A::class,
                B::class,
            ),
        )
    }

    @Test
    fun dynamicCheckKoinRootModule() {
        checkKoinModules(listOf(novaPoshtaNewsAppModule))
    }
}
