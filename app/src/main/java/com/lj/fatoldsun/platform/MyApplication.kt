package com.lj.fatoldsun.platform


import com.lj.fatoldsun.core.base.BaseLibApplication
import com.lj.fatoldsun.core.network.AuthInterceptor
import com.lj.fatoldsun.core.network.NetworkClient
import dagger.hilt.android.HiltAndroidApp

/**
 * @author LJ
 * @time 2025/03/19 13:54
 * @description:
 */
@HiltAndroidApp
class MyApplication : BaseLibApplication() {
    override fun onCreate() {
        super.onCreate()
        NetworkClient.init(
            baseUrl = "https://wanandroid.com/",
            interceptors = listOf(AuthInterceptor{"my_token"}) //模拟token
        )
    }
}
