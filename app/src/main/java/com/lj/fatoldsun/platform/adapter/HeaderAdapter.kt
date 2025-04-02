package com.lj.fatoldsun.platform.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author LJ
 * @time 2025/04/02 14:20
 * @description:
 * RecyclerView的头布局适配器，这里用来装Banner视图
 * 只显示一个视图（headerView），即 BannerView。
 */
class HeaderAdapter(
    private val headerView: View
) : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(headerView)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 头部视图已经通过 headerView 传入，无需绑定数据
    }
}