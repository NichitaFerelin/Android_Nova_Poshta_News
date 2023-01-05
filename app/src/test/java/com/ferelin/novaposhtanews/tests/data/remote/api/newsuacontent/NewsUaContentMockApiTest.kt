package com.ferelin.novaposhtanews.tests.data.remote.api.newsuacontent

import com.ferelin.novaposhtanews.BuildConfig
import com.ferelin.novaposhtanews.data.remote.api.newsuacontent.NewsUaContentApi
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
internal class NewsUaContentMockApiTest : KoinBaseTest() {

    override val koinTestModules: List<Module> = emptyList()

    private val newsUaContentApi by inject<NewsUaContentApi>()
    private val connection: Connection = mockk()
    private val document: Document = mockk()
    private val element: Element = mockk()

    @Before
    fun setup() {
        mockkStatic(Jsoup::class)
        every { Jsoup.connect(any()) } returns connection
        every { connection.get() } returns document
        every { document.getElementsByClass(any()) } returns Elements(element)
        every { element.children() } returns Elements(element)
        every { element.text() } returns ""
    }

    @After
    fun clear() {
        unmockkStatic(Jsoup::class)
    }

    @Test
    fun `jsoup connect should be called with correct url`() = runTest {
        val sourcePathExpected = "expected"
        newsUaContentApi.fetchNewsItemContent(sourcePathExpected)
        verify { Jsoup.connect(BuildConfig.BASE_UA_API_URL + sourcePathExpected) }
    }

    @Test
    fun `get request should succeed if there were no exceptions`() = runTest {
        val expected = "expected"
        every { element.text() } returns expected
        val result = newsUaContentApi.fetchNewsItemContent("")
        assertTrue { result.isSuccess }
        assertTrue { result.getOrThrow().textBlocks.first() == expected }
    }

    @Test
    fun `get request should return empty text blocks in case content is empty`() = runTest {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        every { element.children() } returns null
        val result = newsUaContentApi.fetchNewsItemContent("")
        assertTrue { result.isSuccess }
        assertTrue { result.getOrThrow().textBlocks.isEmpty() }
    }

    @Test
    fun `fetch news item content should not throw exception when jsoup throws error`() = runTest {
        every { Jsoup.connect(BuildConfig.BASE_UA_API_URL) } throws Exception()
        val result = newsUaContentApi.fetchNewsItemContent("")
        assertTrue { result.isFailure }
    }
}
