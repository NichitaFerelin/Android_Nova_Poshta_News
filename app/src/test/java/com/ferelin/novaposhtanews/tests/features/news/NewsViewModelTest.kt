package com.ferelin.novaposhtanews.tests.features.news

import androidx.datastore.core.DataStore
import com.ferelin.novaposhtanews.AppPreferences
import com.ferelin.novaposhtanews.UserSortPreferences
import com.ferelin.novaposhtanews.appPreferences
import com.ferelin.novaposhtanews.data.database.dao.NewsMdDao
import com.ferelin.novaposhtanews.data.database.dao.NewsUaDao
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApi
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiContent
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiMedia
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiSummary
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiTitle
import com.ferelin.novaposhtanews.data.remote.api.newsuacontent.NewsUaContentApi
import com.ferelin.novaposhtanews.data.remote.api.newsuacontent.NewsUaContentResponse
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.API_NEWS_UA_FIRST_PAGE
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApi
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApiItem
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewEndOfPageException
import com.ferelin.novaposhtanews.features.news.NEWS_PIVOT_TO_LOAD_MORE
import com.ferelin.novaposhtanews.features.news.NEWS_PIVOT_TO_SHOW_FAB
import com.ferelin.novaposhtanews.features.news.NewsViewModel
import com.ferelin.novaposhtanews.features.news.uistate.NewsLceState
import com.ferelin.novaposhtanews.features.news.uistate.NewsUiItem
import com.ferelin.novaposhtanews.features.news.utils.asDbo
import com.ferelin.novaposhtanews.rules.CoroutineRule
import com.ferelin.novaposhtanews.userSortPreferences
import com.ferelin.novaposhtanews.utils.KoinBaseTest
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_DEFAULT
import com.ferelin.novaposhtanews.utils.locale.AppDateUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import novaposhtanewsdatabase.NewsMdDBO
import novaposhtanewsdatabase.NewsUaDBO
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsViewModelTest : KoinBaseTest() {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val viewModel: NewsViewModel by inject()
    private val appDateUtils: AppDateUtils by inject()
    private val userSortDatastore: DataStore<UserSortPreferences> = mockk()
    private val appDatastore: DataStore<AppPreferences> = mockk()
    private val newsMdApi: NewsMdApi = mockk()
    private val newsUaContentApi: NewsUaContentApi = mockk()
    private val newsUaPreviewApi: NewsUaPreviewApi = mockk()
    private val newsMdDao: NewsMdDao = mockk()
    private val newsUaDao: NewsUaDao = mockk()

    override val koinTestModules: List<Module> = listOf(
        module {
            factory {
                NewsViewModel(
                    locale = get(),
                    appDateUtils = appDateUtils,
                    newsMdApi = newsMdApi,
                    newsUaContentApi = newsUaContentApi,
                    newsUaPreviewApi = newsUaPreviewApi,
                    newsMdDao = newsMdDao,
                    newsUaDao = newsUaDao,
                    appPreferencesDatastore = appDatastore,
                    userSortPreferencesDatastore = userSortDatastore,
                    defaultDispatcher = get(named(NAMED_DISPATCHER_DEFAULT)),
                )
            }
        },
    )

    @Before
    fun setup() {
        coEvery { userSortDatastore.data } returns flowOf(userSortPreferences { /**/ })
        coEvery { userSortDatastore.updateData(any()) } returns userSortPreferences { /**/ }
        coEvery { appDatastore.data } returns flowOf(appPreferences { /**/ })
        coEvery { appDatastore.updateData(any()) } returns appPreferences { /**/ }
        coEvery { newsMdApi.fetchNews() } returns Result.success(emptyList())
        coEvery { newsUaContentApi.fetchNewsItemContent(any()) } returns Result.success(
            NewsUaContentResponse(emptyList()),
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.success(
            NewsUaPreviewApiItem("", "", ""),
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.success(
            listOf(NewsUaPreviewApiItem("url", "", "")),
        )
        coEvery { newsMdDao.news } returns flowOf(emptyList())
        coEvery { newsMdDao.insertAll(any()) } returns Unit
        coEvery { newsMdDao.eraseAll(any()) } returns Unit
        coEvery { newsUaDao.news } returns flowOf(emptyList())
        coEvery { newsUaDao.updateBy(any(), any()) } returns Unit
        coEvery { newsUaDao.getBy(any()) } returns null
        coEvery { newsUaDao.eraseAll() } returns Unit
        coEvery { newsUaDao.insertAll(any()) } returns Unit
    }

    @Test
    fun `Md & Ua should be updated if sort preferences have useless state on VM initialization`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = false
            },
        )
        viewModel
        coVerify { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `Md & Ua should not be updated if sort preferences have not useless state on VM initialization`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        viewModel
        coVerify(exactly = 0) { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `Fetch Md news is called on VM initialization if isMdNewsEnabled is true`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )
        viewModel
        coVerify { newsMdApi.fetchNews() }
    }

    @Test
    fun `Fetch Md news is called on VM initialization if isMdNewsEnabled is false`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = false
            },
        )
        viewModel
        coVerify(exactly = 0) { newsMdApi.fetchNews() }
    }

    @Test
    fun `Md news insertAll is called of fetch complete & news is not empty`() {
        val returnApi = NewsViewModelDataMock.newsMdApi
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )
        coEvery { newsMdApi.fetchNews() } returns Result.success(returnApi)
        viewModel
        coVerify { newsMdApi.fetchNews() }
        coVerify { newsMdDao.insertAll(returnApi.map { it.asDbo(appDateUtils) }) }
    }

    @Test
    fun `Md news insertAll is not called of fetch complete & news is empty`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )
        coEvery { newsMdApi.fetchNews() } returns Result.success(emptyList())
        viewModel
        coVerify { newsMdApi.fetchNews() }
        coVerify(exactly = 0) { newsMdDao.insertAll(any()) }
    }

    @Test
    fun `firstFetchNewsMdLce is Content on md news fetch complete with success & list is not empty`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )
        coEvery { newsMdApi.fetchNews() } returns Result.success(NewsViewModelDataMock.newsMdApi)

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsMdLce is NewsLceState.Content }
    }

    @Test
    fun `firstFetchNewsMdLce is Error on md news fetch complete with error`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )

        coEvery { newsMdApi.fetchNews() } returns Result.failure(Exception(""))

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsMdLce is NewsLceState.Error }
    }

    @Test
    fun `firstFetchNewsMdLce is Error on md news fetch complete with success & empty list`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )
        coEvery { newsMdApi.fetchNews() } returns Result.success(emptyList())

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsMdLce is NewsLceState.Error }
    }

    @Test
    fun `Fetch first UA news is called on VM initialization if ua news is enabled`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        viewModel
        coVerify { newsUaPreviewApi.fetchFirst() }
    }

    @Test
    fun `Fetch first UA news is not called on VM initialization if ua news is disabled`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )
        viewModel
        coVerify(exactly = 0) { newsUaPreviewApi.fetchFirst() }
    }

    @Test
    fun `Fetch ua news first page is called if firstItemFetch isSuccess && isAnyUpdates`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        coEvery {
            newsUaPreviewApi.fetchFirst()
        } returns Result.success(NewsUaPreviewApiItem("any updates", "", ""))
        viewModel
        coVerify { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `Fetch ua news first page is not called if firstItemFetch isSuccess && !isAnyUpdates`() {
        val returnItemApi = NewsUaPreviewApiItem("source", "", "")
        val returnItemDbo = NewsUaDBO("source", "", 0L, emptyList())

        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.success(returnItemApi)
        coEvery { newsUaDao.getBy(any()) } returns returnItemDbo
        viewModel
        coVerify(exactly = 0) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `Fetch ua news first page is not called if firstItemFetch isFailure`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.failure(Exception(""))
        viewModel
        coVerify(exactly = 0) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `Fetch ua news first page increases nextUaPageToLoad by one`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        viewModel.onLoadMoreUaNewsClick()
        coVerify { newsUaPreviewApi.fetchNews(API_NEWS_UA_FIRST_PAGE) }
        coVerify { newsUaPreviewApi.fetchNews(API_NEWS_UA_FIRST_PAGE + 1) }
    }

    @Test
    fun `Fetch ua news first page not increases nextUaPageToLoad by one if fetch isFailure`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.failure(Exception(""))
        viewModel.onLoadMoreUaNewsClick()
        coVerify(exactly = 2) { newsUaPreviewApi.fetchNews(API_NEWS_UA_FIRST_PAGE) }
    }

    @Test
    fun `Fetch ua news first page calls insert all after success fetch`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        viewModel
        coVerify { newsUaDao.insertAll(any()) }
    }

    @Test
    fun `firstFetchNewsUaLce is Content after fetch ua news complete with success`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Content }
    }

    @Test
    fun `firstFetchNewsUaLce is Error when fetched news items is empty`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.success(emptyList())
        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Error }
    }

    @Test
    fun `firstFetchNewsUaLce is Error when fetchNews isFailure`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.failure(Exception(""))
        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Error }
    }

    @Test
    fun `firstFetchNewsUaLce is Error when fetchFirst isFailure`() {
        coEvery {
            userSortDatastore.data
        } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.failure(Exception(""))
        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Error }
    }

    @Test
    fun `appPreferences ui state updated on VM initialization`() {
        coEvery { appDatastore.data } returns flowOf(
            appPreferences { isNewsAutoLoadEnabled = true },
        )
        val actual = viewModel.uiState.value
        assertTrue { actual.isAutoLoadEnabled }
    }

    @Test
    fun `userSortPreferences ui state updated on VM initialization`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = true
                isSortByCategoryEnabled = true
            },
        )
        val actual = viewModel.uiState.value
        assertTrue { actual.isMdNewsEnabled }
        assertTrue { actual.isUaNewsEnabled }
        assertTrue { actual.isSortByCategoryEnabled }
    }

    @Test
    fun `md & ua news concatenated on VM initialization`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = true
            },
        )

        val expectedMd = NewsMdDBO(0, "", "", "", "", "", "", 0L, emptyList())
        val expectedUa = NewsUaDBO("", "", 0L, emptyList())
        coEvery { newsMdDao.news } returns flowOf(listOf(expectedMd))
        coEvery { newsUaDao.news } returns flowOf(listOf(expectedUa))

        val actual = viewModel.uiState.value
        assertTrue { actual.news.find { it.key == expectedMd.id } != null }
        assertTrue { actual.news.find { it.key == expectedUa.sourceUrlPath } != null }
    }

    @Test
    fun `on settings click activates settings dialog`() {
        viewModel.onSettingsClick()
        val actual = viewModel.uiState.value
        assertTrue { actual.isSettingsDialogActive }
    }

    @Test
    fun `on settings dialog dismiss deactivates settings dialog`() {
        viewModel.onSettingsDialogDismiss()
        val actual = viewModel.uiState.value
        assertFalse { actual.isSettingsDialogActive }
    }

    @Test
    fun `fetch ua next page is called if should load more news`() {
        coEvery { appDatastore.data } returns flowOf(
            appPreferences { isNewsAutoLoadEnabled = true },
        )
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isUaNewsEnabled = true
            }
        )
        viewModel.onFirstVisibleIndexChanged(0)
        coVerify(exactly = 2) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `fetch ua next page is not called if shouldLoadMoreNews is false`() {
        coEvery { appDatastore.data } returns flowOf(
            appPreferences { isNewsAutoLoadEnabled = false },
        )
        viewModel.onFirstVisibleIndexChanged(NEWS_PIVOT_TO_LOAD_MORE + 1)
        coVerify(exactly = 0) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `isFabButtonVisible is changed to true if index is larger than NEWS_PIVOT_TO_SHOW_FAB`() {
        viewModel.onFirstVisibleIndexChanged(NEWS_PIVOT_TO_SHOW_FAB + 1)
        val actual = viewModel.uiState.value
        assertTrue { actual.isFabButtonVisible }
    }

    @Test
    fun `isFabButtonVisible is changed to true if index is less or equal to NEWS_PIVOT_TO_SHOW_FAB`() {
        viewModel.onFirstVisibleIndexChanged(NEWS_PIVOT_TO_SHOW_FAB)
        val actual = viewModel.uiState.value
        assertFalse { actual.isFabButtonVisible }
    }

    @Test
    fun `onRepeatInitialLoadNewsClick fetches md news if mdNewsShouldBeUpdated`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )

        coEvery { newsMdApi.fetchNews() } returns Result.failure(Exception(""))
        viewModel.onRepeatInitialLoadNewsClick()

        coVerify(exactly = 2) { newsMdApi.fetchNews() }
    }

    @Test
    fun `onRepeatInitialLoadNewsClick not fetches md news if mdNewsShouldBeUpdated = false`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )

        viewModel.onRepeatInitialLoadNewsClick()
        coVerify(exactly = 0) { newsMdApi.fetchNews() }
    }

    @Test
    fun `onRepeatInitialLoadNewsClick fetches ua news if uaNewsShouldBeUpdated`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = false
                isUaNewsEnabled = true
            },
        )

        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.failure(Exception(""))
        viewModel.onRepeatInitialLoadNewsClick()

        coVerify(exactly = 2) { newsUaPreviewApi.fetchFirst() }
    }

    @Test
    fun `onRepeatInitialLoadNewsClick not fetches ua news if uaNewsShouldBeUpdated = false`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
                isUaNewsEnabled = false
            },
        )

        viewModel.onRepeatInitialLoadNewsClick()
        coVerify(exactly = 0) { newsUaPreviewApi.fetchFirst() }
    }

    @Test
    fun `onLoadMoreUaNewsClick fetches ua news`() {
        viewModel.onLoadMoreUaNewsClick()
        coVerify(exactly = 1) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `onUaNewsSwitchClick updates user sort preferences`() {
        viewModel.onUaNewsSwitchClick(false)
        coVerify { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `onMdNewsSwitchClick updates user sort preferences`() {
        viewModel.onMdNewsSwitchClick(false)
        coVerify { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `onAutoLoadSwitchClick updates app preferences`() {
        viewModel.onAutoLoadSwitchClick(false)
        coVerify { appDatastore.updateData(any()) }
    }

    @Test
    fun `onSortByTimestampClick updates user sort preferences`() {
        viewModel.onSortByTimestampClick()
        coVerify { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `onSortByCategoryClick updates user sort preferences`() {
        viewModel.onSortByCategoryClick()
        coVerify { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `onExpandClick updates expandedNewsItem with new item`() {
        val expected = NewsUiItem.Ua("", "", 0L, "", emptyList())
        viewModel.onExpandClick(expected)

        val actual = viewModel.uiState.value
        assertTrue { actual.expandedNewsItem == expected }
    }

    @Test
    fun `onExpandClick fetches content for news item if shouldLoadContent`() {
        val expected = NewsUiItem.Ua("", "", 0L, "", emptyList())
        viewModel.onExpandClick(expected)

        coVerify { newsUaContentApi.fetchNewsItemContent(expected.sourceUrlPath) }
    }

    @Test
    fun `onExpandClick not fetches content for news item if shouldLoadContent = false`() {
        val expected = NewsUiItem.Md(0, "", "", "", 0L, "", emptyList())
        viewModel.onExpandClick(expected)

        coVerify(exactly = 0) { newsUaContentApi.fetchNewsItemContent(any()) }
    }

    @Test
    fun `onExpandClick sets expandedNewsItem to null if item already set`() {
        val expected = NewsUiItem.Ua("", "", 0L, "", emptyList())
        viewModel.onExpandClick(expected)
        viewModel.onExpandClick(expected)

        val actual = viewModel.uiState.value
        assertTrue { actual.expandedNewsItem == null }
    }

    @Test
    fun `loadUaNewsNextPage caches data when result is success`() {
        viewModel.onLoadMoreUaNewsClick()
        coVerify(exactly = 1) { newsUaDao.insertAll(any()) }
    }

    @Test
    fun `loadUaNewsNextPage increases nextUaPageToLoad when result is success`() {
        viewModel.onLoadMoreUaNewsClick()
        viewModel.onLoadMoreUaNewsClick()
        coVerify { newsUaPreviewApi.fetchNews(API_NEWS_UA_FIRST_PAGE) }
        coVerify { newsUaPreviewApi.fetchNews(API_NEWS_UA_FIRST_PAGE + 1) }
    }

    @Test
    fun `loadUaNewsNextPage newsUaLce is content when result is success`() {
        viewModel.onLoadMoreUaNewsClick()
        val actual = viewModel.uiState.value
        assertTrue { actual.newsUaLce is NewsLceState.Content }
    }

    @Test
    fun `loadUaNewsNextPage newsUaLce is content & endOfPageReached when isFailure & exception is EndOfPage`() {
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.failure(
            NewsUaPreviewEndOfPageException(0),
        )
        viewModel.onLoadMoreUaNewsClick()

        val actual = viewModel.uiState.value
        assertTrue { actual.newsUaLce is NewsLceState.Content }
        assertTrue { actual.newsUaEndOfPageReached }
    }

    @Test
    fun `loadUaNewsNextPage newsUaLce is error when isFailure & exception !is EndOfPage`() {
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.failure(Exception())
        viewModel.onLoadMoreUaNewsClick()

        val actual = viewModel.uiState.value
        assertTrue { actual.newsUaLce is NewsLceState.Error }
        assertFalse { actual.newsUaEndOfPageReached }
    }

    @Test
    fun `loadContent for caches fetched item content if result is success`() {
        val expected = NewsUiItem.Ua("", "", 0L, "", emptyList())
        viewModel.onExpandClick(expected)

        coVerify { newsUaDao.updateBy(expected.sourceUrlPath, any()) }
    }

    @Test
    fun `loadContent expandedNewsItemContentLce is content if result is success`() {
        val expected = NewsUiItem.Ua("", "", 0L, "", emptyList())
        viewModel.onExpandClick(expected)

        val actual = viewModel.uiState.value
        assertTrue { actual.expandedNewsItemContentLce is NewsLceState.Content }
    }

    @Test
    fun `loadContent expandedNewsItemContentLce is content if result is failure`() {
        coEvery { newsUaContentApi.fetchNewsItemContent(any()) } returns Result.failure(Exception(""))

        val expected = NewsUiItem.Ua("", "", 0L, "", emptyList())
        viewModel.onExpandClick(expected)

        val actual = viewModel.uiState.value
        assertTrue { actual.expandedNewsItemContentLce is NewsLceState.Error }
    }
}

private object NewsViewModelDataMock {
    val newsMdApi = listOf(
        NewsMdApiItem(
            id = 0,
            title = NewsMdApiTitle("ro", "ru"),
            summary = NewsMdApiSummary("ro", "ru"),
            content = NewsMdApiContent("ro", "ru"),
            createdAt = "22.01.12",
            media = listOf(NewsMdApiMedia("media")),
        ),
        NewsMdApiItem(
            id = 1,
            title = NewsMdApiTitle("ro", "ru"),
            summary = NewsMdApiSummary("ro", "ru"),
            content = NewsMdApiContent("ro", "ru"),
            createdAt = "22.01.12",
            media = listOf(NewsMdApiMedia("media")),
        ),
        NewsMdApiItem(
            id = 2,
            title = NewsMdApiTitle("ro", "ru"),
            summary = NewsMdApiSummary("ro", "ru"),
            content = NewsMdApiContent("ro", "ru"),
            createdAt = "22.01.12",
            media = listOf(NewsMdApiMedia("media")),
        ),
    )
}
