package com.ferelin.novaposhtanews.data.database.dao

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import novaposhtanewsdatabase.NewsMdDBO
import novaposhtanewsdatabase.NewsMdQueries

interface NewsMdDao {
    val news: Flow<List<NewsMdDBO>>
    suspend fun insertAll(newsMdDBOs: List<NewsMdDBO>)
}

class NewsMdDaoImpl(
    private val queries: NewsMdQueries,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
) : NewsMdDao {

    override val news: Flow<List<NewsMdDBO>>
        get() = queries.getAll()
            .asFlow()
            .mapToList(defaultDispatcher)
            .flowOn(ioDispatcher)

    override suspend fun insertAll(newsMdDBOs: List<NewsMdDBO>): Unit = withContext(ioDispatcher) {
        queries.transaction {
            newsMdDBOs.forEach {
                queries.insert(it)
            }
        }
    }
}
