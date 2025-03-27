package com.lj.fatoldsun.core.base

import android.app.Application

/**
 * @author LJ
 * @time 2025/03/19 14:11
 * @description:
 * 通用 Application 基类
 * 提供全局 Application 上下文
 * 可扩展其他全局初始化逻辑（如日志、屏幕适配等）
 */
open class BaseLibApplication : Application() { //open 关键字允许子类继承

}