package com.core.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.api.HomeApi
import com.core.base.BaseRepository
import com.core.dataSource.ArticleListDataSource
import com.core.dto.article.ArticleDto
import kotlinx.coroutines.flow.Flow

abstract class HomeRepository : BaseRepository {

    companion object {
        const val PAGE_SIZE = 5

    }

    abstract fun getArticles(country: String): Flow<PagingData<ArticleDto>>
}

class HomeRepositoryImpl(private val homeApi: HomeApi) : HomeRepository() {

    override fun getArticles(country: String): Flow<PagingData<ArticleDto>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ArticleListDataSource(homeApi) }
        ).flow
    }

}