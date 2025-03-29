package com.lj.fatoldsun.core.network

import com.lj.fatoldsun.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author LJ
 * @time 2025/03/21 13:41
 * @description:
 * 通用的网络请求工具，封装 Retrofit,支持动态baseUrl和通用拦截器
 * 提供动态切换baseUrl的能力，通过BaseUrlMap管理多个Retrofit实例
 * 支持自定义拦截器，处理通用请求头和相应逻辑
 * 单例模式，确保资源高效利用
 */
object NetworkClient {
    //默认baseUel,作为fallback
    private const val DEFAULT_BASE_URL = "https://www.wanandroid.com/"

    /**
     * 储存不同baseUrl对应的Retrofit实例. baseUrl切
     * 换的核心是Retrofit实例的管理，因为Retrofit的baseUrl一旦设置就不可变，所以需要多个实例,且
     * 为了保持Retrofit的单例特性（避免重复创建浪费资源），可以用一个Map存储多个Retrofit实
     * 例，每个baseUrl对应一个实例,用Map缓存提高效率。
     * @key baseUrl
     * @value Retrofit实例
     */
    private val baseUrlMap = mutableMapOf<String, Retrofit>()

    //自定义拦截器列表，外部可传入
    private var customInterceptors: List<Interceptor> = emptyList()

    //当前使用的baseUrl，默认为DEFAULT_BASE_URL
    private var currentBaseUrl: String = DEFAULT_BASE_URL

    //OkHttpClient单例，共享配置
    private val okHttpClient: OkHttpClient by lazy { buildOkHttpClient() }

    /**
     * 构建OKHttpClient,配置超市，日志和自定义连接器
     * @return OkHttpClient实例
     */
    private fun buildOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            //设置超时时间
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)

        //添加自定义拦截器,先添加列表里的，比如AuthInterceptor,不然日志拦截器的body看不到你拦截的添加的token
        customInterceptors.forEach { builder.addInterceptor(it) }

        //添加日志拦截器 仅Debug模式
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    /**
     * 初始化网络配置，可在Application中调用,应用启动时
     * @param  baseUrl 默认的baseUrl
     * @param interceptors 自定义拦截器列表
     */
    fun init(baseUrl: String = DEFAULT_BASE_URL, interceptors: List<Interceptor> = emptyList()) {
        currentBaseUrl = baseUrl
        customInterceptors = interceptors
        //预创建默认的baseUrl的Retrofit实例
        getOrCreateRetrofit(baseUrl)
    }

    /**
     * 动态切换baseUrl 应用运行时切换
     * @param newBaseUrl 新的BaseUrl
     */
    fun switchBaseUrl(newBaseUrl: String) {
        currentBaseUrl = newBaseUrl
        getOrCreateRetrofit(newBaseUrl)
    }


    /**
     * 获取或创建Retrofit实例
     * @param baseUrl 目标baseUrl
     * @return Retrofit实例
     */
    private fun getOrCreateRetrofit(baseUrl: String): Retrofit {
        return baseUrlMap.getOrPut(baseUrl) {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }



    /**
     *创建 API 服务接口
     * @param serviceClass API服务接口的 Class对象
     * @param baseUrl 可选参数，指定使用的baseUrl,默认使用currentBaseUrl
     * @return API服务接口实例
     */
    fun <T> createService(serviceClass: Class<T>, baseUrl: String = currentBaseUrl): T {
        return getOrCreateRetrofit(baseUrl).create(serviceClass)
    }
}