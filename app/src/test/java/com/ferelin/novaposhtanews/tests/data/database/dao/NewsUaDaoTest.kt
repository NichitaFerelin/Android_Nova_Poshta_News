package com.ferelin.novaposhtanews.tests.data.database.dao

import com.ferelin.novaposhtanews.NovaPoshtaNewsDatabase
import com.ferelin.novaposhtanews.data.database.dao.NewsUaDao
import com.ferelin.novaposhtanews.utils.KoinBaseTest
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import novaposhtanewsdatabase.NewsUaDBO
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsUaDaoTest : KoinBaseTest() {

    override val koinTestModules: List<Module> = listOf(
        module {
            single<SqlDriver> {
                JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                    NovaPoshtaNewsDatabase.Schema.create(this)
                }
            }
        },
    )

    private val newsUaDao by inject<NewsUaDao>()

    @Test
    fun `news ua dao should be inserted correct and returned as sorted by descending timestamp`() =
        runTest {
            val expected = NewsUaMock.newsMdDboItems.sortedByDescending { it.timestamp }
            newsUaDao.insertAll(expected)
            val actual = newsUaDao.news.first()
            assertTrue { expected == actual }
        }

    @Test
    fun `news ua item should be replaced in case collision`() = runTest {
        val expected = listOf(NewsUaDBO(urlPath = "url", "", 0))
        newsUaDao.insertAll(expected)
        newsUaDao.insertAll(expected)

        val actual = newsUaDao.news.first()
        assertTrue { expected.size == actual.size }
    }

    @Test
    fun `get by source url path should return right item`() = runTest {
        newsUaDao.insertAll(NewsUaMock.newsMdDboItems)
        val expected = NewsUaMock.newsMdDboItems.first()
        val actual = newsUaDao.getBy(expected.urlPath)
        assertTrue { actual == expected }
    }

    @Test
    fun `erase all`() = runTest {
        newsUaDao.insertAll(NewsUaMock.newsMdDboItems)
        newsUaDao.eraseAll()

        val actual = newsUaDao.news.first()
        assertTrue { actual == emptyList<NewsUaDBO>() }
    }
}

private object NewsUaMock {
    val newsMdDboItems = listOf(
        NewsUaDBO(
            urlPath = "urlPath1",
            title = "title1",
            timestamp = 100_000,
        ),
        NewsUaDBO(
            urlPath = "urlPath2",
            title = "title2",
            timestamp = 100_002,
        ),
        NewsUaDBO(
            urlPath = "urlPath3",
            title = "title3",
            timestamp = 100_003,
        ),
        NewsUaDBO(
            urlPath = "urlPath4",
            title = "title4",
            timestamp = 100_004,
        ),
    )
}
