package com.ferelin.shared.features.news.uistate

data class NewsMdUi(
    val id: Int,
    val urlPath: String,
    val title: String,
    val createdAtTimestamp: Long,
    val createdAt: String,
    val url: String,
)
