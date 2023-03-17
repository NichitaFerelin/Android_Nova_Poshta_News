package com.ferelin.novaposhtanews

import android.app.Application
import com.ferelin.novaposhtanews.data.database.di.databaseAndroidModule
import com.ferelin.novaposhtanews.data.remote.di.remoteAndroidModule
import com.ferelin.novaposhtanews.features.news.di.newsAndroidModule
import com.ferelin.novaposhtanews.utils.dispatchers.di.dispatchersAndroidModule
import com.ferelin.shared.utils.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class NovaPoshtaNewsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(this@NovaPoshtaNewsApp)
            modules(dispatchersAndroidModule, newsAndroidModule, databaseAndroidModule, remoteAndroidModule)
        }
    }
}
