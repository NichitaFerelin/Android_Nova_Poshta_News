package com.ferelin.novaposhtanews.tests.utils

import com.ferelin.novaposhtanews.utils.takeIfOrEmpty
import kotlin.test.assertTrue
import org.junit.Test

internal class ListExtensionsTest {

    @Test
    fun `takeIfOrEmpty returns source list if predicate is true`() {
        val expected = listOf(1, 2, 3)
        assertTrue { expected.takeIfOrEmpty(true) == expected }
    }

    @Test
    fun `takeIfOrEmpty returns empty list if predicate is false`() {
        val toTest = listOf(1, 2, 3)
        assertTrue { toTest.takeIfOrEmpty(false) == emptyList<Int>() }
    }
}
