package com.lj.fatoldsun.core.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.load
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.MemoryCache
import com.lj.fatoldsun.core.base.BaseLibApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.temporal.TemporalQueries.precision

/**
 * @author LJ
 * @time 2025/04/20
 * @description:
 * Coil图片加载工具类
 * 提供高效、现代的图片加载功能
 * 基于Kotlin协程，支持多种图片来源、转换效果和加载配置
 * 相比Glide，Coil具有更小的库体积、更好的Kotlin支持和更现代的API设计
 */
class CoilImageLoader private constructor() {
    
    // 创建默认的ImageLoader实例
    private val imageLoader by lazy {
        ImageLoader.Builder(BaseLibApplication.getAppContext())
            .memoryCache {
                // 设置内存缓存大小为应用可用内存的25%
                MemoryCache.Builder(BaseLibApplication.getAppContext())
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                // 设置磁盘缓存大小为100MB
                DiskCache.Builder()
                    .directory(BaseLibApplication.getAppContext().cacheDir.resolve("image_cache"))
                    .maxSizeBytes(100 * 1024 * 1024) // 100MB
                    .build()
            }
            .crossfade(true) // 默认启用淡入淡出效果
            .components {
                // 添加GIF支持
                add(ImageDecoderDecoder.Factory())
                add(GifDecoder.Factory())
            }
            .build()
    }
    
    companion object {
        @Volatile
        private var instance: CoilImageLoader? = null
        
        /**
         * 获取CoilImageLoader单例实例
         * @return CoilImageLoader实例
         */
        fun getInstance(): CoilImageLoader {
            return instance ?: synchronized(this) {
                instance ?: CoilImageLoader().also { instance = it }
            }
        }
        
        /**
         * 清除内存缓存
         */
        fun clearMemoryCache() {
            getInstance().imageLoader.memoryCache?.clear()
        }
        
        /**
         * 清除磁盘缓存
         * 在协程中执行，不阻塞主线程
         */
        fun clearDiskCache() {
            CoroutineScope(Dispatchers.IO).launch {
                getInstance().imageLoader.diskCache?.clear()
            }
        }
    }
    
    /**
     * 加载图片（基础方法）
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     */
    fun loadImage(context: Context, url: String?, imageView: ImageView) {
        imageView.load(url, imageLoader)
    }
    
    /**
     * 加载图片（带占位图和错误图）
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     * @param placeholder 占位图资源ID
     * @param error 错误图资源ID
     */
    fun loadImage(
        context: Context,
        url: String?,
        imageView: ImageView,
        @DrawableRes placeholder: Int,
        @DrawableRes error: Int
    ) {
        imageView.load(url, imageLoader) {
            placeholder(placeholder)
            error(error)
        }
    }
    
    /**
     * 加载圆形图片
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     * @param placeholder 占位图资源ID
     * @param error 错误图资源ID
     */
    fun loadCircleImage(
        context: Context,
        url: String?,
        imageView: ImageView,
        @DrawableRes placeholder: Int = 0,
        @DrawableRes error: Int = 0
    ) {
        imageView.load(url, imageLoader) {
            transformations(CircleCropTransformation())
            if (placeholder != 0) placeholder(placeholder)
            if (error != 0) error(error)
        }
    }
    
    /**
     * 加载圆角图片
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     * @param radius 圆角半径（单位：dp）
     * @param placeholder 占位图资源ID
     * @param error 错误图资源ID
     */
    fun loadRoundedImage(
        context: Context,
        url: String?,
        imageView: ImageView,
        radius: Int,
        @DrawableRes placeholder: Int = 0,
        @DrawableRes error: Int = 0
    ) {
        // 转换dp为px
        val px = context.resources.displayMetrics.density * radius
        
        imageView.load(url, imageLoader) {
            transformations(RoundedCornersTransformation(px.toFloat()))
            if (placeholder != 0) placeholder(placeholder)
            if (error != 0) error(error)
        }
    }
    
    /**
     * 加载高质量图片
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     */
    fun loadHighQualityImage(context: Context, url: String?, imageView: ImageView) {
        imageView.load(url, imageLoader) {
            allowHardware(true) // 使用硬件加速
            allowRgb565(false) // 不使用RGB565格式（保持更高质量）
            precision(coil.size.Precision.EXACT) // 精确加载图片尺寸
            scale(Scale.FILL) // 填充目标
            diskCachePolicy(CachePolicy.ENABLED) // 启用磁盘缓存
            memoryCachePolicy(CachePolicy.ENABLED) // 启用内存缓存
        }
    }
    
    /**
     * 加载图片并监听加载状态
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     * @param listener 加载监听器
     */
    fun loadImageWithListener(
        context: Context,
        url: String?,
        imageView: ImageView,
        listener: ImageLoadListener
    ) {
        imageView.load(url, imageLoader) {
            listener(object : coil.request.Listener {
                override fun onStart(request: ImageRequest) {
                    super.onStart(request)
                }
                
                override fun onSuccess(request: ImageRequest, result: coil.request.SuccessResult) {
                    super.onSuccess(request, result)
                    listener.onLoadSuccess(result.drawable)
                }
                
                override fun onError(request: ImageRequest, result: coil.request.ErrorResult) {
                    super.onError(request, result)
                    listener.onLoadFailed(result.throwable)
                }
            })
        }
    }
    
    /**
     * 加载本地资源图片
     * @param context 上下文
     * @param resourceId 资源ID
     * @param imageView 目标ImageView
     */
    fun loadResourceImage(
        context: Context,
        @DrawableRes resourceId: Int,
        imageView: ImageView
    ) {
        imageView.load(resourceId, imageLoader)
    }
    
    /**
     * 加载本地文件图片
     * @param context 上下文
     * @param file 图片文件
     * @param imageView 目标ImageView
     */
    fun loadFileImage(context: Context, file: File, imageView: ImageView) {
        imageView.load(file, imageLoader)
    }
    
    /**
     * 加载Uri图片
     * @param context 上下文
     * @param uri 图片Uri
     * @param imageView 目标ImageView
     */
    fun loadUriImage(context: Context, uri: Uri, imageView: ImageView) {
        imageView.load(uri, imageLoader)
    }
    
    /**
     * 预加载图片
     * @param context 上下文
     * @param url 图片URL
     */
    fun preloadImage(context: Context, url: String?) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        imageLoader.enqueue(request)
    }
    
    /**
     * 加载图片并应用淡入效果
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     * @param durationMillis 淡入动画持续时间（毫秒）
     */
    fun loadImageWithFade(
        context: Context,
        url: String?,
        imageView: ImageView,
        durationMillis: Int = 300
    ) {
        imageView.load(url, imageLoader) {
            crossfade(durationMillis)
        }
    }
    
    /**
     * 加载GIF图片
     * @param context 上下文
     * @param url GIF图片URL
     * @param imageView 目标ImageView
     */
    fun loadGif(context: Context, url: String?, imageView: ImageView) {
        imageView.load(url, imageLoader)
    }
    
    /**
     * 图片加载监听接口
     */
    interface ImageLoadListener {
        /**
         * 图片加载成功回调
         * @param drawable 加载的Drawable
         */
        fun onLoadSuccess(drawable: Drawable?)
        
        /**
         * 图片加载失败回调
         * @param throwable 异常信息
         */
        fun onLoadFailed(throwable: Throwable?)
    }
}