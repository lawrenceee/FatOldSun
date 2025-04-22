package com.lj.fatoldsun.platform.adapter

import android.content.Context
import android.os.Bundle
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

        override fun getChangePayload(oldItem: ArticleItem, newItem: ArticleItem): Any? {
            // 支持局部更新，返回变化的字段 当只有标题变化时，返回 Bundle 标记变化字段。onBindViewHolder 检查 payloads，仅更新标题视图。
            return if (oldItem.title != newItem.title) {
                Bundle().apply { putString("title", newItem.title) }
            } else {
                null
            }
        }


    }) {
    inner class ViewHolder (
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind (item: ArticleItem, payloads: List<Any>) {
            if (payloads.isEmpty()) {
                binding.apply {
                    tvTitle.text = item.title
                    tvAuthor.text = item.author ?: item.shareUser ?: "未知"
                    tvDate.text = item.niceDate ?: ""
                    " ${item.superChapterName ?: ""} / ${item.chapterName ?: ""} ".also { tvCategory.text = it }
                    root.setOnClickListener { onItemClick(item) }
                }
            } else {
                payloads.forEach { payload ->
                    if (payload is Bundle) {
                        payload.getString("title")?.let { binding.tvTitle.text = it }
                    }

                }
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
        //不直接调用bind, 改用带payloads的版本
            holder.bind(item, emptyList())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        getItem(position)?.let { holder.bind(it, payloads) }
    }
}