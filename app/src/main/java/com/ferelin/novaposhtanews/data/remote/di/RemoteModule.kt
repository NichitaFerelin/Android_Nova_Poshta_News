package com.ferelin.novaposhtanews.data.remote.di

import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApi
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiImpl
import com.ferelin.novaposhtanews.data.remote.api.newsuacontent.NewsUaContentApi
import com.ferelin.novaposhtanews.data.remote.api.newsuacontent.NewsUaContentApiImpl
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApi
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApiImpl
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_DEFAULT
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_IO
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteModule = module {
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.HEADERS
            }
        }
    }

    factory {
        NewsMdApiImpl(
            get(),
            get(qualifier = named(NAMED_DISPATCHER_IO)),
        )
    } bind NewsMdApi::class

    factory {
        NewsUaContentApiImpl(
            get(qualifier = named(NAMED_DISPATCHER_DEFAULT)),
            get(qualifier = named(NAMED_DISPATCHER_IO)),
        )
    } bind NewsUaContentApi::class

    factory {
        NewsUaPreviewApiImpl(
            get(qualifier = named(NAMED_DISPATCHER_DEFAULT)),
            get(qualifier = named(NAMED_DISPATCHER_IO)),
        )
    } bind NewsUaPreviewApi::class
}
