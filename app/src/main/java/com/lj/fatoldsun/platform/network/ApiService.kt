package com.lj.fatoldsun.platform.network

import com.lj.fatoldsun.core.network.Response
import com.lj.fatoldsun.platform.model.Website
import com.lj.fatoldsun.platform.model.WebsiteResponse
import retrofit2.http.GET

/**
 * @author LJ
 * @time 2025/03/21 13:56
 * @description:
 */
interface ApiService {
    //获取常用网站 https://www.wanandroid.com/friend/json
    @GET("friend/json")
    suspend fun getWebsites(): Response<List<Website>>
}