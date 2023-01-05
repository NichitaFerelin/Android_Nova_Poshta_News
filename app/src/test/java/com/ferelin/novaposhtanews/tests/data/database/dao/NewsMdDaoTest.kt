package com.ferelin.novaposhtanews.tests.data.database.dao

import com.ferelin.novaposhtanews.NovaPoshtaNewsDatabase
import com.ferelin.novaposhtanews.data.database.dao.NewsMdDao
import com.ferelin.novaposhtanews.utils.KoinBaseTest
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import novaposhtanewsdatabase.NewsMdDBO
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsMdDaoTest : KoinBaseTest() {

    override val koinTestModules: List<Module> = listOf(
        module {
            single<SqlDriver> {
                JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                    NovaPoshtaNewsDatabase.Schema.create(this)
                }
            }
        },
    )

    private val newsMdDao by inject<NewsMdDao>()

    @Test
    fun `news md dao should be inserted correct and returned sorted by descending`() = runTest {
        val expected = NewsMdMock.newsMdDboItems.sortedByDescending { it.timestamp }
        newsMdDao.insertAll(expected)
        val actual = newsMdDao.news.first()
        assertTrue { expected == actual }
    }

    @Test
    fun `news md dao should be erased correct`() = runTest {
        newsMdDao.insertAll(NewsMdMock.newsMdDboItems)
        newsMdDao.eraseAll(listOf(NewsMdMock.newsMdDboItems.first()))

        val expected = NewsMdMock.newsMdDboItems.subList(1, NewsMdMock.newsMdDboItems.size).reversed()
        val actual = newsMdDao.news.first()
        assertTrue { expected == actual }
    }

    @Test
    fun `news md item should be replaced in case of collision`() = runTest {
        val expected = listOf(NewsMdMock.newsMdDboItems.first())
        newsMdDao.insertAll(expected)
        newsMdDao.insertAll(expected)

        val actual = newsMdDao.news.first()
        assertTrue { expected == actual }
    }
}

private object NewsMdMock {
    val newsMdDboItems = listOf(
        NewsMdDBO(
            id = 1,
            ruTitle = "ruTitle1",
            roTitle = "roTitle1",
            ruSummary = "ruSummary1",
            roSummary = "roSummary1",
            ruContent = "ruContent1",
            roContent = "roContent1",
            timestamp = 100_000,
            imagesUrls = listOf("test1"),
        ),
        NewsMdDBO(
            id = 2,
            ruTitle = "ruTitle2",
            roTitle = "roTitle2",
            ruSummary = "ruSummary2",
            roSummary = "roSummary2",
            ruContent = "ruContent2",
            roContent = "roContent2",
            timestamp = 100_001,
            imagesUrls = listOf("test2"),
        ),
        NewsMdDBO(
            id = 3,
            ruTitle = "ruTitle3",
            roTitle = "roTitle3",
            ruSummary = "ruSummary3",
            roSummary = "roSummary3",
            ruContent = "ruContent3",
            roContent = "roContent3",
            timestamp = 100_002,
            imagesUrls = listOf("test3"),
        ),
        NewsMdDBO(
            id = 4,
            ruTitle = "ruTitle4",
            roTitle = "roTitle4",
            ruSummary = "ruSummary4",
            roSummary = "roSummary4",
            ruContent = "ruContent4",
            roContent = "roContent4",
            timestamp = 100_003,
            imagesUrls = listOf("test4"),
        ),
    )
}
