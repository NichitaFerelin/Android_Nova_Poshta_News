package com.ferelin.novaposhtanews.features.news.uistate

import androidx.compose.runtime.Immutable
import com.ferelin.novaposhtanews.features.news.NEWS_FIRST_VISIBLE_INDEX_DEFAULT
import com.ferelin.novaposhtanews.features.news.NEWS_PIVOT_TO_LOAD_MORE

@Immutable
data class NewsUiState(
    val news: List<NewsUiItem> = emptyList(),
    val firstVisibleNewsIndex: Int = NEWS_FIRST_VISIBLE_INDEX_DEFAULT,
    val firstFetchNewsUaLce: NewsLceState = NewsLceState.None,
    val firstFetchNewsMdLce: NewsLceState = NewsLceState.None,
    val newsUaLce: NewsLceState = NewsLceState.None,
    val newsUaEndOfPageReached: Boolean = false,
    val isSettingsDialogActive: Boolean = false,
    val isFabButtonVisible: Boolean = false,
    val isUaNewsEnabled: Boolean = false,
    val isMdNewsEnabled: Boolean = false,
    val isAutoLoadEnabled: Boolean = false,
    val isSortByCategoryEnabled: Boolean = false,
    val isAscendingTimestampOrderEnabled: Boolean = false,
) {
    fun mdNewsShouldBeUpdated(): Boolean =
        isMdNewsEnabled && (firstFetchNewsMdLce is NewsLceState.Error || firstFetchNewsMdLce is NewsLceState.None)

    fun uaNewsShouldBeUpdated(): Boolean =
        isUaNewsEnabled && (firstFetchNewsUaLce is NewsLceState.Error || firstFetchNewsUaLce is NewsLceState.None)

    fun shouldAutoLoadMoreNews(): Boolean =
        isUaNewsEnabled && isAutoLoadEnabled && news.lastIndex - firstVisibleNewsIndex < NEWS_PIVOT_TO_LOAD_MORE
}
