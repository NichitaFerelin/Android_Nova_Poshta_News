package com.ferelin.novaposhtanews.tests.data.remote.api.newsmd

import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiContent
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiMedia
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiSummary
import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiTitle
import kotlin.test.assertTrue
import org.junit.Test

internal class NewsMdResponseTest {

    @Test
    fun `NewsMdApiItem properties get & set`() {
        val expectedId = 0
        val expectedTitle = NewsMdApiTitle("ro", "ru")
        val expectedSummary = NewsMdApiSummary("ro", "ru")
        val expectedContent = NewsMdApiContent("ro", "ru")
        val expectedCreatedAt = "created at"
        val expectedMedia = listOf(NewsMdApiMedia("url"))

        val actual = NewsMdApiItem(
            expectedId,
            expectedTitle,
            expectedSummary,
            expectedContent,
            expectedCreatedAt,
            expectedMedia,
        )

        assertTrue { actual.id == expectedId }
        assertTrue { actual.title == expectedTitle }
        assertTrue { actual.summary == expectedSummary }
        assertTrue { actual.content == expectedContent }
        assertTrue { actual.createdAt == expectedCreatedAt }
        assertTrue { actual.media == expectedMedia }
    }

    @Test
    fun `NewsMdApiTitle properties get & set`() {
        val expectedRo = "ro title"
        val expectedRu = "ru title"
        val actual = NewsMdApiTitle(expectedRo, expectedRu)

        assertTrue { actual.ro == expectedRo }
        assertTrue { actual.ru == expectedRu }
    }

    @Test
    fun `NewsMdApiSummary properties get & set`() {
        val expectedRo = "ro summary"
        val expectedRu = "ru summary"
        val actual = NewsMdApiSummary(expectedRo, expectedRu)

        assertTrue { actual.ro == expectedRo }
        assertTrue { actual.ru == expectedRu }
    }

    @Test
    fun `NewsMdApiContent properties get & set`() {
        val expectedRo = "ro content"
        val expectedRu = "ru content"
        val actual = NewsMdApiContent(expectedRo, expectedRu)

        assertTrue { actual.ro == expectedRo }
        assertTrue { actual.ru == expectedRu }
    }

    @Test
    fun `NewsMdApiMedia properties get & set`() {
        val expected = "url 1"
        val actual = NewsMdApiMedia(expected)
        assertTrue { actual.collectionItemUrl == expected }
    }
}
