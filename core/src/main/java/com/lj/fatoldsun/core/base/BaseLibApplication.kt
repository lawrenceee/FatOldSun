package com.lj.fatoldsun.core.base

import android.app.Application
import android.content.Context

/**
 * @author LJ
 * @time 2025/03/19 14:11
 * @description:
 * 通用 Application 基类
 * 提供全局 Application 上下文
 * 定义应用初始化的扩展点
 */
open class BaseLibApplication : Application() {
    companion object {
        private lateinit var instance: BaseLibApplication
        
        fun getInstance(): BaseLibApplication {
            return instance
        }
        
        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // 提供扩展点，而非具体实现
        initializeComponents()
    }
    
    /**
     * 应用组件初始化的扩展点
     * 子类可以重写此方法实现具体的初始化逻辑
     */
    protected open fun initializeComponents() {
        // 基类中为空实现
        // 具体的初始化逻辑由子类负责
    }
}