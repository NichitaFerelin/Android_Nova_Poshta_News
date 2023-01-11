package com.ferelin.novaposhtanews.features.news.uistate

sealed class NewsUiItem(val timestamp: Long, val key: Any) {

    data class Md(
        val id: Int,
        val urlPath: String,
        val title: String,
        val createdAtTimestamp: Long,
        val createdAt: String,
    ) : NewsUiItem(createdAtTimestamp, id)

    data class Ua(
        val urlPath: String,
        val title: String,
        val createdAtTimestamp: Long,
        val createdAt: String,
    ) : NewsUiItem(createdAtTimestamp, urlPath)
}
