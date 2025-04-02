package com.lj.fatoldsun.platform.network

import com.lj.fatoldsun.core.network.Response
import com.lj.fatoldsun.platform.model.entity.Website
import com.lj.fatoldsun.platform.model.response.ArticleResponse
import com.lj.fatoldsun.platform.model.response.BannerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author LJ
 * @time 2025/03/21 13:56
 * @description:
 */
interface ApiService {
    //获取常用网站 https://www.wanandroid.com/friend/json
    @GET("friend/json")
    suspend fun getWebsites(): Response<List<Website>>

    //https://www.wanandroid.com/banner/json
    @GET("banner/json")
    suspend fun getBanners() : Response<List<BannerResponse>>

    /**
     * 首页文章列表
     * 注意：页码从0开始，拼接在链接上。
     *
     * 注：该接口支持传入 page_size 控制分页数量，取值为[1-40]，不传则使用默认值，一旦传入了 page_size，后续该接口分页都需要带上，否则会造成分页读取错误。
     *
     * 其中有两个易混淆的字段:
     *
     * "superChapterId": 153,
     * "superChapterName": "framework", // 一级分类的名称
     * https://www.wanandroid.com/article/list/0/json
     */
    @GET("article/list/{page}/json")
    suspend fun getArticles(
        @Path ("page") page: Int, // 页码
        @Query("page_size") pageSize: Int = 40 //每页大小
    ) : Response<PagingResponse<ArticleResponse>>
}