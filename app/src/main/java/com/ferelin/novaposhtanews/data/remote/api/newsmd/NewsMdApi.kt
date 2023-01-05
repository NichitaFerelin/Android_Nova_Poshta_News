package com.ferelin.novaposhtanews.data.remote.api.newsmd

import com.ferelin.novaposhtanews.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

const val API_NEWS_MD_ARTICLES_PATH = "/articles"
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
                httpClient.get(BuildConfig.BASE_MD_API_URL + API_NEWS_MD_ARTICLES_PATH).body(),
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
