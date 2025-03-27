package com.lj.fatoldsun.core.navigation

import androidx.fragment.app.Fragment

/**
 * @author LJ
 * @time 2025/03/25 14:29
 * @description:底部导航项数据类
 * @param title 导航项标题
 * @param iconRes 导航项图标资源 ID
 * @param fragmentProvider 提供 Fragment 实例的函数
 */
data class NavItem (
    val title: String,
    val iconRes: Int,
    val fragmentProvider: () -> Fragment
)
