package com.ferelin.novaposhtanews.tests.data.remote.api.newsuapreview

import com.ferelin.novaposhtanews.BuildConfig
import com.ferelin.novaposhtanews.data.remote.api.HTML_NEWS_UA_ITEM_URL_TAG
import com.ferelin.novaposhtanews.data.remote.api.HTML_NEWS_UA_TITLE_TAG
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.API_NEWS_UA_ARTICLES_PATH
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.API_NEWS_UA_FIRST_PAGE
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApi
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewEndOfPageException
import com.ferelin.novaposhtanews.utils.KoinBaseTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.module.Module
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsUaPreviewMockApiTest : KoinBaseTest() {

    override val koinTestModules: List<Module> = emptyList()

    private val newsUaPreviewApi by inject<NewsUaPreviewApi>()
    private val connection: Connection = mockk()
    private val document: Document = mockk()
    private val element: Element = mockk()
    private val elements: Elements = mockk()

    @Before
    fun setup() {
        mockkStatic(Jsoup::class)
        every { Jsoup.connect(any()) } returns connection
        every { connection.get() } returns document
        every { document.getElementsByClass(any()) } returns Elements(element)
        every { element.select(HTML_NEWS_UA_TITLE_TAG) } returns Elements(element)
        every { element.text() } returns ""
        every { element.select(HTML_NEWS_UA_ITEM_URL_TAG) } returns elements
        every { elements.attr(any()) } returns ""
    }

    @After
    fun clear() {
        unmockkStatic(Jsoup::class)
    }

    @Test
    fun `fetch first connect should be called with correct url`() = runTest {
        newsUaPreviewApi.fetchFirst()
        verify {
            Jsoup.connect(
                BuildConfig.BASE_UA_API_URL +
                    API_NEWS_UA_ARTICLES_PATH +
                    API_NEWS_UA_FIRST_PAGE,
            )
        }
    }

    @Test
    fun `fetched first news html should be parsed successful`() = runTest {
        val fullTitle = "test 11"
        val expectedTitle = "test"
        val expectedDate = "11"
        val expectedPath = "path"

        every { element.select(HTML_NEWS_UA_TITLE_TAG) } returns Elements(element)
        every { element.text() } returns fullTitle
        every { element.select(HTML_NEWS_UA_ITEM_URL_TAG) } returns elements
        every { elements.attr(any()) } returns expectedPath

        val result = newsUaPreviewApi.fetchFirst()
        val resultItem = result.getOrThrow()
        assertTrue { result.isSuccess }
        assertTrue { resultItem.title == expectedTitle }
        assertTrue { resultItem.date == expectedDate }
        assertTrue { resultItem.sourceUrlPath == expectedPath }
    }

    @Test
    fun `fetch first should succeed if there were no exceptions`() = runTest {
        val result = newsUaPreviewApi.fetchFirst()
        assertTrue { result.isSuccess }
    }

    @Test
    fun `fetch first should not throw exception when jsoup throws error`() = runTest {
        every { Jsoup.connect(any()) } throws Exception()
        val result = newsUaPreviewApi.fetchFirst()
        assertTrue { result.isFailure }
    }

    @Test
    fun `fetch news connect should be called with correct url`() = runTest {
        val expectedPage = 1
        newsUaPreviewApi.fetchNews(expectedPage)
        verify {
            Jsoup.connect(
                BuildConfig.BASE_UA_API_URL +
                    API_NEWS_UA_ARTICLES_PATH +
                    expectedPage,
            )
        }
    }

    @Test
    fun `fetched news html should be parsed successful`() = runTest {
        val fullTitle = "test 11"
        val expectedTitle = "test"
        val expectedDate = "11"
        val expectedPath = "path"

        every { element.select(HTML_NEWS_UA_TITLE_TAG) } returns Elements(element)
        every { element.text() } returns fullTitle
        every { element.select(HTML_NEWS_UA_ITEM_URL_TAG) } returns elements
        every { elements.attr(any()) } returns expectedPath

        val result = newsUaPreviewApi.fetchNews(1)
        val resultItem = result.getOrThrow().first()
        assertTrue { result.isSuccess }
        assertTrue { resultItem.title == expectedTitle }
        assertTrue { resultItem.date == expectedDate }
        assertTrue { resultItem.sourceUrlPath == expectedPath }
    }

    @Test
    fun `fetch news should succeed if there were no exceptions`() = runTest {
        val result = newsUaPreviewApi.fetchNews(1)
        assertTrue { result.isSuccess }
    }

    @Test
    fun `fetch news should not throw exception when jsoup throws error`() = runTest {
        every { Jsoup.connect(any()) } throws Exception()
        val result = newsUaPreviewApi.fetchNews(1)
        assertTrue { result.isFailure }
    }

    @Test
    fun `NewsUaPreviewEndOfPageException should be thrown when content is empty`() = runTest {
        every { document.getElementsByClass(any()) } returns Elements(emptyList())
        val result = newsUaPreviewApi.fetchNews(1)
        assertTrue { result.isFailure }
        assertTrue { result.exceptionOrNull() is NewsUaPreviewEndOfPageException }
    }
}
