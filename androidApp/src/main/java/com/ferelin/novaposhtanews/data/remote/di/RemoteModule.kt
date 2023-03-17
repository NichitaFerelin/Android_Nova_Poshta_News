package com.ferelin.novaposhtanews.data.remote.di

import io.ktor.client.engine.android.Android
import org.koin.dsl.module

val remoteAndroidModule = module {
    single { Android.create() }
}
