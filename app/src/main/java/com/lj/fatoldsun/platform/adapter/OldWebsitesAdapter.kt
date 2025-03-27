package com.lj.fatoldsun.platform.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lj.fatoldsun.platform.databinding.ItemWebsitesBinding
import com.lj.fatoldsun.platform.model.Website

/**
 * @author LJ
 * @time 2025/03/21 16:14
 * @description:
 */
class OldWebsitesAdapter : RecyclerView.Adapter<OldWebsitesAdapter.WebsiteHolder>(){
    private var websites: List<Website> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteHolder {
        return WebsiteHolder(ItemWebsitesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = websites.size


    override fun onBindViewHolder(holder: WebsiteHolder, position: Int) {
        holder.bind(websites[position])
    }

    fun updateWebsites(newWebsites: List<Website>) {
        websites = newWebsites
        notifyDataSetChanged()
    }
    class WebsiteHolder(private val binding: ItemWebsitesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(website: Website) {
            binding.apply {
                tvName.text = website.name
                tvLink.text = website.link
                tvCategory.text = website.category
            }
        }

    }

}