package com.lj.fatoldsun.platform.vm


import com.lj.fatoldsun.core.base.BaseViewModel
import com.lj.fatoldsun.core.utils.Logger
import com.lj.fatoldsun.platform.model.entity.Website
import com.lj.fatoldsun.platform.repository.WebsitesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author LJ
 * @time 2025/03/20 15:36
 * @description:
 */
@HiltViewModel
class WebsitesViewModel @Inject constructor(private val websitesRepository: WebsitesRepository): BaseViewModel<List<Website>>() {
    private var isDataLoaded = false //添加标志，避免数据库数据重复加载
    init {
        if (!isDataLoaded) {
            launchLocal(
                block = {websitesRepository.getCacheWebsitesFromLocal()},
                onSuccess = {cacheWebsites ->
                    Logger.d("这是缓存数据：：：：：$cacheWebsites")
                    updateData(cacheWebsites)
                }
            )
            isDataLoaded = true
        }
    }



    fun fetchDataFromNet() {
        launchRequest(
            block = {websitesRepository.fetchWebsitesFromNetwork()},
            onSuccess = { data ->
                Logger.d("获取数据成功: ${data[0]}")
                websitesRepository.saveWebsites(data)
                Logger.d("这是网络数据：：：：：${data}")
                updateData(data)
            }
        )
    }



}