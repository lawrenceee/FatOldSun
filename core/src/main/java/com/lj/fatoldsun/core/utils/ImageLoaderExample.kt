package com.lj.fatoldsun.core.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.engine.GlideException

/**
 * @author LJ
 * @time 2025/04/20
 * @description:
 * 图片加载工具类使用示例
 * 提供Glide和Coil两种实现的使用方法
 * 可根据项目需求选择合适的图片加载框架
 */
class ImageLoaderExample {

    /**
     * 使用Glide加载图片示例
     */
    fun loadImageWithGlide(context: Context, url: String, imageView: ImageView, placeholderId: Int, errorId: Int) {
        // 基本加载
        GlideImageLoader.getInstance().loadImage(context, url, imageView)
        
        // 带占位图和错误图的加载
        GlideImageLoader.getInstance().loadImage(context, url, imageView, placeholderId, errorId)
        
        // 加载圆形图片
        GlideImageLoader.getInstance().loadCircleImage(context, url, imageView, placeholderId, errorId)
        
        // 加载圆角图片 (8dp圆角)
        GlideImageLoader.getInstance().loadRoundedImage(context, url, imageView, 8, placeholderId, errorId)
        
        // 加载高质量图片
        GlideImageLoader.getInstance().loadHighQualityImage(context, url, imageView)
        
        // 带淡入效果的图片加载
        GlideImageLoader.getInstance().loadImageWithFade(context, url, imageView, 300)
        
        // 带加载监听的图片加载
        GlideImageLoader.getInstance().loadImageWithListener(context, url, imageView, 
            object : GlideImageLoader.ImageLoadListener {
                override fun onLoadSuccess(drawable: Drawable) {
                    // 图片加载成功处理
                }
                
                override fun onLoadFailed(exception: GlideException?) {
                    // 图片加载失败处理
                }
            }
        )
    }
    
    /**
     * 使用Coil加载图片示例
     */
    fun loadImageWithCoil(context: Context, url: String, imageView: ImageView, placeholderId: Int, errorId: Int) {
        // 基本加载
        CoilImageLoader.getInstance().loadImage(context, url, imageView)
        
        // 带占位图和错误图的加载
        CoilImageLoader.getInstance().loadImage(context, url, imageView, placeholderId, errorId)
        
        // 加载圆形图片
        CoilImageLoader.getInstance().loadCircleImage(context, url, imageView, placeholderId, errorId)
        
        // 加载圆角图片 (8dp圆角)
        CoilImageLoader.getInstance().loadRoundedImage(context, url, imageView, 8, placeholderId, errorId)
        
        // 加载高质量图片
        CoilImageLoader.getInstance().loadHighQualityImage(context, url, imageView)
        
        // 带淡入效果的图片加载
        CoilImageLoader.getInstance().loadImageWithFade(context, url, imageView, 300)
        
        // 带加载监听的图片加载
        CoilImageLoader.getInstance().loadImageWithListener(context, url, imageView, 
            object : CoilImageLoader.ImageLoadListener {
                override fun onLoadSuccess(drawable: Drawable?) {
                    // 图片加载成功处理
                }
                
                override fun onLoadFailed(throwable: Throwable?) {
                    // 图片加载失败处理
                }
            }
        )
    }
    
    /**
     * 图片加载工具类选择器
     * 可以根据不同场景选择不同的图片加载框架
     */
    object ImageLoaderSelector {
        /**
         * 获取推荐的图片加载器
         * @param preferModern 是否偏好现代化API (true使用Coil, false使用Glide)
         * @return 图片加载器实例
         */
        fun getRecommendedLoader(preferModern: Boolean = true): Any {
            return if (preferModern) {
                // 推荐使用Coil - 更现代的API，更好的Kotlin集成
                CoilImageLoader.getInstance()
            } else {
                // 使用Glide - 更成熟稳定的选择
                GlideImageLoader.getInstance()
            }
        }
        
        /**
         * 清除所有图片加载器的缓存
         */
        fun clearAllCache() {
            // 清除Glide缓存
            GlideImageLoader.clearMemoryCache()
            // 在后台线程中清除磁盘缓存
            Thread {
                GlideImageLoader.clearDiskCache()
            }.start()
            
            // 清除Coil缓存
            CoilImageLoader.clearMemoryCache()
            CoilImageLoader.clearDiskCache() // Coil已在内部使用协程处理
        }
    }
}