package com.ferelin.shared.features.news

import com.ferelin.shared.data.database.dao.NewsMdDao
import com.ferelin.shared.data.remote.api.newsmd.NewsMdApi
import com.ferelin.shared.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.shared.features.news.uistate.NewsLceState
import com.ferelin.shared.features.news.uistate.NewsUiState
import com.ferelin.shared.features.news.utils.asDbo
import com.ferelin.shared.features.news.utils.asUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import novaposhtanewsdatabase.NewsMdDBO

const val NEWS_PIVOT_TO_SHOW_FAB = 13
const val NEWS_FIRST_VISIBLE_INDEX_DEFAULT = 0

class NewsViewModel(
    private val newsMdApi: NewsMdApi,
    private val newsMdDao: NewsMdDao,
    private val defaultDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState = _uiState.asStateFlow()

    private val viewModelScope = CoroutineScope(mainDispatcher)

    init {
        fetchMdNews()

        newsMdDao.news
            .onEach(::updateUiStateWithNews)
            .flowOn(defaultDispatcher)
            .launchIn(viewModelScope)
    }

    fun clear() {
        viewModelScope.cancel()
    }

    fun onFirstVisibleIndexChanged(index: Int) {
        _uiState.update {
            it.copy(
                isFabButtonVisible = index > NEWS_PIVOT_TO_SHOW_FAB,
                firstVisibleNewsIndex = index,
            )
        }
    }

    fun onRepeatInitialLoadNewsClick() {
        fetchMdNews()
    }

    private fun fetchMdNews() {
        _uiState.update { it.copy(firstFetchNewsMdLce = NewsLceState.Loading) }

        viewModelScope.launch {
            val fetchedNewsResult = newsMdApi.fetchNews()
            if (fetchedNewsResult.isSuccess) {
                onMdNewsFetchComplete(fetchedNewsResult.getOrThrow())
            } else {
                _uiState.update { it.copy(firstFetchNewsMdLce = NewsLceState.Error()) }
            }
        }
    }

    private suspend fun onMdNewsFetchComplete(fetchedNews: List<NewsMdApiItem>) {
        if (fetchedNews.isEmpty()) {
            _uiState.update { it.copy(firstFetchNewsMdLce = NewsLceState.Error()) }
            return
        }

        val newsDbo = withContext(defaultDispatcher) { fetchedNews.map { it.asDbo() } }
        newsMdDao.insertAll(newsDbo)
        _uiState.update { it.copy(firstFetchNewsMdLce = NewsLceState.Content) }
    }

    private fun updateUiStateWithNews(news: List<NewsMdDBO>) {
        val uiNews = news
            .asSequence()
            .sortedBy { it.timestamp }
            .map { it.asUi() }
            .toList()

        _uiState.update { it.copy(news = uiNews) }
    }
}
