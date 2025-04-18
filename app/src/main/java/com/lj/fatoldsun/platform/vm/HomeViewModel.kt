package com.lj.fatoldsun.platform.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lj.fatoldsun.core.base.BaseViewModel
import com.lj.fatoldsun.platform.model.entity.ArticleItem
import com.lj.fatoldsun.platform.model.response.BannerResponse
import com.lj.fatoldsun.platform.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    private val _articlesFlow = MutableStateFlow<PagingData<ArticleItem>>(PagingData.empty())
    val articlesFlow: StateFlow<PagingData<ArticleItem>> get() = _articlesFlow

    init {
        loadArticlesFromNetwork() //初始化时加载文章数据
    }

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
     * 提供纯网络加载的数据
     */
    private fun loadArticlesFromNetwork() {
        viewModelScope.launch {
            homeRepository.getArticlesFromNetwork()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _articlesFlow.value = pagingData

                }
        }
    }

    /**
     * 获取网络+本地文章数据
     * 提供网络和本地混合加载的文章数据
     */
    private fun loadArticlesWithMediator() {
        viewModelScope.launch {
            homeRepository.getArticlesWithMediator()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _articlesFlow.value = pagingData

                }
        }
    }


    /**
     * 刷新数据
    重新拉取 Banner 数据
     */
    fun refresh() {
        fetchBannersFromNet()
    }


}