package com.ferelin.novaposhtanews.tests.data.remote.api.newsuacontent

// hide test from CI
/*import com.ferelin.novaposhtanews.data.remote.api.newsuacontent.NewsUaContentApi
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApi
import com.ferelin.novaposhtanews.utils.KoinBaseTest
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.module.Module
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsUaContentRealApiTest : KoinBaseTest() {

    override val koinTestModules: List<Module> = emptyList()

    private val newsUaPreviewApi by inject<NewsUaPreviewApi>()
    private val newsUaContentApi by inject<NewsUaContentApi>()

    @Test
    fun `real request to server should be success`() = runTest {
        val firstItem = newsUaPreviewApi.fetchFirst()
        val firstItemSourcePath = firstItem.getOrThrow().sourceUrlPath

        val result = newsUaContentApi.fetchNewsItemContent(firstItemSourcePath)
        assertTrue { result.isSuccess }
    }
}*/
