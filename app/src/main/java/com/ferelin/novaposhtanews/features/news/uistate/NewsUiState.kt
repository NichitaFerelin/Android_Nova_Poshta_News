package com.ferelin.novaposhtanews.features.news.uistate

import androidx.compose.runtime.Immutable
import com.ferelin.novaposhtanews.features.news.NEWS_PIVOT_TO_LOAD_MORE


@Immutable
data class NewsUiState(
    val news: List<NewsUiItem> = emptyList(),
    val expandedNewsItem: NewsUiItem? = null,
    val expandedNewsItemContentLce: NewsLceState = NewsLceState.None,
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
        isMdNewsEnabled && firstFetchNewsMdLce is NewsLceState.Error

    fun uaNewsShouldBeUpdated(): Boolean =
        isUaNewsEnabled && firstFetchNewsUaLce is NewsLceState.Error

    fun shouldAutoLoadMoreNews(firstVisibleItemIndex: Int) : Boolean =
        isUaNewsEnabled && isAutoLoadEnabled && news.lastIndex - firstVisibleItemIndex < NEWS_PIVOT_TO_LOAD_MORE
}
