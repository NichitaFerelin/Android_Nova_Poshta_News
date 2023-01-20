package com.ferelin.novaposhtanews.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.ferelin.novaposhtanews.data.datastore.serializer.AppPreferencesSerializer
import com.ferelin.novaposhtanews.data.datastore.serializer.UserSortPreferencesSerializer
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val NAMED_USER_SORT_DATASTORE = "user_sort_datastore"
const val NAMED_APP_DATASTORE = "app_datastore"

val datastoreModule = module {
    single(qualifier = named(NAMED_USER_SORT_DATASTORE)) {
        DataStoreFactory.create(
            serializer = UserSortPreferencesSerializer(),
            produceFile = { get<Context>().dataStoreFile("user_sort_prefs.pb") },
            corruptionHandler = null,
            scope = CoroutineScope(
                get<CoroutineDispatcher>(named(NAMED_DISPATCHER_IO)) + SupervisorJob(),
            ),
        )
    }
    single(qualifier = named(NAMED_APP_DATASTORE)) {
        DataStoreFactory.create(
            serializer = AppPreferencesSerializer(),
            produceFile = { get<Context>().dataStoreFile("app_prefs.pb") },
            corruptionHandler = null,
            scope = CoroutineScope(
                get<CoroutineDispatcher>(named(NAMED_DISPATCHER_IO)) + SupervisorJob(),
            ),
        )
    }
}
