package com.ferelin.novaposhtanews.tests.data.remote.api.newsmd

import com.ferelin.shared.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.shared.data.remote.api.newsmd.NewsMdApiTitle
import kotlin.test.assertTrue
import org.junit.Test

internal class NewsMdResponseTest {

    @Test
    fun `NewsMdApiItem properties get & set`() {
        val expectedId = 0
        val expectedUrlPath = "url path"
        val expectedTitle = NewsMdApiTitle("ro", "ru")
        val expectedCreatedAt = "created at"

        val actual = NewsMdApiItem(
            expectedId,
            expectedUrlPath,
            expectedTitle,
            expectedCreatedAt,
        )

        assertTrue { actual.id == expectedId }
        assertTrue { actual.urlPath == expectedUrlPath }
        assertTrue { actual.title == expectedTitle }
        assertTrue { actual.createdAt == expectedCreatedAt }
    }

    @Test
    fun `NewsMdApiTitle properties get & set`() {
        val expectedRo = "ro title"
        val expectedRu = "ru title"
        val actual = NewsMdApiTitle(expectedRo, expectedRu)

        assertTrue { actual.ro == expectedRo }
        assertTrue { actual.ru == expectedRu }
    }
}
