package com.lj.fatoldsun.core.network

import androidx.core.app.NotificationCompat.MessagingStyle.Message

/**
 * @author LJ
 * @time 2025/03/24 15:02
 * @description:
 */
data class Response<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
) {
    /**
     *判断请求成功
     */
    fun isSuccess(): Boolean{return errorCode == 0}
}