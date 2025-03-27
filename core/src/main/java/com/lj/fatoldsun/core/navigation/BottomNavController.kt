package com.lj.fatoldsun.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * @author LJ
 * @time 2025/03/25 14:32
 * @description: 底部导航控制器，管理导航项和页面切换
 */
class BottomNavController (
    private val activity: FragmentActivity,
    private val navView: BottomNavigationView,
    private val containerId: Int
) {
    private val fragmentManager: FragmentManager = activity.supportFragmentManager
    private val navItems = mutableListOf<NavItem>()
    private val fragments = mutableMapOf<Int, Fragment>()
    private var currentFragment: Fragment? = null
    private var currentPosition: Int = -1 //记录当前导航项位置
    private var defaultPosition: Int = 0 //自定义：初始时，默认选中的位置

    /**
     * 添加导航项
     * @param navItem 导航项，包含标题、图标和 Fragment 提供函数
     * @param menuItemId 使用 navItems 列表的当前大小作为 MenuItem 的 ID,
     * 从 0 开始递增，确保每个导航项的 ID 唯一，且与 navItems 列表的索引对
     */
    fun addNavItem(navItem: NavItem) {
        val menuItemId = navItems.size
        navItems.add(navItem)
        /**
         * 向 BottomNavigationView 的菜单添加一个新的 MenuItem。
         * 参数说明：
         * 0：groupId，不分组。
         * menuItemId：itemId，菜单项的唯一 ID，与 navItems 索引对应。
         * menuItemId：order，菜单项的排序顺序，确保显示顺序与添加顺序一致。
         * navItem.title：菜单项的标题。
         */
        navView.menu.add(0, menuItemId, menuItemId, navItem.title)
            .setIcon(navItem.iconRes) //设置菜单项的图标
    }

    /**
     * 初始化导航
     */
    fun setup() {
        /**
         * 默认显示指定位置的页面，不设置setDefaultPosition()函数，默认第一页
         * @param initialPosition 初始导航项位置
         */
        if (navItems.isNotEmpty()) {
            val initialPosition = if(defaultPosition in 0 until  navItems.size) {
                defaultPosition
            } else {
                0
            }
                switchFragment(initialPosition)
                currentPosition = initialPosition
                navView.selectedItemId = initialPosition //高亮指定位置的导航项，默认第一个
        }
        navView.setOnItemSelectedListener { item ->
            val position = item.itemId
            if (position <  navItems.size ) { //确保 position 有效
                if (position == currentPosition) { //如果点击的是当前选中的导航项，则不执行切换
                    true //时间已处理但不切换
                } else {
                    switchFragment(position)
                    currentPosition = position
                    true

                }
            } else {
                false
            }
        }

    }

    /**
     * 设置默认选中的导航项位置
     * @param position 导航项位置
     */

    fun setDefaultPosition(position: Int) { if (position in 0 until navItems.size) defaultPosition = position }

    /**
     * 切换 Fragment
     * @param position 导航项位置
     */
    private fun switchFragment(position: Int) {
        val transaction = fragmentManager.beginTransaction()
        /**
         * 从 fragments 缓存中获取 position 对应的 Fragment。
         * 如果不存在（getOrPut 的 defaultValue 会被调用），
         * 则通过 navItems[position].fragmentProvider() 创建新的 Fragment 实例，并存入 fragments。
         */
        val targetFragment = fragments.getOrPut(position) { navItems[position].fragmentProvider() }

        //隐藏当前Fragment
        currentFragment?.let { transaction.hide(it) }

        //显示目标Fragment
        transaction.apply {
            if (targetFragment.isAdded) show(targetFragment)
            else add(containerId, targetFragment, "fragment_$position")
        }.commit()

        currentFragment = targetFragment
    }
}
