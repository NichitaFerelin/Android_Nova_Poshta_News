package com.ferelin.novaposhtanews.features.news.uistate

import com.ferelin.novaposhtanews.BuildConfig
import com.ferelin.novaposhtanews.data.remote.api.newsmd.API_NEWS_WEBSITE_MD_ARTICLE_PATH

sealed class NewsUiItem(
    val timestamp: Long,
    val key: Any,
    val url: String,
) {

    data class Md(
        val id: Int,
        val urlPath: String,
        val title: String,
        val createdAtTimestamp: Long,
        val createdAt: String,
    ) : NewsUiItem(
        timestamp = createdAtTimestamp,
        key = id,
        url = BuildConfig.BASE_MD_WEBSITE_URL + API_NEWS_WEBSITE_MD_ARTICLE_PATH + urlPath,
    )

    data class Ua(
        val urlPath: String,
        val title: String,
        val createdAtTimestamp: Long,
        val createdAt: String,
    ) : NewsUiItem(
        timestamp = createdAtTimestamp,
        key = urlPath,
        url = BuildConfig.BASE_UA_API_URL + urlPath,
    )
}
