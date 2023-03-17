package com.example.desktopapp.data.remote.di

import io.ktor.client.engine.java.Java
import java.net.InetSocketAddress
import java.net.Proxy
import org.koin.dsl.module

val remoteDesktopModule = module {
    single {
        Java.create {
            proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("10.130.10.254", 3128))
        }
    }
}
