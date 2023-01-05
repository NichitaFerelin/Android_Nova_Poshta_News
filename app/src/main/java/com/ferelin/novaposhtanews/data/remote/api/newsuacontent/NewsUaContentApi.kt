package com.ferelin.novaposhtanews.data.remote.api.newsuacontent

import com.ferelin.novaposhtanews.BuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

private const val HTML_EXPENDED_NEWS_TAG = "text"

interface NewsUaContentApi {
    suspend fun fetchNewsItemContent(sourceUrlPath: String): Result<NewsUaContentResponse>
}

class NewsUaContentApiImpl(
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : NewsUaContentApi {

    override suspend fun fetchNewsItemContent(
        sourceUrlPath: String,
    ): Result<NewsUaContentResponse> = withContext(ioDispatcher) {
        try {
            val connection = Jsoup.connect(BuildConfig.BASE_UA_API_URL + sourceUrlPath)

            withContext(defaultDispatcher) {
                val uaNewsPage = connection.get()

                val targetNewsItemText =
                    uaNewsPage.getElementsByClass(HTML_EXPENDED_NEWS_TAG).first()

                val newsItemTextBlocks = targetNewsItemText?.children() ?: emptyList()
                val result = newsItemTextBlocks.map { it.text() }
                Result.success(NewsUaContentResponse(result))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
