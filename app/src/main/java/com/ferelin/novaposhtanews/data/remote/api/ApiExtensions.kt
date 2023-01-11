package com.ferelin.novaposhtanews.data.remote.api

import com.ferelin.novaposhtanews.data.remote.api.newsuapreview.NewsUaPreviewApiItem
import org.jsoup.nodes.Element

const val HTML_NEWS_UA_ITEM_URL_TAG = "a"
const val HTML_NEWS_UA_ITEM_URL_ATTR_TAG = "href"
const val HTML_NEWS_UA_TITLE_TAG = "h2"
private const val NEWS_UA_TITLE_DATE_SPLITTER = ' '

fun Element.parseNewsUaPreviewApiItem(): NewsUaPreviewApiItem {
    val title = select(HTML_NEWS_UA_TITLE_TAG).text()
    return NewsUaPreviewApiItem(
        urlPath = select(HTML_NEWS_UA_ITEM_URL_TAG).attr(HTML_NEWS_UA_ITEM_URL_ATTR_TAG),
        title = title.substringBeforeLast(NEWS_UA_TITLE_DATE_SPLITTER),
        date = title.substringAfterLast(NEWS_UA_TITLE_DATE_SPLITTER),
    )
}
