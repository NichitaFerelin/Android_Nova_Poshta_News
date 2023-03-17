package com.ferelin.shared.data.remote.api.newsmd

import com.ferelin.shared.utils.BASE_MD_API_URl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

const val API_NEWS_MD_ARTICLES_PATH = "/articles"
const val API_NEWS_WEBSITE_MD_ARTICLE_PATH = "/article/"
const val API_NEWS_MD_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"

interface NewsMdApi {
    suspend fun fetchNews(): Result<List<NewsMdApiItem>>
}

class NewsMdApiImpl(
    private val httpClient: HttpClient,
    private val ioDispatcher: CoroutineDispatcher,
) : NewsMdApi {

    override suspend fun fetchNews(): Result<List<NewsMdApiItem>> = withContext(ioDispatcher) {
        try {
            Result.success(
                httpClient.get(BASE_MD_API_URl + API_NEWS_MD_ARTICLES_PATH).body(),
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
