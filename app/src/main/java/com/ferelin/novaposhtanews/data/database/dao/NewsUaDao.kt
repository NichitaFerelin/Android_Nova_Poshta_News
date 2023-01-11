package com.ferelin.novaposhtanews.data.database.dao

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import novaposhtanewsdatabase.NewsUaDBO
import novaposhtanewsdatabase.NewsUaQueries

interface NewsUaDao {
    val news: Flow<List<NewsUaDBO>>
    suspend fun insertAll(newsUaDBOs: List<NewsUaDBO>)
    suspend fun getBy(sourceUrlPath: String): NewsUaDBO?
    suspend fun eraseAll()
}

class NewsUaDaoImpl(
    private val queries: NewsUaQueries,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
) : NewsUaDao {

    override val news: Flow<List<NewsUaDBO>>
        get() = queries.getAll()
            .asFlow()
            .mapToList(defaultDispatcher)
            .flowOn(ioDispatcher)

    override suspend fun insertAll(newsUaDBOs: List<NewsUaDBO>): Unit = withContext(ioDispatcher) {
        queries.transaction {
            newsUaDBOs.forEach {
                queries.insert(it)
            }
        }
    }

    override suspend fun getBy(sourceUrlPath: String): NewsUaDBO? = withContext(ioDispatcher) {
        queries.getBy(sourceUrlPath).executeAsOneOrNull()
    }

    override suspend fun eraseAll(): Unit = withContext(ioDispatcher) {
        queries.eraseAll()
    }
}
