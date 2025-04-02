package com.lj.fatoldsun.core.widget.banner

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter

/**
 * @author LJ
 * @time 2025/03/31 16:11
 * @description:
 * Banner 图片适配器，使用 Glide 加载图片
 */
class BannerImageAdapter(data: List<IBannerItem>) : BannerAdapter<IBannerItem, BannerImageAdapter.BannerImageHolder>(data) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerImageHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return BannerImageHolder(imageView)
    }

    override fun onBindView(
        holder: BannerImageHolder,
        data: IBannerItem,
        position: Int,
        size: Int
    ) {
        Glide.with(holder.imageView)
            .load(data.imageUrl)
            .into(holder.imageView)
    }

    /**
     * Banner 图片 ViewHolder
     * 作用：持有 ImageView 实例，供适配器复用
     */
    class BannerImageHolder(
        val imageView: ImageView  // ImageView 实例，显示单张 Banner 图片
    ) : RecyclerView.ViewHolder(imageView)  // 继承 RecyclerView.ViewHolder
}