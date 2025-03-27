package com.lj.fatoldsun.core.base

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lj.fatoldsun.core.utils.StatusNavBarUtil
import me.jessyan.autosize.AutoSizeCompat

/**
 * @author LJ
 * @time 2025/03/19 14:14
 * @description:
 */
open class BaseLibActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //屏幕适配
        AutoSizeCompat.autoConvertDensityOfGlobal(resources)

    }

}