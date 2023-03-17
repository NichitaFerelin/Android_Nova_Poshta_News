package com.ferelin.shared.data.remote.di

import com.ferelin.shared.data.remote.api.newsmd.NewsMdApi
import com.ferelin.shared.data.remote.api.newsmd.NewsMdApiImpl
import com.ferelin.shared.utils.NAMED_DISPATCHER_IO
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal val remoteModule = module {
    single {
        HttpClient(get()) {
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
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
            }
        }
    }

    factory {
        NewsMdApiImpl(
            get(),
            get(qualifier = named(NAMED_DISPATCHER_IO)),
        )
    } bind NewsMdApi::class
}
