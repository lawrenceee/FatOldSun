package com.lj.fatoldsun.platform.vm

import androidx.paging.PagingData
import com.lj.fatoldsun.core.base.BaseViewModel
import com.lj.fatoldsun.platform.model.entity.ArticleItem
import com.lj.fatoldsun.platform.model.response.BannerResponse
import com.lj.fatoldsun.platform.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author LJ
 * @time 2025/04/01 17:38
 * @description:
 * 首页数据管理以及逻辑
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository //注入仓库
) : BaseViewModel<List<BannerResponse>>() {


    fun fetchBannersFromNet() {
        launchRequest(
            block = { homeRepository.getBanners() },
            onSuccess = { bannerResponse ->
                updateData(bannerResponse)
            }

        )
    }

    /**
     * 获取纯网络文章数据
     * @return Flow<PagingData<ArticleItem>> 分页数据流
     * 作用：提供纯网络加载的文章数据
     */
    fun getArticlesFromNetwork(): Flow<PagingData<ArticleItem>> {
        //调取仓库方法，获取分页数据流
        return homeRepository.getArticlesFromNetwork()
    }

    /**
     * 获取网络+本地文章数据
     * @return Flow<PagingData<ArticleItem>> 分页数据流
     * 作用：提供网络和本地混合加载的文章数据
     */
    fun getArticlesWithMediator(): Flow<PagingData<ArticleItem>> {
        return homeRepository.getArticlesWithMediator()
    }

    /**
     * 刷新数据
    重新拉取 Banner 数据
     */
    fun refresh() {
        fetchBannersFromNet()
    }


}