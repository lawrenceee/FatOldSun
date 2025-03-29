package com.lj.fatoldsun.core.network

import com.lj.fatoldsun.core.utils.Logger
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author LJ
 * @time 2025/03/28 17:39
 * @description:
 * 自动添加Authorization请求头
 * @param tokenProvider  是个函数类型参数，外部传入获取token的逻辑（比如从SharedPreferences取）
 */
class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原始请求
        val originalRequest = chain.request()

        //添加token到请求头
        val token = tokenProvider() ?: "default_token" //如果token为空，用默认值
        val modifiedRequest = originalRequest.newBuilder() //修改后生成新请求
            .header("Authorization", "Bearer $token")
            .build()

        //执行请求 返回响应
        val response = chain.proceed(modifiedRequest)
        if (response.code == 401) {
            Logger.e(message = "未授权，请重新登录")
        }

        return response

    }
}