package com.ferelin.novaposhtanews.tests.data.remote.api.newsuacontent

import com.ferelin.novaposhtanews.data.remote.api.newsuacontent.NewsUaContentResponse
import kotlin.test.assertTrue
import org.junit.Test

internal class NewsUaContentResponseTest {

    @Test
    fun `NewsUaContentResponse properties get & set`() {
        val expected = listOf("block 1", "block 2", "block 3")
        val actual = NewsUaContentResponse(expected)
        assertTrue { expected == actual.textBlocks }
    }
}
