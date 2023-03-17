package com.ferelin.novaposhtanews.features.news.di

import com.ferelin.novaposhtanews.features.news.NewsAndroidViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsAndroidModule = module {
    viewModel {
        NewsAndroidViewModel(get())
    }
}
