package com.parisa.app.ui.homePage.articlesPage.viewModel

import android.app.Application
import androidx.paging.PagingData
import com.core.base.BaseViewModel
import com.core.dto.article.ArticleDto
import kotlinx.coroutines.flow.Flow


abstract class ArticleViewModel(application: Application) : BaseViewModel(application) {

    abstract fun getArticleData(): Flow<PagingData<ArticleDto>>

}