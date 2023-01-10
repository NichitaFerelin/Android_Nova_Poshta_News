package com.ferelin.novaposhtanews.features.news.utils

import com.ferelin.novaposhtanews.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApiItem
import com.ferelin.novaposhtanews.features.news.uistate.NewsUiItem
import com.ferelin.novaposhtanews.utils.locale.AppDateUtils
import novaposhtanewsdatabase.NewsMdDBO
import novaposhtanewsdatabase.NewsUaDBO

fun NewsUaDBO.asUi(appDateUtils: AppDateUtils): NewsUiItem {
    return NewsUiItem.Ua(
        sourceUrlPath = sourceUrlPath,
        title = title,
        createdAtTimestamp = timestamp,
        createdAt = appDateUtils.timestampToDateStr(timestamp),
        textBlocks = textBlocks,
    )
}

fun NewsUaPreviewApiItem.asDbo(appDateUtils: AppDateUtils): NewsUaDBO {
    return NewsUaDBO(
        sourceUrlPath = sourceUrlPath,
        title = title,
        timestamp = appDateUtils.newsUaDateToTimestamp(date),
        textBlocks = emptyList(),
    )
}

fun NewsMdDBO.asUi(appDateUtils: AppDateUtils, isSystemLanguageRo: Boolean): NewsUiItem {
    return NewsUiItem.Md(
        id = id,
        title = if (isSystemLanguageRo) roTitle else ruTitle,
        summary = if (isSystemLanguageRo) roSummary else ruSummary,
        content = if (isSystemLanguageRo) roContent else ruContent,
        createdAtTimestamp = timestamp,
        createdAt = appDateUtils.timestampToDateStr(timestamp),
        imagesUrls = imagesUrls,
    )
}

fun NewsMdApiItem.asDbo(appDateUtils: AppDateUtils): NewsMdDBO {
    return NewsMdDBO(
        id = id,
        ruTitle = title.ru,
        roTitle = title.ro,
        ruSummary = summary.ru,
        roSummary = summary.ro,
        ruContent = content.ru,
        roContent = content.ro,
        timestamp = appDateUtils.newsMdDateToTimestamp(createdAt),
        imagesUrls = media.map { it.collectionItemUrl },
    )
}
