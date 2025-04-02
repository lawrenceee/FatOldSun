package com.lj.fatoldsun.platform.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lj.fatoldsun.core.widget.paging.BasePagingAdapter
import com.lj.fatoldsun.platform.databinding.ItemArticleBinding
import com.lj.fatoldsun.platform.model.entity.ArticleItem

/**
 * @author LJ
 * @time 2025/04/01 14:08
 * @description:
 * 文章分页适配器
 */
class ArticlePagingAdapter(private val onItemClick: (ArticleItem) -> Unit) :
    BasePagingAdapter<ArticleItem, ArticlePagingAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<ArticleItem>() { //数据比较器
        override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
            return oldItem.id == newItem.id //基于id比较
        }

        override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
            return oldItem == newItem //基于equals比较
        }


    }) {
    inner class ViewHolder (
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind (item: ArticleItem) {
            binding.apply {
                tvTitle.text = item.title
                tvAuthor.text = item.author ?: item.shareUser ?: "未知"
                tvDate.text = item.niceDate ?: ""
                " ${item.superChapterName ?: ""} / ${item.chapterName ?: ""} ".also { tvCategory.text = it }
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: ArticleItem) {
            holder.bind(item)
    }
}