package com.example.desktopapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.desktopapp.data.database.di.databaseDesktopModule
import com.example.desktopapp.data.remote.di.remoteDesktopModule
import com.example.desktopapp.features.news.NewsScreen
import com.example.desktopapp.features.news.NewsViewModelWrapper
import com.example.desktopapp.utils.dispatchers.di.dispatchersDesktopModule
import com.ferelin.shared.utils.di.initKoin

fun main() = application {
    initKoin {
        modules(dispatchersDesktopModule, databaseDesktopModule, remoteDesktopModule)
    }

    MainWindow()
}

@Composable
private fun ApplicationScope.MainWindow() {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Nova Poshta News",
        state = rememberWindowState(width = 1400.dp, height = 600.dp),
    ) {
        NewsScreen(newsViewModel = NewsViewModelWrapper().viewModel)
    }
}
