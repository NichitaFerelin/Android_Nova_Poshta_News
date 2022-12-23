package com.ferelin.novaposhtanews

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class NovaPoshtaNewsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(this@NovaPoshtaNewsApp)
            modules(novaPoshtaNewsAppModule)
        }
        val meaningOfLife: MeaningOfLifeKoin by inject()
        meaningOfLife.meaningOfLife()
    }
}

val novaPoshtaNewsAppModule = module {
    includes(meaningOfLifeModule)
}
