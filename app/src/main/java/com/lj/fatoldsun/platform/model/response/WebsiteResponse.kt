package com.lj.fatoldsun.platform.model.response

import com.lj.fatoldsun.platform.model.entity.Website

/**
 * https://www.wanandroid.com/friend/json数据
 */
 data class WebsiteResponse(
    val data: List<Website>,
    val errorCode: Int,
    val errorMessage: String
)
