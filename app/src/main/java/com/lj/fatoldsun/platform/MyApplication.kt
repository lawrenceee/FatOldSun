package com.lj.fatoldsun.platform


import com.lj.fatoldsun.core.base.BaseLibApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * @author LJ
 * @time 2025/03/19 13:54
 * @description:
 */
@HiltAndroidApp
class MyApplication : BaseLibApplication() {
    override fun onCreate() {
        super.onCreate()
        //初始化日志
    }
}
