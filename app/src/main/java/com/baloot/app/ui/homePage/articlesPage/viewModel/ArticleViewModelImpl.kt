package com.baloot.app.ui.homePage.articlesPage.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.core.dto.NetworkState
import com.core.dto.article.ArticleDto
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import kotlinx.coroutines.flow.Flow


class ArticleViewModelImpl(
    application: Application,
    private var localRepository: LocalRepository,
    private var homeRepository: HomeRepository
) : ArticleViewModel(
    application
) {


    override fun getNetworkStatus(): LiveData<NetworkState> =
        MediatorLiveData<NetworkState>().apply {

        }

    override fun getArticleData(): Flow<PagingData<ArticleDto>> {
        return homeRepository.getArticles("us").cachedIn(viewModelScope)
    }


}