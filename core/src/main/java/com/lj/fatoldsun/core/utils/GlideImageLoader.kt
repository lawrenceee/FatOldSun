package com.lj.fatoldsun.core.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lj.fatoldsun.core.base.BaseLibApplication
import java.io.File

/**
 * @author LJ
 * @time 2023/05/20
 * @description:
 * Glide图片加载工具类
 * 提供高效、灵活的图片加载功能
 * 支持多种图片来源、转换效果和加载配置
 */
class GlideImageLoader private constructor() {

    companion object {
        @Volatile
        private var instance: GlideImageLoader? = null

        /**
         * 获取GlideImageLoader单例实例
         * @return GlideImageLoader实例
         */
        fun getInstance(): GlideImageLoader {
            return instance ?: synchronized(this) {
                instance ?: GlideImageLoader().also { instance = it }
            }
        }

        /**
         * 清除内存缓存
         * 建议在UI线程调用
         */
        fun clearMemoryCache() {
            Glide.get(BaseLibApplication.getAppContext()).clearMemory()
        }

        /**
         * 清除磁盘缓存
         * 必须在后台线程中调用
         */
        fun clearDiskCache() {
            Glide.get(BaseLibApplication.getAppContext()).clearDiskCache()
        }
    }

    /**
     * 加载图片（基础方法）
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     */
    fun loadImage(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .into(imageView)
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
        Glide.with(context)
            .load(url)
            .placeholder(placeholder)
            .error(error)
            .into(imageView)
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
        val requestOptions = RequestOptions()
            .transform(CircleCrop())

        if (placeholder != 0) {
            requestOptions.placeholder(placeholder)
        }
        if (error != 0) {
            requestOptions.error(error)
        }

        Glide.with(context)
            .load(url)
            .apply(requestOptions)
            .into(imageView)
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

        val requestOptions = RequestOptions()
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(px.toInt())))

        if (placeholder != 0) {
            requestOptions.placeholder(placeholder)
        }
        if (error != 0) {
            requestOptions.error(error)
        }

        Glide.with(context)
            .load(url)
            .apply(requestOptions)
            .into(imageView)
    }

    /**
     * 加载高质量图片
     * @param context 上下文
     * @param url 图片URL
     * @param imageView 目标ImageView
     */
    fun loadHighQualityImage(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888) // 使用更高质量的解码格式
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 缓存所有版本的图片
            .into(imageView)
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
        Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener.onLoadFailed(e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener.onLoadSuccess(resource)
                    return false
                }
            })
            .into(imageView)
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
        Glide.with(context)
            .load(resourceId)
            .into(imageView)
    }

    /**
     * 加载本地文件图片
     * @param context 上下文
     * @param file 图片文件
     * @param imageView 目标ImageView
     */
    fun loadFileImage(context: Context, file: File, imageView: ImageView) {
        Glide.with(context)
            .load(file)
            .into(imageView)
    }

    /**
     * 加载Uri图片
     * @param context 上下文
     * @param uri 图片Uri
     * @param imageView 目标ImageView
     */
    fun loadUriImage(context: Context, uri: Uri, imageView: ImageView) {
        Glide.with(context)
            .load(uri)
            .into(imageView)
    }

    /**
     * 预加载图片
     * @param context 上下文
     * @param url 图片URL
     */
    fun preloadImage(context: Context, url: String?) {
        Glide.with(context)
            .load(url)
            .preload()
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
        Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade(durationMillis))
            .into(imageView)
    }

    /**
     * 加载缩略图
     * @param context 上下文
     * @param url 图片URL
     * @param thumbnailUrl 缩略图URL
     * @param imageView 目标ImageView
     */
    fun loadImageWithThumbnail(
        context: Context,
        url: String?,
        thumbnailUrl: String?,
        imageView: ImageView
    ) {
        val thumbnailRequest: RequestBuilder<Drawable> = Glide.with(context)
            .load(thumbnailUrl)

        Glide.with(context)
            .load(url)
            .thumbnail(thumbnailRequest)
            .into(imageView)
    }

    /**
     * 加载GIF图片
     * @param context 上下文
     * @param url GIF图片URL
     * @param imageView 目标ImageView
     */
    fun loadGif(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .asGif()
            .load(url)
            .into(imageView)
    }

    /**
     * 图片加载监听接口
     */
    interface ImageLoadListener {
        /**
         * 图片加载成功回调
         * @param drawable 加载的Drawable
         */
        fun onLoadSuccess(drawable: Drawable)

        /**
         * 图片加载失败回调
         * @param exception 异常信息
         */
        fun onLoadFailed(exception: GlideException?)
    }
}