package com.lj.fatoldsun.core.network

import com.lj.fatoldsun.core.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author LJ
 * @time 2025/03/21 13:41
 * @description:
 * 通用的网络请求工具，封装 Retrofit
 */
object NetworkClient {
    private const val BASE_URL = "https://www.wanandroid.com/"
    //自定义OkHttpClient
    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            //设置超时时间
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        //在Debug模式下添加日志拦截器
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                builder.addInterceptor(loggingInterceptor)
            }

        builder.build()
    }

    //懒加载创建Retrofit实例
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     *创建 API 服务接口
     * @param serviceClass API服务接口的 Class对象
     * @return API 服务接口类型
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}