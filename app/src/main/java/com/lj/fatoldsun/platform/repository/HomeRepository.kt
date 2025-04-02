package com.lj.fatoldsun.platform.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.lj.fatoldsun.core.network.NetworkClient
import com.lj.fatoldsun.core.network.Response
import com.lj.fatoldsun.core.utils.Logger
import com.lj.fatoldsun.platform.db.AppDatabase
import com.lj.fatoldsun.platform.model.entity.ArticleItem
import com.lj.fatoldsun.platform.model.response.ArticleResponse
import com.lj.fatoldsun.platform.model.response.BannerResponse
import com.lj.fatoldsun.platform.network.ApiService
import com.lj.fatoldsun.platform.network.PagingResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author LJ
 * @time 2025/04/01 15:14
 * @description:
 * 首页数据仓库
 */
@Singleton
class HomeRepository @Inject constructor(private val database: AppDatabase) {
    private val apiService = NetworkClient.createService(ApiService::class.java)

    /**
     * 获取banner数据
     */
    suspend fun getBanners(): Response<List<BannerResponse>> {
        return apiService.getBanners()
    }

    /**
     * 获取纯网络文章数据
     * @return Paging 数据流
     */
    fun getArticlesFromNetwork(): Flow<PagingData<ArticleItem>> {
        //创建pager
        return Pager(
            config = PagingConfig(
                pageSize = 40, //每页大小
                prefetchDistance = 5, // 距离底部 5 项时开始预加载
                enablePlaceholders = false //禁用占位符
            ),
            pagingSourceFactory = { ArticlePagingSource(apiService) }
        ).flow
    }

    /**
     * 获取网络+本地文章数据
     * @return Paging 数据流
     */

    @OptIn(ExperimentalPagingApi::class)
    fun getArticlesWithMediator(): Flow<PagingData<ArticleItem>> {
        //创建pager
        return Pager(
            config = PagingConfig(
                pageSize = 40,
                enablePlaceholders = false
            ),
            remoteMediator = ArticleRemoteMediator(apiService, database), //混合数据源
            pagingSourceFactory = { database.articlesDao().getArticles() } //本地数据源
        ).flow
    }


}

/**
 * 文章 RemoteMediator
 * 作用：协调网络和本地数据
 */
@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val apiService: ApiService,
    private val database: AppDatabase
) : RemoteMediator<Int, ArticleItem>() {

    /**
     * 加载数据
     * @param loadType 加载类型
     * @param state Paging 状态
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 0 //从0开始
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true) //不支持前置加载
            LoadType.APPEND -> {
                // 计算下一页页码
                // 作用：基于已加载的页面数量确定下一页
                // 原理：state.pages 是已加载的所有页面，size 是页面数量，接口从 0 开始，所以 size 就是下一页页码
                state.pages.size
            }
        }

       return try {
            val response = apiService.getArticles(page)
            if (response.isSuccess()) {
                val articles = responseMapToArticleItem(response)

                database.withTransaction {
                    //如果是刷新状态，清空数据
                    if (loadType == LoadType.REFRESH) {
                        database.articlesDao().clearAll()
                    }

                    database.articlesDao().insertArticles(articles)
                }
                MediatorResult.Success(endOfPaginationReached = response.data.over)
            } else {
                MediatorResult.Error(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            // 记录错误
            Logger.e(message = "Error in RemoteMediator: ${e.message}")

            // 返回错误
            MediatorResult.Error(e)
        }
    }


}

/**
 * 将文章返回响应类Response映射成UI需要的ArticleItem
 */
private fun responseMapToArticleItem(response: Response<PagingResponse<ArticleResponse>>): List<ArticleItem> {
    return response.data.datas.map {
        ArticleItem(
            id = it.id,
            title = it.title,
            link = it.link,
            author = it.author,
            shareUser = it.shareUser,
            niceDate = it.niceDate,
            chapterName = it.chapterName,
            superChapterName = it.superChapterName
        )
    }
}

/**
 * 网络文章数据源
 * 作用：纯网络加载
 */
class ArticlePagingSource(private val apiService: ApiService) : PagingSource<Int, ArticleItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleItem> {

        return try {
            //获取页码
            val page = params.key ?: 0
            //获取网络数据
            val response = apiService.getArticles(page)

            if (response.isSuccess()) {
                val articles = responseMapToArticleItem(response)
                LoadResult.Page(
                    data = articles,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (response.data.over) null else page + 1
                )

            } else {
                //返回错误
                LoadResult.Error(Exception(response.errorMsg))
            }
        } catch (e: Exception) {
            // 记录错误
            Logger.e(message = "Error loading articles: ${e.message}")
            // 返回错误
            LoadResult.Error(e)
        }

    }

    /**
     * 获取刷新键
     * @param state Paging 状态
     */
    override fun getRefreshKey(state: PagingState<Int, ArticleItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
