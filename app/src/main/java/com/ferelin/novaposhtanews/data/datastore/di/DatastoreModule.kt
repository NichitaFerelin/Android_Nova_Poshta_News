package com.ferelin.novaposhtanews.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.ferelin.novaposhtanews.data.datastore.serializer.UserPreferencesSerializer
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val datastoreModule = module {
    single {
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { get<Context>().dataStoreFile("user_prefs.pb") },
            corruptionHandler = null,
            scope = CoroutineScope(
                get<CoroutineDispatcher>(named(NAMED_DISPATCHER_IO)) + SupervisorJob(),
            ),
        )
    }
}
