package com.lj.fatoldsun.core.widget.paging

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @author LJ
 * @time 2025/03/31 17:08
 * @description:
 * 通用的 Paging 适配器，基于 Paging 3 和 BRVAH
 * 作用：提供分页加载的通用实现，支持任意数据类型
 * @param T 数据类型，泛型设计提升复用性
 * @param VH ViewHolder 类型，基于 QuickViewHolder
 * @param diffCallback DiffUtil 比较器，用于高效更新数据
 */
abstract class BasePagingAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T> //传入比较器，定义数据差异逻辑
) : PagingDataAdapter<T, VH>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(parent.context, parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { onBindViewHolder(holder, it) }
    }

    /**
     * 创建 ViewHolder，由子类实现
     * @param context 上下文
     * @param parent 父容器
     * @param viewType 视图类型
     */
    abstract fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int) : VH
    /**
     * 绑定数据，由子类实现
     * @param holder ViewHolder
     * @param item 数据项
     */
    abstract fun onBindViewHolder(holder: VH, item: T)
}