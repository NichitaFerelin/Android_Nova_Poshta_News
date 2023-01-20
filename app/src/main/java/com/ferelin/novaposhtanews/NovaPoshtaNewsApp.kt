package com.ferelin.novaposhtanews

import android.app.Application
import com.ferelin.novaposhtanews.data.database.di.databaseModule
import com.ferelin.novaposhtanews.data.datastore.di.datastoreModule
import com.ferelin.novaposhtanews.data.remote.di.remoteModule
import com.ferelin.novaposhtanews.features.news.di.newsModule
import com.ferelin.novaposhtanews.utils.dispatchers.di.dispatchersModule
import com.ferelin.novaposhtanews.utils.locale.di.localeModule
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
    }
}

val novaPoshtaNewsAppModule = module {
    includes(
        dispatchersModule,
        remoteModule,
        databaseModule,
        datastoreModule,
        localeModule,
        newsModule,
    )
}
