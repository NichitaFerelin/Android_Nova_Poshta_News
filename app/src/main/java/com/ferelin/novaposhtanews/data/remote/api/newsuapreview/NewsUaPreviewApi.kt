package com.ferelin.novaposhtanews.data.remote.api.newsuapreview

import com.ferelin.novaposhtanews.BuildConfig
import com.ferelin.novaposhtanews.data.remote.api.parseNewsUaPreviewApiItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

const val API_NEWS_UA_ARTICLES_PATH = "/news/rubric/2/page/"
const val API_NEWS_UA_FIRST_PAGE = 1
const val API_NEWS_UA_DATE_PATTERN = "dd.MM.yyyy"
private const val HTML_NEWS_UA_ITEM_TAG = "m_news_item"

interface NewsUaPreviewApi {
    suspend fun fetchFirst(): Result<NewsUaPreviewApiItem>
    suspend fun fetchNews(page: Int): Result<List<NewsUaPreviewApiItem>>
}

class NewsUaPreviewApiImpl(
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : NewsUaPreviewApi {

    override suspend fun fetchFirst(): Result<NewsUaPreviewApiItem> = withContext(ioDispatcher) {
        try {
            val connection = Jsoup.connect(
                BuildConfig.BASE_UA_API_URL +
                    API_NEWS_UA_ARTICLES_PATH +
                    API_NEWS_UA_FIRST_PAGE,
            )

            withContext(defaultDispatcher) {
                val document = connection.get()
                val newsItemsElements = document.getElementsByClass(HTML_NEWS_UA_ITEM_TAG)
                val firstNewsElement = newsItemsElements.first() ?: error("News UA empty page")
                Result.success(firstNewsElement.parseNewsUaPreviewApiItem())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchNews(
        page: Int,
    ): Result<List<NewsUaPreviewApiItem>> = withContext(ioDispatcher) {
        try {
            val connection = Jsoup.connect(
                BuildConfig.BASE_UA_API_URL +
                    API_NEWS_UA_ARTICLES_PATH +
                    page,
            )

            withContext(defaultDispatcher) {
                val document = connection.get()
                val newsItemsElements = document.getElementsByClass(HTML_NEWS_UA_ITEM_TAG)

                if (newsItemsElements.isEmpty()) {
                    throw NewsUaPreviewEndOfPageException(page)
                }
                Result.success(
                    newsItemsElements.map { it.parseNewsUaPreviewApiItem() },
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
