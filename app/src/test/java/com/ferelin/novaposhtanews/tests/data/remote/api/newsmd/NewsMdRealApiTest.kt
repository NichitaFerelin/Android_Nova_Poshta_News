package com.ferelin.novaposhtanews.tests.data.remote.api.newsmd

import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApi
import com.ferelin.novaposhtanews.utils.KoinBaseTest
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.module.Module
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsMdRealApiTest : KoinBaseTest() {

    override val koinTestModules: List<Module> = emptyList()

    private val newsMdApi by inject<NewsMdApi>()

    @Test
    fun `real request to server should be success`() = runTest {
        val result = newsMdApi.fetchNews()
        assertTrue { result.isSuccess }
        assertTrue { result.getOrThrow().isNotEmpty() }
    }
}
