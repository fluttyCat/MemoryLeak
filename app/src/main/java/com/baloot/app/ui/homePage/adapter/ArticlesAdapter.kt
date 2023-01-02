package com.baloot.app.ui.homePage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.baloot.app.databinding.ItemArticlePreviewBinding
import com.core.base.adapter.BaseHolder
import com.core.dto.article.Article

class ArticlesAdapter(private val itemClickCallback: (Article) -> Unit) :
    PagingDataAdapter<Article, BaseHolder<Article>>(object :
        DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem == newItem
        }

    }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Article> {

        return ArticleViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: BaseHolder<Article>, position: Int) {
        holder.bind(getItem(position)!!, position)
    }


    inner class ArticleViewHolder(
        private val binding: ItemArticlePreviewBinding
    ) : BaseHolder<Article>(binding) {

        override fun bind(value: Article, position: Int) {
            binding.articleItem = value

            binding.root.setOnClickListener {
                itemClickCallback(value)
            }


        }


    }

}
