package com.ferelin.novaposhtanews.data.remote.api.newsuapreview

data class NewsUaPreviewApiItem(
    val urlPath: String,
    val title: String,
    val date: String,
)

class NewsUaPreviewEndOfPageException(page: Int) :
    Exception("News Ua Preview: end of page reached at $page")
