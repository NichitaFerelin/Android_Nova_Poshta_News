package com.ferelin.shared.features.news.utils

import com.ferelin.shared.data.remote.api.newsmd.NewsMdApiItem
import com.ferelin.shared.features.news.uistate.NewsMdUi
import novaposhtanewsdatabase.NewsMdDBO

fun NewsMdDBO.asUi(): NewsMdUi {
    return NewsMdUi(
        id = id,
        urlPath = urlPath,
        title = ruTitle,
        createdAtTimestamp = timestamp,
        createdAt = "",
        url = "",
    )
}

fun NewsMdApiItem.asDbo(): NewsMdDBO {
    return NewsMdDBO(
        id = id,
        urlPath = urlPath,
        ruTitle = title.ru,
        roTitle = title.ro,
        timestamp = 1_000L,
    )
}
