package com.core.dto.article

data class Article(
    var source: SourceDto? = null,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null,

)
