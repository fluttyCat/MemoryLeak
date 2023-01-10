package com.core.dto.article

import com.google.gson.annotations.SerializedName

data class ArticleDto(
    @SerializedName("source") var source: SourceDto,
    @SerializedName("author") var author: String,
    @SerializedName("title") var title: String,
    @SerializedName("description") var description: String,
    @SerializedName("url") var url: String,
    @SerializedName("urlToImage") var urlToImage: String,
    @SerializedName("publishedAt") var publishedAt: String,
    @SerializedName("content") var content: String

)
