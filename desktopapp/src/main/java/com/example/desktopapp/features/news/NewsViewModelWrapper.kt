package com.example.desktopapp.features.news

import com.ferelin.shared.features.news.NewsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewsViewModelWrapper : KoinComponent {
    val viewModel: NewsViewModel by inject()
}
