package com.core.api

import com.core.dto.ApiResultDto
import com.core.dto.article.ArticleDto
import com.core.dto.article.ArticleWrapperDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET("top-headlines")
    suspend fun getArticles(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("country") country: String,
    ): ArticleWrapperDto


}