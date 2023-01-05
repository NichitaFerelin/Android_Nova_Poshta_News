package com.ferelin.novaposhtanews.tests.data.remote.api.newsmd

import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.novaposhtanews.utils.RESOURCES_NEWS_MD_SUCCESS_JSON
import com.ferelin.novaposhtanews.utils.readFileContent
import kotlin.test.assertTrue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

internal class NewsMdResponseParseTest {

    @Test
    fun `news items json should be parsed success`() {
        val jsonString = javaClass.classLoader!!.readFileContent(RESOURCES_NEWS_MD_SUCCESS_JSON)
        val json = Json { ignoreUnknownKeys = true }
        val parseResult = json.decodeFromString<List<NewsMdApiItem>>(jsonString)
        assertTrue { parseResult.isNotEmpty() }
    }
}
