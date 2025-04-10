package com.lj.fatoldsun.core.utils

import android.graphics.Color
import android.os.Build
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zackratos.ultimatebarx.ultimatebarx.navigationBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly

/**
 * @author LJ
 * @time 2025/03/26 13:38
 * @description:
 * 提供方法设置状态栏样式（透明度、字体颜色等）。
 * 提供方法根据背景颜色计算状态栏字体颜色（light 模式）。
 * 提供方法动态检测手机导航栏模式以及导航栏样式
 * 提供方法适配手势导航栏模式下BottomNavigationView padding
 * 支持在 Fragment 和 FragmentActivity 中使用。
 */
object StatusNavBarUtil {

    //设置Activity状态栏样式
    fun setupStatusBar(
        activity: FragmentActivity,
        isTransparent: Boolean = true,
        fitWindow: Boolean? = null, // 允许外部指定 fitWindow，如果为 null 则根据导航模式动态决定
        light: Boolean? = null, //true 灰色 false 白色
        backgroundColor: Int? = null
    ) {

        activity.statusBarOnly {
            if (isTransparent) transparent() else backgroundColor?.let {
                color = it
            } //是否透明状态栏，不是就根据背景颜色是否为空设置颜色
            this.light = light ?: backgroundColor?.let { calculateLightMode(it) } ?: true
            fitWindow?.let {this.fitWindow = it }
        }

    }

    //设置Fragment状态栏模式
    fun setupStatusBar(
        fragment: Fragment,
        isTransparent: Boolean = true,
        light: Boolean? = null,
        backgroundColor: Int? = null
    ) {

        fragment.statusBar {
            if (isTransparent) transparent() else backgroundColor?.let { color = it }
            this.light = light ?: backgroundColor?.let { calculateLightMode(it) } ?: true
        }
    }

    // 根据背景颜色计算状态栏字体颜色（light 模式）
    private fun calculateLightMode(backgroundColor: Int): Boolean {
        // 计算颜色的亮度（Luminance）
        val red = Color.red(backgroundColor)
        val green = Color.green(backgroundColor)
        val blue = Color.blue(backgroundColor)
        val luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255
        // 亮度大于 0.5 表示背景较亮，状态栏字体应为灰色（light = true）
        // 亮度小于等于 0.5 表示背景较暗，状态栏字体应为白色（light = false）
        return luminance > 0.5
    }

    //设置系统导航栏样式
    fun setupNavigationBar (
        activity: FragmentActivity,
        isTransparent: Boolean = true, //是否透明
        light: Boolean = true,
        fitWindow: Boolean? = null, // 允许外部指定 fitWindow，如果为 null 则根据导航模式动态决定
        backgroundColor: Int? = null
    ) {
        // 动态决定 fitWindow：手势导航时侵入（true），传统导航时不侵入（false）
        val finalFitWindow = fitWindow ?: isGestureNavigationEnabled(activity)

        activity.navigationBar {
            // 如果 非透明且backgroundColor 为 null，默认使用黑色
            color = if (isTransparent) {
                Color.TRANSPARENT
            } else {
                backgroundColor ?: Color.BLACK
            }
            this.light = light
            this.fitWindow = finalFitWindow
        }

    }

    // 设置 BottomNavigationView 的样式
    fun setupBottomNavigationView(
        bottomNav: BottomNavigationView,
        backgroundColor: Int? = null,
        itemIconTintRes: Int? = null
    ) {
        // 设置背景颜色
        backgroundColor?.let {
            bottomNav.setBackgroundColor(it)
        }

        // 设置图标和文字颜色
        itemIconTintRes?.let {
            bottomNav.itemIconTintList = ContextCompat.getColorStateList(bottomNav.context, it)
            bottomNav.itemTextColor = ContextCompat.getColorStateList(bottomNav.context, it)
        }

        // 如果使用手势导航，动态调整 BottomNavigationView 的内边距
        val activity = bottomNav.context as? FragmentActivity
        if (activity != null && isGestureNavigationEnabled(activity)) {
            bottomNav.setPadding(
                bottomNav.paddingLeft,
                bottomNav.paddingTop,
                bottomNav.paddingRight,
                bottomNav.paddingBottom + 16 // 增加底部内边距，避免手势区域遮挡
            )
        }
    }

     // 动态检测导航栏模式，是否为手势导航还是传统导航栏
    fun isGestureNavigationEnabled(activity: FragmentActivity) : Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //API大于30
            val windowInsetsController = activity.window.decorView.windowInsetsController
            //WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE 表示系统栏（包括导航栏）在手势导航模式下会通过滑动显示/隐藏
            windowInsetsController?.systemBarsBehavior == WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            //低于API 30,用废弃方法getIdentifier
            try {
                val resources = activity.resources
                val resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
                if (resourceId > 0) {
                    resources.getInteger(resourceId) == 2 // 0：传统三按钮导航。 1：两按钮导航。2：手势导航。
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }

    }



}