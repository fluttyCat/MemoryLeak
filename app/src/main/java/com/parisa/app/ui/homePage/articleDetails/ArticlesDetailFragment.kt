package com.parisa.app.ui.homePage.articleDetails

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.core.base.ParentFragment
import com.core.repository.HomeRepository
import com.core.repository.LocalRepository
import com.parisa.app.R
import com.parisa.app.databinding.FragmentArticleDetailBinding
import com.parisa.app.di.DaggerAppComponent
import com.parisa.app.ui.homePage.articlesPage.viewModel.ArticleViewModel
import com.parisa.app.ui.homePage.articlesPage.viewModel.ArticleViewModelImpl
import com.squareup.picasso.Picasso
import javax.inject.Inject


class ArticlesDetailFragment : ParentFragment<ArticleViewModel, FragmentArticleDetailBinding>() {

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var homeRepository: HomeRepository

    private val args: ArticlesDetailFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subscribeArticlesData()
    }


    private fun subscribeArticlesData() {
        dataBinding.tvDetailTitle.text = args.title
        dataBinding.tvDetailSource.text = args.source
        dataBinding.tvDetailPublishedAt.text = args.published
        dataBinding.tvDetailDescription.text = args.description
        Picasso.get().load(args.url).into(dataBinding.ivArticleDetailImage)
    }


    override fun getViewModelClass(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ArticleViewModelImpl(
                    application = requireActivity().application,
                    localRepository = localRepository,
                    homeRepository = homeRepository
                ) as T
            }
        }
    }

    override fun getResourceLayoutId(): Int = R.layout.fragment_article_detail

    override fun inject() {
        DaggerAppComponent.builder()
            .app(requireActivity().application)
            .build()
            .inject(this)
    }


}