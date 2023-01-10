package com.ferelin.novaposhtanews.features.news.di

import com.ferelin.novaposhtanews.data.datastore.di.NAMED_APP_DATASTORE
import com.ferelin.novaposhtanews.data.datastore.di.NAMED_USER_SORT_DATASTORE
import com.ferelin.novaposhtanews.features.news.NewsViewModel
import com.ferelin.novaposhtanews.utils.dispatchers.NAMED_DISPATCHER_DEFAULT
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val newsModule = module {
    viewModel {
        NewsViewModel(
            locale = get(),
            appDateUtils = get(),
            newsMdApi = get(),
            newsUaContentApi = get(),
            newsUaPreviewApi = get(),
            newsMdDao = get(),
            newsUaDao = get(),
            appPreferencesDatastore = get(named(NAMED_APP_DATASTORE)),
            userSortPreferencesDatastore = get(named(NAMED_USER_SORT_DATASTORE)),
            defaultDispatcher = get(named(NAMED_DISPATCHER_DEFAULT)),
        )
    }
}
