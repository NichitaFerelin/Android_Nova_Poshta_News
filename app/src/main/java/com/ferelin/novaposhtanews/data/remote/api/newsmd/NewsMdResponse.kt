package com.ferelin.novaposhtanews.data.remote.api.newsmd

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsMdApiItem(
    @SerialName(value = "id") val id: Int,
    @SerialName(value = "title") val title: NewsMdApiTitle,
    @SerialName(value = "summary") val summary: NewsMdApiSummary,
    @SerialName(value = "content") val content: NewsMdApiContent,
    @SerialName(value = "created_at") val createdAt: String,
    @SerialName(value = "media") val media: List<NewsMdApiMedia>,
)

@Serializable
data class NewsMdApiTitle(
    @SerialName(value = "ro") val ro: String,
    @SerialName(value = "ru") val ru: String,
)

@Serializable
data class NewsMdApiSummary(
    @SerialName(value = "ro") val ro: String,
    @SerialName(value = "ru") val ru: String,
)

@Serializable
data class NewsMdApiContent(
    @SerialName(value = "ro") val ro: String,
    @SerialName(value = "ru") val ru: String,
)

@Serializable
data class NewsMdApiMedia(
    @SerialName(value = "url") val collectionItemUrl: String,
)
