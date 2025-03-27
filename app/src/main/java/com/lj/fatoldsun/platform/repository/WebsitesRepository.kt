package com.lj.fatoldsun.platform.repository

import com.lj.fatoldsun.core.network.NetworkClient
import com.lj.fatoldsun.core.network.Response
import com.lj.fatoldsun.platform.db.WebsitesDao
import com.lj.fatoldsun.platform.model.Website
import com.lj.fatoldsun.platform.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author LJ
 * @time 2025/03/24 22:27
 * @description:网站链接数据仓库，封装网络请求和数据库操作
 */
@Singleton
class WebsitesRepository @Inject constructor(
    private val websitesDao: WebsitesDao
) {
    private val apiService = NetworkClient.createService(ApiService::class.java)

    /**
     * 从接口获取网站数据
     */
    suspend fun fetchWebsitesFromNetwork() : Response<List<Website>>{
        return apiService.getWebsites()
    }

    /**
     * 从本地数据库获取网站数据
     */
    suspend fun getCacheWebsitesFromLocal() : List<Website> {
        return websitesDao.getAllWebsites()
    }

    suspend fun saveWebsites(websites: List<Website>) {
        websitesDao.clearWebsites()
        websitesDao.insertWebsites(websites)
    }
}