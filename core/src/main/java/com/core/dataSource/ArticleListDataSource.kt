package com.core.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.api.HomeApi
import com.core.dto.article.ArticleDto

class ArticleListDataSource(
    private val homePageApi: HomeApi
) : PagingSource<Int, ArticleDto>() {

    override fun getRefreshKey(state: PagingState<Int, ArticleDto>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleDto> {
        return try {
            val page = params.key ?: 1 // set page 1 as default
            val pageSize = params.loadSize
            val packagesResponse = homePageApi.getArticles(page, pageSize, "us")
            val packagesItems = packagesResponse.articles
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (packagesItems.isNotEmpty()) page + 1 else null
            LoadResult.Page(packagesItems, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}


/*
* override fun getRefreshKey(state: PagingState<Int, PackagesDto>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PackagesDto> {
        val pageNumber = params.key ?: 1
        val result =
            categoryRepository.getPackagesRecyclerHome(
                PackagesRequestDto(
                    pageNumber, NETWORK_PAGE_SIZE
                )
            )

        return when (result.networkState) {
            Status.FAILED -> {
                LoadResult.Error(Exception(result.networkState.msg))
            }
            else -> {
                val data = result.onSuccess
                val nextPageNumber: Int? = data.let { res ->
                    if (res.size >= NETWORK_PAGE_SIZE) {
                        pageNumber + 1
                    } else {
                        null
                    }
                } ?: kotlin.run {
                    null
                }
                LoadResult.Page(
                    data = data.orEmpty(),
                    prevKey = if (pageNumber == 1) null else pageNumber - 1,
                    nextKey = nextPageNumber
                )
            }
        }
    }*/