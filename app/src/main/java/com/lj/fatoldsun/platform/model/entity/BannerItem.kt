package com.lj.fatoldsun.platform.model.entity

import com.lj.fatoldsun.core.widget.banner.IBannerItem

/**
 * @author LJ
 * @time 2025/04/01 14:31
 * @description: Banner数据模型
 */
data class BannerItem(
    override val imageUrl: String,
    override val title: String,
    override val actionUrl: String
) : IBannerItem