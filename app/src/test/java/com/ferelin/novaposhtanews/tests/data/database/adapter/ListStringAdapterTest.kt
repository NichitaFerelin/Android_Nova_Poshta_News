package com.ferelin.novaposhtanews.tests.data.database.adapter

import com.ferelin.novaposhtanews.data.database.adapter.LIST_STRING_ADAPTER_SEPARATOR
import com.ferelin.novaposhtanews.data.database.adapter.ListStringAdapter
import kotlin.test.assertTrue
import org.junit.Test

internal class ListStringAdapterTest {

    private val adapter = ListStringAdapter()

    @Test
    fun `decode string should be correct`() {
        val result = adapter.decode(ListStringAdapterMock.singleString)
        assertTrue { result == ListStringAdapterMock.strings }
    }

    @Test
    fun `encode list of strings should be correct`() {
        val result = adapter.encode(ListStringAdapterMock.strings)
        assertTrue { result == ListStringAdapterMock.singleString }
    }
}

private object ListStringAdapterMock {
    val strings = listOf("ab", "cd", "ef")
    const val singleString =
        "ab" + LIST_STRING_ADAPTER_SEPARATOR + "cd" + LIST_STRING_ADAPTER_SEPARATOR + "ef"
}
