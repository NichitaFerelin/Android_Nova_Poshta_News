package com.ferelin.novaposhtanews.features.news

import androidx.lifecycle.ViewModel
import com.ferelin.shared.features.news.NewsViewModel

class NewsAndroidViewModel(
    val contractViewModel: NewsViewModel,
) : ViewModel() {

    override fun onCleared() {
        contractViewModel.clear()
    }
}
