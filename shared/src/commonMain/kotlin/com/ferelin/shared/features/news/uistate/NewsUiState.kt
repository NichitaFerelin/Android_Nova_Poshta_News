package com.ferelin.shared.features.news.uistate

import com.ferelin.shared.features.news.NEWS_FIRST_VISIBLE_INDEX_DEFAULT

data class NewsUiState(
    val news: List<NewsMdUi> = emptyList(),
    val firstVisibleNewsIndex: Int = NEWS_FIRST_VISIBLE_INDEX_DEFAULT,
    val firstFetchNewsMdLce: NewsLceState = NewsLceState.None,
    val isFabButtonVisible: Boolean = false,
)
