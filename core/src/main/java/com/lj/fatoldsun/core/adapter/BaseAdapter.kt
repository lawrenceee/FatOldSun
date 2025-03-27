package com.lj.fatoldsun.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter4.BaseQuickAdapter

/**
 * @author LJ
 * @time 2025/03/24 17:43
 * @description:
 */
abstract class BaseAdapter<T : Any, VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB // inflate 函数，用于创建 ViewBinding 实例
) : BaseQuickAdapter<T, BaseAdapter.BaseViewHolder<VB>>(mutableListOf()) {



    abstract override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int, item: T?)
    /**
     * 创建 ViewHolder
     * @param parent RecyclerView 作为 ViewGroup，提供上下文和布局参数
     * @return BaseViewHolder 实例
     */
     override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<VB> {
        val binding = inflate(LayoutInflater.from(context), parent, false)
        return BaseViewHolder(binding)
    }

    /**
     * 自定义 ViewHolder，持有 ViewBinding 实例
     * @param binding ViewBinding 实例，用于访问布局中的视图
     */
      class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

     


}