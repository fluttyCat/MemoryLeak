package com.core.dto.article

import com.google.gson.annotations.SerializedName

data class ArticleWrapperDto(
    @SerializedName("status") var status: String,
    @SerializedName("totalResults") var totalResults: Int,
    @SerializedName("articles") var articles: List<ArticleDto>
)
