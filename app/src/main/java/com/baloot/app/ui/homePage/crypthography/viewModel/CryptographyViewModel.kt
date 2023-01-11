package com.baloot.app.ui.homePage.crypthography.viewModel

import android.app.Application
import androidx.paging.PagingData
import com.core.base.BaseViewModel
import com.core.dto.article.ArticleDto
import kotlinx.coroutines.flow.Flow


abstract class CryptographyViewModel(application: Application) : BaseViewModel(application) {

    abstract fun getArticleData(): Flow<PagingData<ArticleDto>>
    abstract var decryptedText: String
}