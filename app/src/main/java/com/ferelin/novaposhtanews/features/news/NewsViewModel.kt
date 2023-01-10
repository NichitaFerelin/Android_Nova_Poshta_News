package com.ferelin.novaposhtanews.features.news

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferelin.novaposhtanews.AppPreferences
import com.ferelin.novaposhtanews.UserSortPreferences
import com.ferelin.novaposhtanews.copy
import com.ferelin.novaposhtanews.data.database.dao.NewsMdDao
import com.ferelin.novaposhtanews.data.database.dao.NewsUaDao
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApi
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.novaposhtanews.data.remote.api.newsuacontent.NewsUaContentApi
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.API_NEWS_UA_FIRST_PAGE
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApi
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApiItem
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewEndOfPageException
import com.ferelin.novaposhtanews.features.news.uistate.NewsLceState
import com.ferelin.novaposhtanews.features.news.uistate.NewsUiItem
import com.ferelin.novaposhtanews.features.news.uistate.NewsUiState
import com.ferelin.novaposhtanews.features.news.utils.asDbo
import com.ferelin.novaposhtanews.features.news.utils.asUi
import com.ferelin.novaposhtanews.features.news.utils.isUselessState
import com.ferelin.novaposhtanews.features.news.utils.sortedByUserSortPreferences
import com.ferelin.novaposhtanews.features.news.utils.sortedConcatenatedNews
import com.ferelin.novaposhtanews.utils.locale.AppDateUtils
import com.ferelin.novaposhtanews.utils.locale.isRomanian
import com.ferelin.novaposhtanews.utils.takeIfOrEmpty
import java.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import novaposhtanewsdatabase.NewsMdDBO
import novaposhtanewsdatabase.NewsUaDBO

const val NEWS_PIVOT_TO_LOAD_MORE = 15
const val NEWS_PIVOT_TO_SHOW_FAB = 13

