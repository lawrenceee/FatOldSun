package com.lj.fatoldsun.platform.adapter

import com.lj.fatoldsun.core.adapter.BaseAdapter
import com.lj.fatoldsun.platform.databinding.ItemWebsitesBinding
import com.lj.fatoldsun.platform.model.Website

/**
 * @author LJ
 * @time 2025/03/24 21:06
 * @description:
 */
class WebsitesAdapter : BaseAdapter<Website, ItemWebsitesBinding>(
    inflate =  { inflater, viewGroup, _ ->
        ItemWebsitesBinding.inflate(inflater, viewGroup, false)}) {
    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemWebsitesBinding>,
        position: Int,
        item: Website?
    ) {
        item?.let {
            holder.binding.tvName.text = it.name
            holder.binding.tvCategory.text = it.category
            holder.binding.tvLink.text = it.link
        }
    }

    fun updateWebsites(newWebsites: List<Website>) {
        submitList(newWebsites) // 使用 BRVAH 的高效更新方法
    }


}

