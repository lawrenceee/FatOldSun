package com.lj.fatoldsun.platform

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lj.fatoldsun.core.base.BaseLibActivity
import com.lj.fatoldsun.core.navigation.BottomNavController
import com.lj.fatoldsun.core.navigation.NavItem
import com.lj.fatoldsun.core.utils.Logger
import com.lj.fatoldsun.core.utils.StatusNavBarUtil
import com.lj.fatoldsun.platform.databinding.ActivityMainBinding
import com.lj.fatoldsun.platform.ui.HomeFragment
import com.lj.fatoldsun.platform.ui.WebsitesFragment
import com.zackratos.ultimatebarx.ultimatebarx.navigationBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MainActivity : BaseLibActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var bottomNavController: BottomNavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        //透明状态栏，导航栏侵入
        StatusNavBarUtil.setupStatusBar(this, isTransparent = true)
        StatusNavBarUtil.setupNavigationBar(this, fitWindow = false, isTransparent = true)
        //初始化底部导航
        bottomNavController =  BottomNavController(this, mBinding.bottomNav, mBinding.fragmentContainer.id)

        //依次添加导航项 一个个添加的方式让开发者可以在添加每个导航项时明确指定 fragmentProvider，并在需要时动态调整：
        //TestFragment().apply { arguments = Bundle().apply { putString("key", "value") } }

        bottomNavController.addNavItem(
            NavItem("Home", R.drawable.ic_home) { HomeFragment() }
        )

        bottomNavController.addNavItem(
            NavItem("Websites", R.drawable.ic_websites) { WebsitesFragment() }
        )
        //设置导航
        bottomNavController.setup()

    }



}