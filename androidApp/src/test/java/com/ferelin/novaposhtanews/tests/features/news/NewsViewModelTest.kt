package com.ferelin.novaposhtanews.tests.features.news

import androidx.datastore.core.DataStore
import com.ferelin.novaposhtanews.AppPreferences
import com.ferelin.novaposhtanews.UserSortPreferences
import com.ferelin.novaposhtanews.appPreferences
import com.ferelin.shared.data.database.dao.NewsMdDao
import com.ferelin.novaposhtanews.data.database.dao.NewsUaDao
import com.ferelin.shared.data.remote.api.newsmd.NewsMdApi
import com.ferelin.shared.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.shared.data.remote.api.newsmd.NewsMdApiTitle
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApi
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApiItem
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewEndOfPageException
import com.ferelin.shared.features.news.NEWS_PIVOT_TO_SHOW_FAB
import com.ferelin.shared.features.news.NewsViewModel
import com.ferelin.shared.features.news.uistate.NewsLceState
import com.ferelin.novaposhtanews.rules.CoroutineRule
import com.ferelin.novaposhtanews.userSortPreferences
import com.ferelin.novaposhtanews.utils.KoinBaseTest
import com.ferelin.shared.utils.NAMED_DISPATCHER_DEFAULT
import com.ferelin.novaposhtanews.utils.locale.AppDateUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.success(
            NewsUaPreviewApiItem("", "", ""),
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.success(
            listOf(NewsUaPreviewApiItem("url", "", "")),
        )
        coEvery { newsMdDao.news } returns flowOf(emptyList())
        coEvery { newsMdDao.insertAll(any()) } returns Unit
        coEvery { newsUaDao.news } returns flowOf(emptyList())
        coEvery { newsUaDao.getBy(any()) } returns null
        coEvery { newsUaDao.eraseAll() } returns Unit
        coEvery { newsUaDao.insertAll(any()) } returns Unit
    }

    @Test
    fun `UI state updates on app preferences changed`() {
        val expected = true
        coEvery { appDatastore.data } returns flowOf(
            appPreferences { isNewsAutoLoadEnabled = expected },
        )

        val actual = viewModel.uiState.value.isAutoLoadEnabled
        assertTrue { actual == expected }
    }

    @Test
    fun `Fetch UA news is called on app preferences changed && shouldAutoLoadMoreNews`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { appDatastore.data } returns flowOf(
            appPreferences { isNewsAutoLoadEnabled = true },
        )

        // trigger lazy initialization
        viewModel

        coVerify(exactly = 1) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `Fetch UA news is not called on app preferences changed && !shouldAutoLoadMoreNews`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = false },
        )

        // trigger lazy initialization
        viewModel

        coVerify(exactly = 0) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `User sort preferences fixed if has useless state on VM initialization`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isUaNewsEnabled = false
                isMdNewsEnabled = false
            },
        )

        // trigger lazy initialization
        viewModel

        coVerify(exactly = 1) { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `UI state updates on user sort preferences changed`() {
        val expected = true
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isUaNewsEnabled = expected
                isMdNewsEnabled = expected
                isSortByCategoryEnabled = expected
                isAscendingTimestampOrderEnabled = expected
            },
        )

        val actual = viewModel.uiState.value
        assertTrue { actual.isUaNewsEnabled == expected }
        assertTrue { actual.isMdNewsEnabled == expected }
        assertTrue { actual.isSortByCategoryEnabled == expected }
        assertTrue { actual.isAscendingTimestampOrderEnabled == expected }
    }

    @Test
    fun `Fetch Md news called on user sort preferences changed && md news should be updated`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
            },
        )

        // trigger lazy initialization
        viewModel

        coVerify(exactly = 1) { newsMdApi.fetchNews() }
    }

    @Test
    fun `Fetch Ua news called on user sort preferences changed && ua news should be updated`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isUaNewsEnabled = true
            },
        )

        // trigger lazy initialization
        viewModel

        coVerify(exactly = 1) { newsUaPreviewApi.fetchFirst() }
    }

    @Test
    fun `UI state updates on md & ua news changes`() {
        val expectedUa = listOf(
            NewsUaDBO("1", "1", 0L),
            NewsUaDBO("2", "3", 0L),
            NewsUaDBO("2", "3", 0L),
        )
        val expectedMd = listOf(
            NewsMdDBO(1, "1", "", "", 0L),
            NewsMdDBO(2, "1", "", "", 0L),
            NewsMdDBO(3, "1", "", "", 0L),
        )

        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isUaNewsEnabled = true
                isMdNewsEnabled = true
            },
        )
        coEvery { newsMdDao.news } returns flowOf(expectedMd)
        coEvery { newsUaDao.news } returns flowOf(expectedUa)

        val actual = viewModel.uiState.value.news
        actual.forEach { target ->
            assertTrue {
                expectedMd.find { it.id == target.key } != null ||
                    expectedUa.find { it.urlPath == target.key } != null
            }
        }
    }

    @Test
    fun `UI state updates isSettingsDialogActive after onSettingsClick`() {
        viewModel.onSettingsClick()
        val actual = viewModel.uiState.value
        assertTrue { actual.isSettingsDialogActive }
    }

    @Test
    fun `UI state updates !isSettingsDialogActive after onSettingsDialogDismiss`() {
        viewModel.onSettingsDialogDismiss()
        val actual = viewModel.uiState.value
        assertFalse { actual.isSettingsDialogActive }
    }

    @Test
    fun `UI states updates firstVisibleNewsIndex on FirstVisibleIndexChanged`() {
        val expected = 101
        viewModel.onFirstVisibleIndexChanged(expected)

        val actual = viewModel.uiState.value
        assertTrue { actual.firstVisibleNewsIndex == expected }
    }

    @Test
    fun `UI states updates isFabButtonVisible on FirstVisibleIndexChanged && index is larger than x`() {
        val indexLargerThanX = NEWS_PIVOT_TO_SHOW_FAB + 1
        viewModel.onFirstVisibleIndexChanged(indexLargerThanX)
        val actual = viewModel.uiState.value
        assertTrue { actual.isFabButtonVisible }
    }

    @Test
    fun `UI states updates isFabButtonVisible on FirstVisibleIndexChanged && index is less than x`() {
        val indexLessThanX = NEWS_PIVOT_TO_SHOW_FAB - 1
        viewModel.onFirstVisibleIndexChanged(indexLessThanX)
        val actual = viewModel.uiState.value
        assertFalse { actual.isFabButtonVisible }
    }

    @Test
    fun `Load news UA page is called if onFirstVisibleIndexChanged && shouldLoadMoreNews`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { appDatastore.data } returns flowOf(
            appPreferences { isNewsAutoLoadEnabled = true },
        )

        viewModel.onFirstVisibleIndexChanged(0)
        coVerify(exactly = 2) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `Load news UA page is not called if onFirstVisibleIndexChanged && !shouldLoadMoreNews`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = false },
        )

        viewModel.onFirstVisibleIndexChanged(0)
        coVerify(exactly = 0) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `Fetch Md news called on RepeatInitialLoadNewsClick && md news should be updated`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isMdNewsEnabled = true
            },
        )

        viewModel.onRepeatInitialLoadNewsClick()
        coVerify(exactly = 2) { newsMdApi.fetchNews() }
    }

    @Test
    fun `Fetch Ua news called on RepeatInitialLoadNewsClick && ua news should be updated`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences {
                isUaNewsEnabled = true
            },
        )

        viewModel.onRepeatInitialLoadNewsClick()
        coVerify(exactly = 1) { newsUaPreviewApi.fetchFirst() }
    }

    @Test
    fun `Fetch UA news called onLoadMoreUaNewsClick`() {
        viewModel.onLoadMoreUaNewsClick()
        coVerify(exactly = 1) { newsUaPreviewApi.fetchNews(any()) }
    }

    @Test
    fun `UserSortPreferences updated after onUaNewsSwitchClick`() {
        viewModel.onUaNewsSwitchClick(true)
        coVerify(exactly = 2) { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `UserSortPreferences updated after onMdNewsSwitchClick`() = runTest {
        viewModel.onMdNewsSwitchClick(true)
        coVerify(exactly = 2) { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `AppPreferences updated after onAutoLoadSwitchClick`() {
        viewModel.onAutoLoadSwitchClick(true)
        coVerify(exactly = 1) { appDatastore.updateData(any()) }
    }

    @Test
    fun `UserSortPreferences updated after onSortByCategoryClick`() {
        viewModel.onSortByCategoryClick()
        coVerify(exactly = 2) { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `UserSortPreferences updated after onSortByTimestampClick`() {
        viewModel.onSortByTimestampClick()
        coVerify(exactly = 2) { userSortDatastore.updateData(any()) }
    }

    @Test
    fun `MdNewsFirstFetchLce is Content on fetch success`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isMdNewsEnabled = true },
        )
        coEvery { newsMdApi.fetchNews() } returns Result.success(
            listOf(NewsMdApiItem(0, "", NewsMdApiTitle("", ""), "")),
        )

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsMdLce is NewsLceState.Content }
    }

    @Test
    fun `Md news cached on fetch news success`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isMdNewsEnabled = true },
        )
        coEvery { newsMdApi.fetchNews() } returns Result.success(
            listOf(NewsMdApiItem(0, "", NewsMdApiTitle("", ""), "")),
        )

        // trigger lazy initialization
        viewModel

        coVerify(exactly = 1) { newsMdDao.insertAll(any()) }
    }

    @Test
    fun `MdNewsFirstFetchLce is Error on fetch failure`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isMdNewsEnabled = true },
        )
        coEvery { newsMdApi.fetchNews() } returns Result.failure(Exception(""))

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsMdLce is NewsLceState.Error }
    }

    @Test
    fun `MdNewsFirstFetchLce is Error on fetch success && empty news list`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isMdNewsEnabled = true },
        )
        coEvery { newsMdApi.fetchNews() } returns Result.success(emptyList())

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsMdLce is NewsLceState.Error }
    }

    @Test
    fun `UaNewsFirstFetchLce is Content on fetchFirst success && fetchNews success`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.success(
            NewsUaPreviewApiItem("", "", ""),
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.success(
            listOf(NewsUaPreviewApiItem("", "", "")),
        )
        coEvery { newsUaDao.getBy(any()) } returns null

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Content }
    }

    @Test
    fun `UA news cached on fetchFirst success && fetchNews success`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.success(
            NewsUaPreviewApiItem("", "", ""),
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.success(
            listOf(NewsUaPreviewApiItem("", "", "")),
        )
        coEvery { newsUaDao.getBy(any()) } returns null

        // trigger lazy initialization
        viewModel

        coVerify(exactly = 1) { newsUaDao.insertAll(any()) }
    }

    @Test
    fun `UaNewsFirstFetchLce is Error on fetchFirst success && fetchNews success && fetched news is empty`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.success(
            NewsUaPreviewApiItem("", "", ""),
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.success(emptyList())
        coEvery { newsUaDao.getBy(any()) } returns null

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Error }
    }

    @Test
    fun `UaNewsFirstFetchLce is Error on fetchFirst success && fetchNews error`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.success(
            NewsUaPreviewApiItem("", "", ""),
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.failure(Exception(""))
        coEvery { newsUaDao.getBy(any()) } returns null

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Error }
    }

    @Test
    fun `UaNewsFirstFetchLce is Content on fetchFirst success && !isAnyUpdates`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.success(
            NewsUaPreviewApiItem("", "", ""),
        )
        coEvery { newsUaDao.getBy(any()) } returns NewsUaDBO("", "", 0L)

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Content }
    }

    @Test
    fun `UaNewsFirstFetchLce is Error on fetchFirst failure`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchFirst() } returns Result.failure(Exception(""))

        val actual = viewModel.uiState.value
        assertTrue { actual.firstFetchNewsUaLce is NewsLceState.Error }
    }

    @Test
    fun `NewsUaLce is Content on fetch success`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.success(
            listOf(NewsUaPreviewApiItem("", "", "")),
        )

        viewModel.onLoadMoreUaNewsClick()

        val actual = viewModel.uiState.value
        assertTrue { actual.newsUaLce is NewsLceState.Content }
    }

    @Test
    fun `UA news cached on fetch success`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.success(
            listOf(NewsUaPreviewApiItem("", "", "")),
        )

        viewModel.onLoadMoreUaNewsClick()

        coVerify(exactly = 2) { newsUaDao.insertAll(any()) }
    }

    @Test
    fun `NewsUaLce is Error on fetch failure`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.failure(Exception(""))

        viewModel.onLoadMoreUaNewsClick()

        val actual = viewModel.uiState.value
        assertTrue { actual.newsUaLce is NewsLceState.Error }
    }

    @Test
    fun `NewsUaLce is Content on fetch failure && NewsUaPreviewEndOfPageException`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.failure(
            NewsUaPreviewEndOfPageException(0),
        )

        viewModel.onLoadMoreUaNewsClick()

        val actual = viewModel.uiState.value
        assertTrue { actual.newsUaLce is NewsLceState.Content }
    }

    @Test
    fun `NewsUaEndOfPageReached is True on fetch failure && NewsUaPreviewEndOfPageException`() {
        coEvery { userSortDatastore.data } returns flowOf(
            userSortPreferences { isUaNewsEnabled = true },
        )
        coEvery { newsUaPreviewApi.fetchNews(any()) } returns Result.failure(
            NewsUaPreviewEndOfPageException(0),
        )

        viewModel.onLoadMoreUaNewsClick()

        val actual = viewModel.uiState.value
        assertTrue { actual.newsUaEndOfPageReached }
    }
}
