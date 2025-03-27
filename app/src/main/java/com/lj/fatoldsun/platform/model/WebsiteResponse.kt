package com.lj.fatoldsun.platform.model

/**
 * https://www.wanandroid.com/friend/json数据
 */
 data class WebsiteResponse(
    val data: List<Website>,
    val errorCode: Int,
    val errorMessage: String
)
