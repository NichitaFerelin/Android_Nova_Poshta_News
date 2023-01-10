package com.ferelin.novaposhtanews.utils.locale.di

import com.ferelin.novaposhtanews.data.remote.api.newsmd.API_NEWS_MD_DATE_PATTERN
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.API_NEWS_UA_DATE_PATTERN
import com.ferelin.novaposhtanews.utils.locale.AppDateUtils
import com.ferelin.novaposhtanews.utils.locale.NEWS_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*
import org.koin.dsl.module

val localeModule = module {
    factory { Locale.getDefault() }
    factory {
        AppDateUtils(
            newsDateFormat = SimpleDateFormat(NEWS_DATE_FORMAT, get<Locale>()),
            newsMdDateFormat = SimpleDateFormat(API_NEWS_MD_DATE_PATTERN, get<Locale>()),
            newsUaDateFormat = SimpleDateFormat(API_NEWS_UA_DATE_PATTERN, get<Locale>()),
        )
    }
}
