package com.ferelin.novaposhtanews.tests.data.remote.api.newsmd

import com.ferelin.novaposhtanews.BuildConfig
import com.ferelin.novaposhtanews.data.remote.api.newsmd.API_NEWS_MD_ARTICLES_PATH
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiImpl
import com.ferelin.novaposhtanews.utils.RESOURCES_NEWS_MD_SUCCESS_JSON
import com.ferelin.novaposhtanews.utils.readFileContent
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.util.*
import io.ktor.utils.io.*
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.test.KoinTest

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsMdMockApiTest : KoinTest {

    @Test
    fun `get request should be called with correct url`() = runTest {
        val api = NewsMdApiImpl(
            httpClient = HttpClient(
                MockEngine { request ->
                    assertTrue { request.url.toString() == BuildConfig.BASE_MD_API_URL + API_NEWS_MD_ARTICLES_PATH }
                    respond("")
                },
            ),
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        api.fetchNews()
    }

    @Test
    fun `get request should succeed if there were no exceptions`() = runTest {
        val jsonString = javaClass.classLoader!!.readFileContent(RESOURCES_NEWS_MD_SUCCESS_JSON)
        val api = NewsMdApiImpl(
            httpClient = HttpClient(
                MockEngine {
                    respond(
                        content = jsonString,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                },
            ) {
                install(ContentNegotiation) {
                    json(
                        Json { ignoreUnknownKeys = true },
                    )
                }
            },
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val result = api.fetchNews()
        assertTrue { result.isSuccess }
    }


    @Test
    fun `fetch news should not throw exception when http client throws error`() = runTest {
        val api = NewsMdApiImpl(
            httpClient = HttpClient(
                MockEngine {
                    throw Exception()
                },
            ),
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val result = api.fetchNews()
        assertTrue { result.isFailure }
    }
}
