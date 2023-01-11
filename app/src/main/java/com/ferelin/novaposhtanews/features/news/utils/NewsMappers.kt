package com.ferelin.novaposhtanews.features.news.utils

import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApiItem
import com.ferelin.novaposhtanews.features.news.uistate.NewsUiItem
import com.ferelin.novaposhtanews.utils.locale.AppDateUtils
import novaposhtanewsdatabase.NewsMdDBO
import novaposhtanewsdatabase.NewsUaDBO

fun NewsUaDBO.asUi(appDateUtils: AppDateUtils): NewsUiItem {
    return NewsUiItem.Ua(
        urlPath = urlPath,
        title = title,
        createdAtTimestamp = timestamp,
        createdAt = appDateUtils.timestampToDateStr(timestamp),
    )
}

fun NewsUaPreviewApiItem.asDbo(appDateUtils: AppDateUtils): NewsUaDBO {
    return NewsUaDBO(
        urlPath = urlPath,
        title = title,
        timestamp = appDateUtils.newsUaDateToTimestamp(date),
    )
}

fun NewsMdDBO.asUi(appDateUtils: AppDateUtils, isSystemLanguageRo: Boolean): NewsUiItem {
    return NewsUiItem.Md(
        id = id,
        urlPath = urlPath,
        title = if (isSystemLanguageRo) roTitle else ruTitle,
        createdAtTimestamp = timestamp,
        createdAt = appDateUtils.timestampToDateStr(timestamp),
    )
}

fun NewsMdApiItem.asDbo(appDateUtils: AppDateUtils): NewsMdDBO {
    return NewsMdDBO(
        id = id,
        urlPath = urlPath,
        ruTitle = title.ru,
        roTitle = title.ro,
        timestamp = appDateUtils.newsMdDateToTimestamp(createdAt),
    )
}