class NewsViewModel(
    private val locale: Locale,
    private val appDateUtils: AppDateUtils,
    private val newsMdApi: NewsMdApi,
    private val newsUaContentApi: NewsUaContentApi,
    private val newsUaPreviewApi: NewsUaPreviewApi,
    private val newsMdDao: NewsMdDao,
    private val newsUaDao: NewsUaDao,
    private val userSortPreferencesDatastore: DataStore<UserSortPreferences>,
    private val appPreferencesDatastore: DataStore<AppPreferences>,
    private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState = _uiState.asStateFlow()

    private var nextUaPageToLoad = API_NEWS_UA_FIRST_PAGE

    init {
        initialNewsFetch()

        appPreferencesDatastore.data
            .onEach(::onAppPreferencesChanged)
            .launchIn(viewModelScope)

        userSortPreferencesDatastore.data
            .onEach(::onUserSortPreferencesChanged)
            .launchIn(viewModelScope)

        combine(
            flow = userSortPreferencesDatastore.data,
            flow2 = newsMdDao.news,
            flow3 = newsUaDao.news,
            transform = ::concatenateNews,
        )
            .onEach(::updateUiStateWithNews)
            .flowOn(defaultDispatcher)
            .launchIn(viewModelScope)
    }

    fun onSettingsClick() {
        _uiState.update { it.copy(isSettingsDialogActive = true) }
    }

    fun onSettingsDialogDismiss() {
        _uiState.update { it.copy(isSettingsDialogActive = false) }
    }

    fun onFirstVisibleIndexChanged(index: Int) {
        if (_uiState.value.shouldAutoLoadMoreNews(index)) {
            loadUaNewsNextPage()
        }
        _uiState.update { it.copy(isFabButtonVisible = index > NEWS_PIVOT_TO_SHOW_FAB) }
    }

    fun onRepeatInitialLoadNewsClick() {
        val uiState = _uiState.value
        if (uiState.mdNewsShouldBeUpdated()) fetchMdNews()
        if (uiState.uaNewsShouldBeUpdated()) initUaNewsFirstPageFetch()
    }

    fun onLoadMoreUaNewsClick() {
        loadUaNewsNextPage()
    }

    fun onUaNewsSwitchClick(enabled: Boolean) {
        viewModelScope.launch {
            userSortPreferencesDatastore.updateData {
                it.copy { isUaNewsEnabled = enabled }
            }
        }
    }

    fun onMdNewsSwitchClick(enabled: Boolean) {
        viewModelScope.launch {
            userSortPreferencesDatastore.updateData {
                it.copy { isMdNewsEnabled = enabled }
            }
        }
    }

    fun onAutoLoadSwitchClick(enabled: Boolean) {
        viewModelScope.launch {
            appPreferencesDatastore.updateData {
                it.copy { isNewsAutoLoadEnabled = enabled }
            }
        }
    }

    fun onSortByCategoryClick() {
        viewModelScope.launch {
            userSortPreferencesDatastore.updateData {
                it.copy { isSortByCategoryEnabled = !it.isSortByCategoryEnabled }
            }
        }
    }

    fun onSortByTimestampClick() {
        viewModelScope.launch {
            userSortPreferencesDatastore.updateData {
                it.copy { isAscendingTimestampOrderEnabled = !it.isAscendingTimestampOrderEnabled }
            }
        }
    }

    fun onExpandClick(newsUiItem: NewsUiItem) {
        val alreadyExpanded = _uiState.value.expandedNewsItem
        if (alreadyExpanded == newsUiItem) {
            _uiState.update { it.copy(expandedNewsItem = null) }
        } else {
            _uiState.update { it.copy(expandedNewsItem = newsUiItem) }
            newsUiItem.ifShouldLoadContent { loadContentFor(it) }
        }
    }

    private fun initialNewsFetch() {
        viewModelScope.launch {
            val userPreferences = userSortPreferencesDatastore.data.first()
            val validUserPreferences = fixIfUselessState(userPreferences)

            if (validUserPreferences.isMdNewsEnabled) fetchMdNews()
            if (validUserPreferences.isUaNewsEnabled) initUaNewsFirstPageFetch()
        }
    }

    private fun onAppPreferencesChanged(prefs: AppPreferences) {
        _uiState.update {
            it.copy(isAutoLoadEnabled = prefs.isNewsAutoLoadEnabled)
        }
    }

    private fun onUserSortPreferencesChanged(prefs: UserSortPreferences) {
        _uiState.update {
            it.copy(
                isSortByCategoryEnabled = prefs.isSortByCategoryEnabled,
                isAscendingTimestampOrderEnabled = prefs.isAscendingTimestampOrderEnabled,
                isMdNewsEnabled = prefs.isMdNewsEnabled,
                isUaNewsEnabled = prefs.isUaNewsEnabled,
            )
        }
    }

    private fun fetchMdNews() {
        _uiState.update { it.copy(firstFetchNewsMdLce = NewsLceState.Loading) }

        viewModelScope.launch {
            val fetchedNewsResult = newsMdApi.fetchNews()
            if (fetchedNewsResult.isSuccess) {
                val fetchedNews = fetchedNewsResult.getOrDefault(emptyList())
                onMdNewsFetchComplete(fetchedNews)
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

        val newsDbo = withContext(defaultDispatcher) { fetchedNews.map { it.asDbo(appDateUtils) } }
        newsMdDao.insertAll(newsDbo)
        _uiState.update { it.copy(firstFetchNewsMdLce = NewsLceState.Content) }
    }

    private fun initUaNewsFirstPageFetch() {
        _uiState.update { it.copy(firstFetchNewsUaLce = NewsLceState.Loading) }

        viewModelScope.launch {
            val firstFetchedNewsResult = newsUaPreviewApi.fetchFirst()
            if (firstFetchedNewsResult.isSuccess) {
                fetchUaNewsFirstPageIfNeed(firstFetchedNewsResult.getOrThrow())
            } else {
                _uiState.update { it.copy(firstFetchNewsUaLce = NewsLceState.Error()) }
            }
        }
    }

    private suspend fun fetchUaNewsFirstPageIfNeed(newsUaPreviewApiItem: NewsUaPreviewApiItem) {
        val isAnyUpdates = newsUaDao.getBy(newsUaPreviewApiItem.sourceUrlPath) == null
        if (isAnyUpdates) {
            fetchUaNewsFirstPage()
        } else {
            _uiState.update { it.copy(firstFetchNewsUaLce = NewsLceState.Content) }
        }
    }

    private suspend fun fetchUaNewsFirstPage() {
        val fetchedNewsResult = newsUaPreviewApi.fetchNews(API_NEWS_UA_FIRST_PAGE)

        if (fetchedNewsResult.isSuccess) {
            val fetchedNews = fetchedNewsResult.getOrThrow()
            onUaNewsFirstFetchComplete(fetchedNews)
            nextUaPageToLoad++
        } else {
            _uiState.update { it.copy(firstFetchNewsUaLce = NewsLceState.Error()) }
        }
    }

    private fun loadUaNewsNextPage() {
        viewModelScope.launch {
            _uiState.update { it.copy(newsUaLce = NewsLceState.Loading) }
            val fetchedNewsResult = newsUaPreviewApi.fetchNews(nextUaPageToLoad)

            if (fetchedNewsResult.isSuccess) {
                val fetchedNews = fetchedNewsResult.getOrThrow()
                newsUaDao.insertAll(fetchedNews.map { it.asDbo(appDateUtils) })
                _uiState.update { it.copy(newsUaLce = NewsLceState.Content) }
                nextUaPageToLoad++
            } else {
                onLoadUaNewsPageException(fetchedNewsResult)
            }
        }
    }

    private fun onLoadUaNewsPageException(fetchedNewsResult: Result<List<NewsUaPreviewApiItem>>) {
        when (fetchedNewsResult.exceptionOrNull()) {
            is NewsUaPreviewEndOfPageException -> {
                _uiState.update {
                    it.copy(
                        newsUaLce = NewsLceState.Content,
                        newsUaEndOfPageReached = true,
                    )
                }
            }
            else -> {
                _uiState.update { it.copy(newsUaLce = NewsLceState.Error()) }
            }
        }
    }

    private fun loadContentFor(newsItem: NewsUiItem.Ua) {
        viewModelScope.launch {
            _uiState.update { it.copy(expandedNewsItemContentLce = NewsLceState.Loading) }

            val fetchResult = newsUaContentApi.fetchNewsItemContent(newsItem.sourceUrlPath)
            if (fetchResult.isSuccess) {
                newsUaDao.updateBy(newsItem.sourceUrlPath, fetchResult.getOrThrow().textBlocks)
                _uiState.update { it.copy(expandedNewsItemContentLce = NewsLceState.Content) }
            } else {
                _uiState.update { it.copy(expandedNewsItemContentLce = NewsLceState.Error()) }
            }
        }
    }

    private suspend fun onUaNewsFirstFetchComplete(fetchedNewsItems: List<NewsUaPreviewApiItem>) {
        if (fetchedNewsItems.isEmpty()) {
            _uiState.update { it.copy(firstFetchNewsUaLce = NewsLceState.Error()) }
            return
        }

        newsUaDao.insertAll(fetchedNewsItems.map { it.asDbo(appDateUtils) })
        _uiState.update { it.copy(firstFetchNewsUaLce = NewsLceState.Content) }
    }

    private fun updateUiStateWithNews(news: List<NewsUiItem>) {
        _uiState.update { it.copy(news = news) }
    }

    private fun concatenateNews(
        userSortPreferences: UserSortPreferences,
        newsMd: List<NewsMdDBO>,
        newsUa: List<NewsUaDBO>,
    ): List<NewsUiItem> {
        val newsMdUi = newsMd
            .takeIfOrEmpty(userSortPreferences.isMdNewsEnabled)
            .map { it.asUi(appDateUtils, locale.isRomanian()) }
            .sortedByUserSortPreferences(userSortPreferences)

        val newsUaUi = newsUa
            .takeIfOrEmpty(userSortPreferences.isUaNewsEnabled)
            .map { it.asUi(appDateUtils) }
            .sortedByUserSortPreferences(userSortPreferences)

        return (newsMdUi + newsUaUi).sortedConcatenatedNews(userSortPreferences)
    }

    private suspend fun fixIfUselessState(
        userSortPreferences: UserSortPreferences,
    ): UserSortPreferences {
        return if (userSortPreferences.isUselessState()) {
            userSortPreferencesDatastore.updateData {
                it.copy {
                    isMdNewsEnabled = true
                    isUaNewsEnabled = true
                }
            }
        } else {
            userSortPreferences
        }
    }
}
