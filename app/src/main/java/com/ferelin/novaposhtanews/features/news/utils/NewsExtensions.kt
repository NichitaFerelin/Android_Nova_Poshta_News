package com.ferelin.novaposhtanews.features.news.utils

import com.ferelin.novaposhtanews.UserSortPreferences
import com.ferelin.novaposhtanews.features.news.uistate.NewsUiItem

fun List<NewsUiItem>.sortedByUserSortPreferences(
    userSortPreferences: UserSortPreferences,
): List<NewsUiItem> {
    return if (!userSortPreferences.isSortByCategoryEnabled) {
        this
    } else {
        this.sortByTimestamp(userSortPreferences.isAscendingTimestampOrderEnabled)
    }
}

fun List<NewsUiItem>.sortedConcatenatedNews(
    userSortPreferences: UserSortPreferences,
): List<NewsUiItem> {
    return if (userSortPreferences.isSortByCategoryEnabled) {
        this
    } else {
        this.sortByTimestamp(userSortPreferences.isAscendingTimestampOrderEnabled)
    }
}

fun List<NewsUiItem>.sortByTimestamp(isAscendingTimestampOrder: Boolean): List<NewsUiItem> {
    return if (isAscendingTimestampOrder) {
        this.sortedBy { it.timestamp }
    } else {
        this.sortedByDescending { it.timestamp }
    }
}

fun UserSortPreferences.isUselessState(): Boolean = !this.isMdNewsEnabled && !this.isUaNewsEnabled
