package com.ferelin.novaposhtanews.data.remote.api.newsmd

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsMdApiItem(
    @SerialName(value = "id") val id: Int,
    @SerialName(value = "path") val urlPath: String,
    @SerialName(value = "title") val title: NewsMdApiTitle,
    @SerialName(value = "created_at") val createdAt: String,
)

@Serializable
data class NewsMdApiTitle(
    @SerialName(value = "ro") val ro: String,
    @SerialName(value = "ru") val ru: String,
)
