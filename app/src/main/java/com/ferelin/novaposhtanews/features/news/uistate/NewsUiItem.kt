package com.ferelin.novaposhtanews.features.news.uistate

sealed class NewsUiItem(val timestamp: Long, val key: Any) {

    data class Md(
        val id: Int,
        val title: String,
        val summary: String,
        val content: String,
        val createdAtTimestamp: Long,
        val createdAt: String,
        val imagesUrls: List<String>,
    ) : NewsUiItem(createdAtTimestamp, id)

    data class Ua(
        val sourceUrlPath: String,
        val title: String,
        val createdAtTimestamp: Long,
        val createdAt: String,
        val textBlocks: List<String>,
    ) : NewsUiItem(createdAtTimestamp, sourceUrlPath)

    fun ifShouldLoadContent(block: (Ua) -> Unit) {
        if (this is Ua && this.textBlocks.isEmpty()) {
            block(this)
        }
    }
}
