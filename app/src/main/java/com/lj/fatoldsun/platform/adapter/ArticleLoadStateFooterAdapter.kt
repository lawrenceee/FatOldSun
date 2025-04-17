package com.lj.fatoldsun.platform.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lj.fatoldsun.platform.databinding.ItemLoadStateBinding

/**
 * @author LJ
 * @time 2025/04/02 13:42
 * @description:列表加载更多的脚布局适配器
 */
class ArticleLoadStateFooterAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ArticleLoadStateFooterAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry)
    }

    inner class ViewHolder(
        private val binding: ItemLoadStateBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                tvError.isVisible = loadState is LoadState.Error
                btnRetry.isVisible = loadState is LoadState.Error
                tvError.text =
                    if (loadState is LoadState.Error) loadState.error.localizedMessage else ""
                btnRetry.setOnClickListener { retry() }
            }
        }
    }

}