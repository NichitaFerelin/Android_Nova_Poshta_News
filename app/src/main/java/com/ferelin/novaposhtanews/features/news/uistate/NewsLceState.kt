package com.ferelin.novaposhtanews.features.news.uistate

sealed class NewsLceState {
    object None : NewsLceState()
    object Loading : NewsLceState()
    object Content : NewsLceState()
    data class Error(val exception: Exception? = null) : NewsLceState()
}
