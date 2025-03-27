package com.lj.fatoldsun.core.utils

import java.net.UnknownHostException

object NetworkErrorHandler {
    fun handleError(throwable: Throwable): String {
        return when(throwable) {
            is UnknownHostException -> "网络异常，请检查网络设置"
            else -> "网络错误: ${throwable.message}"
        }
    }

}